package rmhub.model.mivisu.api;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Nature extends MesureBase {
  @JsonProperty("period")
  @JacksonXmlElementWrapper(useWrapping = false)
  List<Period> periods;
}
