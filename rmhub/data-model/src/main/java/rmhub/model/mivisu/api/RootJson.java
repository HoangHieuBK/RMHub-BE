package rmhub.model.mivisu.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class RootJson {
  @JsonProperty("INFOS")
  Infos infos;
}
