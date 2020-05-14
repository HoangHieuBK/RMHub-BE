package rmhub.mod.trafficlogger.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum StatusEnum {

  ACTIVE(true),
  DELETED(false);

  /**
   * Used for JSON Serialization.
   */
  @JsonValue
  @Getter
  private final Boolean status;

  /**
   * Find the Enum instance that match the specified status.
   *
   * @throws IllegalArgumentException if no enum instance match
   */
  public static StatusEnum of(Boolean status) {
    return Boolean.TRUE.equals(status) ? ACTIVE : DELETED;
  }

  /**
   * Used for JSON Deserialization.
   */
  @JsonCreator
  public static StatusEnum of(String status) {
    if ("true".equalsIgnoreCase(status))
      return ACTIVE;
    else if ("false".equalsIgnoreCase(status))
      return DELETED;
    throw new IllegalArgumentException("Status is invalid: " + status);
  }
}
