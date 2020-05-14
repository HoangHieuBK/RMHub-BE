package rmhub.model.mivisu.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Mesure implements Serializable {
  
  @JsonProperty("pool")
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<Pool> pools;
  
//  Map<String, Object> mesure = new LinkedHashMap<>();
//
//  @JsonAnySetter
//  public void set(String key, Object value) {
//    mesure.put(key, value);
//  }
}
