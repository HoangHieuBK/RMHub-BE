package rmhub.common.utility.exception;

import rmhub.common.exception.RmhubException;

/**
 * Used this for exception raised when mapping client input.
 */
public class RmhubJsonMappingException extends RmhubException {

  public RmhubJsonMappingException(String message) {
    super(message);
  }
}
