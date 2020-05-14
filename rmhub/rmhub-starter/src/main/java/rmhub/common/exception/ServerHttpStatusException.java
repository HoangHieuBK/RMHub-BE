package rmhub.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

/**
 * Exception that raised when server cannot fulfill an apparently valid request.
 *
 * @deprecated temporarily use {@link BusinessException} instead.
 */
@Deprecated
public class ServerHttpStatusException extends RmhubHttpStatusException {

  private static final long serialVersionUID = -3801671524323617856L;

  public ServerHttpStatusException(String message) {
    this(message, null);
  }

  /**
   * Constructor with default status as {@link HttpStatus#INTERNAL_SERVER_ERROR} if not specified.
   */
  public ServerHttpStatusException(String message, Throwable cause) {
    super(message, cause);
    setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
  }

  /**
   * If httpStatus is specified, it must be client error (5xx).
   */
  public ServerHttpStatusException(HttpStatus httpStatus, String message, Throwable cause) {
    super(message, cause);
    Assert.isTrue(httpStatus.is5xxServerError(), "Must be server error!");
    setHttpStatus(httpStatus);
  }
}
