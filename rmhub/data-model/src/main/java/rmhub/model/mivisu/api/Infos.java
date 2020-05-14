package rmhub.model.mivisu.api;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName(value = "INFOS")
public class Infos implements Serializable {

  @JsonProperty("CRVAL")
  int crval;

  @JsonProperty("CRTXT")
  Crtxt crtxt;

  @JsonProperty("requestId")
  String requestId;
}
