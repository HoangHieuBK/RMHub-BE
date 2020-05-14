package rmhub.common.utility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import rmhub.common.exception.RmhubException;

public class ParserHelperTest {

  @Test
  void convertString2Int_Ok() {
    String val = "1";
    Assertions.assertEquals(1, ParserHelper.convertString2Int(val));
  }

  @Test
  void convertString2Int_nullValue() {
    String val = null;
    RmhubException ex = Assertions.assertThrows(RmhubException.class, () -> ParserHelper.convertString2Int(val));
    Assertions.assertNotNull(ex);
  }

  @Test
  void convertString2Int_emptyValue() {
    String val = "";
    RmhubException ex = Assertions.assertThrows(RmhubException.class, () -> ParserHelper.convertString2Int(val));
    Assertions.assertNotNull(ex);
  }
}
