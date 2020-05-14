package rmhub.model.mivisu.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Channel extends MesureBase {

  @JsonProperty("_text")
  private String _text;

  @JsonProperty("natures")
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<Nature> natures;
}
