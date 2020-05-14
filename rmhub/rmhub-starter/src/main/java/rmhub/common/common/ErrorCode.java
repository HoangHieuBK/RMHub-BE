package rmhub.common.common;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

/**
 * Business Error Code that is described with corresponding {@link HttpStatus} for being handled by a REST handler.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

  NOT_FOUND(HttpStatus.NOT_FOUND),
  BAD_REQUEST(HttpStatus.BAD_REQUEST),
  CONFLICT(HttpStatus.CONFLICT),
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

  @Getter
  private final HttpStatus httpStatus;
}
