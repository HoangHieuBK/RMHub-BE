package rmhub.common.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

@Builder
@Data
public class ValidationError {

  @NonNull
  private String field;

  @NonNull
  private String message;
}
