package rmhub.common.handler;

import java.util.Collections;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;
import rmhub.common.common.ErrorCode;
import rmhub.common.exception.BusinessException;
import rmhub.common.exception.RmhubException;
import rmhub.common.exception.RmhubHttpStatusException;
import rmhub.common.helper.RmhubResponseEntity;
import rmhub.common.model.RmhubResponseBody;
import rmhub.common.model.ValidationError;
import rmhub.common.util.PathUtil;
import rmhub.common.utility.exception.RmhubJsonMappingException;

/**
 * Parent class for global exception handler inside rmhub system.
 *
 * @author Hino &lt;ntquan@cmc.com.vn&gt;
 */
@Slf4j
@RestControllerAdvice
public class RmhubExceptionHandler extends ResponseEntityExceptionHandler implements MessageSourceAware {

  protected MessageSource messageSource;

  @Override
  public void setMessageSource(@Nullable MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  /**
   * Override default <b>MethodArgumentNotValidException</b> handler for being too verbose.
   */
  @Override
  @NonNull
  protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, @Nullable HttpHeaders headers,
      HttpStatus status, @NonNull WebRequest request) {

    // FIXME this is kinda stupid algorithm, will re-implement later
    // first get all the errors
    var errorList = ex.getBindingResult().getAllErrors();

    // we will handle Object Error in different way from Field Error
    if (!(errorList.get(0) instanceof FieldError)) {
      var body = RmhubResponseBody.simpleBuilder()
          .status(status)
          .message(messageSource.getMessage(errorList.get(0), request.getLocale()))
          .errors(Collections.emptyList())
          .data(Collections.emptyList()) // the data should be empty
          .path(PathUtil.getPath(request))
          .build();

      return ResponseEntity.status(status).headers(headers).body(body);
    }
    // get the binding errors
    var errors = ex.getBindingResult().getAllErrors()
        .stream()
        .map(objectError -> ValidationError.builder()
            .field(((FieldError) objectError).getField())
            .message(messageSource.getMessage(objectError, request.getLocale()))
            .build())
        .collect(Collectors.toList());

    // build the response body
    var body = RmhubResponseBody.simpleBuilder()
        .status(status)
        .message("Validation failed for object: " + ex.getBindingResult().getObjectName())
        .errors(errors)
        .data(Collections.emptyList()) // the data should be empty
        .path(PathUtil.getPath(request))
        .build();

    return ResponseEntity.status(status).headers(headers).body(body);
  }

  /**
   * Handle exception for HTTP-based request.
   *
   * @deprecated temporarily use {@link BusinessException} instead.
   */
  @ExceptionHandler(RmhubHttpStatusException.class)
  @Deprecated
  public ResponseEntity<?> handleRmhubHttpStatusException(RmhubHttpStatusException ex, WebRequest request) {

    return handleExceptionInternal(ex, ex.getHttpStatus(), request);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {

    return handleExceptionInternal(ex, HttpStatus.BAD_REQUEST, request);
  }

  @ExceptionHandler(RmhubJsonMappingException.class)
  public ResponseEntity<?> handleJsonMapping(RmhubJsonMappingException ex, WebRequest request) {

    return handleExceptionInternal(ex, HttpStatus.BAD_REQUEST, request);
  }

  /**
   * Handle exception during JSON Serialization.
   */
  @ExceptionHandler(HttpMessageConversionException.class)
  public ResponseEntity<?> handleJsonConverterException(HttpMessageConversionException ex, WebRequest request) {

    // handle the cause for more readable message, specified our status code (400) instead of 500
    return handleExceptionInternal((Exception) ex.getCause(), HttpStatus.BAD_REQUEST, request);
  }

  /**
   * Handle exception caused by repository layer development.
   */
  @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
  public ResponseEntity<?> handleIncorrectResultSizeDataAccessException(IncorrectResultSizeDataAccessException ex, WebRequest request) {

    // first log the error for further investigation
    log.error("Exception occurs:", ex);

    return RmhubResponseEntity.with(null, HttpStatus.INTERNAL_SERVER_ERROR,
        "There is error when implementing repository layer, please contact your development team!",
        Collections.emptyList(),
        PathUtil.getPath(request));
  }

  /**
   * Handle business exception with corresponding {@link ErrorCode}.
   */
  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<?> handleBusinessException(BusinessException ex, WebRequest request) {

    return handleExceptionInternal(ex, ex.getErrorCode().getHttpStatus(), request);
  }

  /**
   * When there is no handler for more specific exceptions, this handler kicks in.
   */
  @ExceptionHandler(RmhubException.class)
  public ResponseEntity<?> handleRmhubException(RmhubException ex, WebRequest request) {

    // because no specific handler is called, we assume that it's our server error
    return handleExceptionInternal(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
  }

  /**
   * We should also handle any other Exceptions so that the response body always follows our template
   */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleException(Throwable ex, WebRequest request) {

    return handleExceptionInternal((Exception) ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
  }

  /**
   * Customize the response body of all Exception types.
   */
  @Override
  @NonNull
  protected ResponseEntity<Object> handleExceptionInternal(@NonNull Exception ex, @Nullable Object body, HttpHeaders headers,
      HttpStatus status, @NonNull WebRequest request) {

    // for not spamming the logger, log exception in case of server error only
    if (status.is5xxServerError()) {
      log.error("Exception occurs:", ex);
    }

    if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
      request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
    }

    // ignore the body from method param, avoid inconsistent error response
    var responseBody = RmhubResponseBody.simpleBuilder()
        .status(status)
        .message(getMessage(ex))
        .errors(Collections.emptyList()) // the errors array should be empty
        .data(Collections.emptyList()) // the data should be empty
        .path(PathUtil.getPath(request))
        .build();

    return ResponseEntity.status(status).headers(headers).body(responseBody);
  }

  /**
   * Use this method when you delegate the response body to parent class.
   */
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    return handleExceptionInternal(ex, null, headers, status, request);
  }

  /**
   * Use this method when you delegate the response body to parent class and have nothing to do with the headers.
   */
  protected ResponseEntity<Object> handleExceptionInternal(Exception ex, HttpStatus status, WebRequest request) {
    return handleExceptionInternal(ex, null, null, status, request);
  }

  /**
   * Extract message from the exception.
   * <br>You can override this method to be used at your handler but please be sure that it should not be null.
   * <br>TODO implement i18n
   */
  @NonNull
  protected String getMessage(Exception ex) {

    // try to get message from the exception
    if (ex.getMessage() != null) {
      return ex.getMessage();
    }

    return "Take a sip of coffee, we'll be right back!";
  }
}
