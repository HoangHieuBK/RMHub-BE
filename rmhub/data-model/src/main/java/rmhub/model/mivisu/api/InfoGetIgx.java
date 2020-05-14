package rmhub.model.mivisu.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InfoGetIgx implements Serializable {

  @JsonProperty("InfoGeneric")
  @JacksonXmlElementWrapper(useWrapping = false)
  List<InfoGeneric> infoGenerics;

  @JsonProperty("requestId")
  String requestId;

  @JsonProperty("deploymentId")
  Long deploymentId;
}
