package rmhub.common.exception;

import lombok.Getter;
import rmhub.common.common.ErrorCode;

/**
 * Business exception inside Rmhub system.
 */
public class BusinessException extends RmhubException {

  private static final long serialVersionUID = 1L;

  @Getter
  protected ErrorCode errorCode;

  public BusinessException(ErrorCode errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }
}
