package rmhub.model.mivisu.ssil;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Centrale implements Serializable {

  @JsonProperty("ID_EXT")
  private String id_ext;

  @JsonProperty("Eqt_Nb_Mesure")
  private int eqt_Nb_Mesure;

  @JsonProperty("MESURE")
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<Map<String, Object>> mesure = new ArrayList<>();
}
