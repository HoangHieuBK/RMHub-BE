package rmhub.common.helper;

import java.util.Collections;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import rmhub.common.model.RmhubResponseBody;

/**
 * This class is used instead of {@link ResponseEntity} to unify the response body template.
 *
 * @param <D> the generic type for data field
 */
public class RmhubResponseEntity<D> extends ResponseEntity<RmhubResponseBody<D>> {

  // =======================================================================================================================================
  // Constructors

  /**
   * Create a new {@code RmhubResponseEntity} with the given body, headers, and status code.
   *
   * @param body the entity body
   * @param headers the entity headers
   * @param status the status code
   */
  public RmhubResponseEntity(RmhubResponseBody<D> body, MultiValueMap<String, String> headers, HttpStatus status) {
    super(body, headers, status);
  }

  /**
   * Create a new {@code RmhubResponseEntity} with the given body and status code, and no headers.
   *
   * @param body the entity body
   * @param status the status code
   */
  public RmhubResponseEntity(RmhubResponseBody<D> body, HttpStatus status) {
    this(body, null, status);
  }

  // =======================================================================================================================================
  // Static builder methods

  /**
   * Create a {@code RmhubResponseEntity} with the given headers, status and necessary fields for building body.
   *
   * @return the created {@code RmhubResponseEntity}
   */
  public static <D> RmhubResponseEntity<D> with(MultiValueMap<String, String> headers, HttpStatus status, String message, D data,
      String path) {
    return new RmhubResponseEntity<>(
        RmhubResponseBody.<D>simpleBuilder()
            .status(status)
            .message(message)
            .data(data)
            .path(path)
            .build(),
        headers,
        status);
  }

  /**
   * Create a {@code RmhubResponseEntity} with the given status and necessary fields for building body.
   *
   * @return the created {@code RmhubResponseEntity}
   */
  public static <D> RmhubResponseEntity<D> with(HttpStatus status, String message, D data, String path) {
    return RmhubResponseEntity.with(null, status, message, data, path);
  }

  /**
   * Create a {@code RmhubResponseEntity} with the given resource data and the status set to {@linkplain HttpStatus#OK OK}.
   *
   * @return the created {@code RmhubResponseEntity}
   */
  public static <D> RmhubResponseEntity<D> with(String message, D data, String path) {
    return RmhubResponseEntity.with(HttpStatus.OK, message, data, path);
  }

  /**
   * Create a {@code RmhubResponseEntity} with the status set to {@linkplain HttpStatus#OK OK} and empty resource data.
   *
   * @return the created {@code RmhubResponseEntity}
   */
  public static RmhubResponseEntity<?> with(String message, String path) {
    return RmhubResponseEntity.with(HttpStatus.OK, message, Collections.emptyList(), path);
  }
}
