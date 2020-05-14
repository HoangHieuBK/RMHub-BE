package rmhub.common.utility;

import rmhub.common.exception.RmhubException;

public class ParserHelper {

  private ParserHelper() {
  }

  public static int convertString2Int(String value) {
    if (value != null && !value.isEmpty()) {
      return Integer.parseInt(value);
    }
    throw new RmhubException("Value must be not null or empty");
  }
}
