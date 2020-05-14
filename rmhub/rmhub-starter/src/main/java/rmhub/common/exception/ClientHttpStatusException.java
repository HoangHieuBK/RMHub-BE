package rmhub.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;

/**
 * Exception that raised when client request contains bad syntax or cannot be fulfilled.
 *
 * @deprecated temporarily use {@link BusinessException} instead.
 */
@Deprecated
public class ClientHttpStatusException extends RmhubHttpStatusException {

  private static final long serialVersionUID = -2405096938662135293L;

  public ClientHttpStatusException(String message) {
    this(message, null);
  }

  /**
   * Constructor with default status as {@link HttpStatus#BAD_REQUEST} if not specified.
   */
  public ClientHttpStatusException(String message, Throwable cause) {
    super(message, cause);
    setHttpStatus(HttpStatus.BAD_REQUEST);
  }

  /**
   * If httpStatus is specified, it must be client error (4xx).
   */
  public ClientHttpStatusException(HttpStatus httpStatus, String message, Throwable cause) {
    super(message, cause);
    Assert.isTrue(httpStatus.is4xxClientError(), "Must be client error!");
    setHttpStatus(httpStatus);
  }
}
