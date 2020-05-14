package rmhub.mod.trafficlogger.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AlertLevelEnumTest {

  @Test
  void staticConstructor_ofInteger() {
    Assertions.assertSame(AlertLevelEnum.LEVEL_1, AlertLevelEnum.of(1));
  }

  @Test
  void staticConstructor_ofString() {
    Assertions.assertSame(AlertLevelEnum.LEVEL_5, AlertLevelEnum.of("5"));
  }

  @Test
  void staticConstructor_invalid_shouldFail() {
    var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> AlertLevelEnum.of(7));
    Assertions.assertEquals("Level is invalid: 7", exception.getMessage());
  }
}
