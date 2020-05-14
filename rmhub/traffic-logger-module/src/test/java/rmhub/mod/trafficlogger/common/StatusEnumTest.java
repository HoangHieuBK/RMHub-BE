package rmhub.mod.trafficlogger.common;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StatusEnumTest {

  @Test
  void staticConstructor_ofBoolean_true() {
    Assertions.assertSame(StatusEnum.ACTIVE, StatusEnum.of(true));
    Assertions.assertSame(StatusEnum.ACTIVE.getStatus(), Boolean.TRUE);
  }

  @Test
  void staticConstructor_ofBoolean_false() {
    Assertions.assertSame(StatusEnum.DELETED, StatusEnum.of(false));
  }

  @Test
  void staticConstructor_ofString_true() {
    Assertions.assertSame(StatusEnum.ACTIVE, StatusEnum.of("true"));
  }

  @Test
  void staticConstructor_ofString_false() {
    Assertions.assertSame(StatusEnum.DELETED, StatusEnum.of("false"));
  }

  @Test
  void staticConstructor_invalidString_shouldFail() {
    var exception = Assertions.assertThrows(IllegalArgumentException.class, () -> StatusEnum.of("test"));
    Assertions.assertEquals("Status is invalid: test", exception.getMessage());
  }
}
