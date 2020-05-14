package rmhub.common.utility.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RmhubJsonMappingExceptionTest {

  @Test
  void RmhubJsonMappingException_construct() {
    RmhubJsonMappingException ex = new RmhubJsonMappingException("exception expected!");
    Assertions.assertNotNull(ex);
  }
}
