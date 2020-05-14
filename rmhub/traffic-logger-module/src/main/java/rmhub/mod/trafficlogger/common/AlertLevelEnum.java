package rmhub.mod.trafficlogger.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum for Alert Level.
 *
 * @author Hino &lt;ntquan@cmc.com.vn&gt;
 * @deprecated Map directly to Integer instead. Marked as deprecated for future reuse.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Deprecated
public enum AlertLevelEnum {

  LEVEL_1(1),
  LEVEL_2(2),
  LEVEL_3(3),
  LEVEL_4(4),
  LEVEL_5(5);

  /**
   * Used for JSON Serialization.
   */
  @JsonValue
  @Getter
  private final Integer level;

  /**
   * Find the Enum instance that match the specified level.
   *
   * @throws IllegalArgumentException if no enum instance match
   */
  //TODO performance: should we create static final Map instead
  public static AlertLevelEnum of(Integer level) {
    // stream the values and find first enum instance that match the specified level
    return Arrays.stream(values()).filter(alertLevelEnum -> alertLevelEnum.getLevel().equals(level)).findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Level is invalid: " + level));
  }

  /**
   * Used for JSON Deserialization.
   */
  @JsonCreator
  public static AlertLevelEnum of(String level) {
    return of(Integer.valueOf(level));
  }
}
