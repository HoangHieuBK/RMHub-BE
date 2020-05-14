package rmhub.model.mivisu.api;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pool extends MesureBase {
  
  @JsonProperty("_text")
  String _text;
  
  @JsonProperty("channels")
  List<Channel> channels;
}
