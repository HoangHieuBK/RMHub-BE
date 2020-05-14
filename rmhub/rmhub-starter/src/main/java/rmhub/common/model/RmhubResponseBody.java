package rmhub.common.model;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

/**
 * The unified response body used inside RMHub system.
 */
@Builder
@Data
public class RmhubResponseBody<D> {

  private final Date timestamp = new Date();

  @NonNull
  private RmhubResponseStatus status;

  private List<ValidationError> errors;

  private D data;

  @NonNull
  private String path;

  public static <D> SimpleBuilder<D> simpleBuilder() {
    return new SimpleBuilder<>();
  }

  /**
   * Simplified builder for {@link RmhubResponseBody} with only necessary fields.
   */
  public static class SimpleBuilder<D> {

    private HttpStatus status;

    private String message;

    // errors will be an empty array instead of null
    private List<ValidationError> errors = Collections.emptyList();

    private D data;

    private String path;

    private SimpleBuilder() {
    }

    public SimpleBuilder<D> status(HttpStatus status) {
      this.status = status;
      return this;
    }

    public SimpleBuilder<D> message(String message) {
      this.message = message;
      return this;
    }

    public SimpleBuilder<D> errors(List<ValidationError> errors) {
      this.errors = errors;
      return this;
    }

    public SimpleBuilder<D> data(D data) {
      this.data = data;
      return this;
    }

    public SimpleBuilder<D> path(String path) {
      this.path = path;
      return this;
    }

    public RmhubResponseBody<D> build() {
      var status = RmhubResponseStatus.builder()
          .code(this.status.value())
          .type(this.status.getReasonPhrase())
          .message(this.message)
          .error(this.status.isError())
          .build();

      return new RmhubResponseBody<>(status, errors, data, path);
    }
  }
}
