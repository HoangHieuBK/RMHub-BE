package rmhub.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

public abstract class RmhubHttpStatusException extends RmhubException {

  private static final long serialVersionUID = -4039175506649667383L;

  @Getter
  @Setter(AccessLevel.PROTECTED)
  @NonNull
  private HttpStatus httpStatus;

  public RmhubHttpStatusException(String message) {
    this(message, null);
  }

  public RmhubHttpStatusException(String message, Throwable cause) {
    super(message, cause);
  }
}
