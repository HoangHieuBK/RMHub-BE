package rmhub.model.mivisu.api;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Crtxt implements Serializable {
  @JsonProperty("info_get_igx")
  InfoGetIgx igxGetIgx;
}
