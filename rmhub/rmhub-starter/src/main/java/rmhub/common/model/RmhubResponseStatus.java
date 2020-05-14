package rmhub.common.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.lang.NonNull;

@Builder
@Data
public class RmhubResponseStatus {

  @NonNull
  private Integer code;

  @NonNull
  private String type;

  @NonNull
  private String message;

  @NonNull
  private Boolean error;

}
