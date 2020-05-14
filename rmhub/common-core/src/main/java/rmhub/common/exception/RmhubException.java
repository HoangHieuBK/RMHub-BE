package rmhub.common.exception;

import org.springframework.lang.NonNull;

/**
 * Parent and also wrapper of those exceptions that can be thrown during the normal operation of RMHub system.
 *
 * @author Hino &lt;ntquan@cmc.com.vn&gt;
 */
public class RmhubException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  /**
   * You can not instantiate this exception for no reason!
   */
  @SuppressWarnings("unused")
  private RmhubException() {
    super();
    throw new UnsupportedOperationException("Not client-friendly!");
  }

  /**
   * We don't use this because the message would be generated from {@link Throwable#toString()} and thus not client-friendly.
   */
  @SuppressWarnings("unused")
  private RmhubException(Throwable cause) {
    super(cause);
    throw new UnsupportedOperationException("Not client-friendly!");
  }

  /**
   * Use this for exception raised by rmhub business code.
   */
  public RmhubException(@NonNull String message) {
    super(message);
  }

  /**
   * Use this when you want to wrap the exception so that it can be easily handled by rmhub system.
   */
  public RmhubException(@NonNull String message, Throwable cause) {
    super(message, cause);
  }
}
