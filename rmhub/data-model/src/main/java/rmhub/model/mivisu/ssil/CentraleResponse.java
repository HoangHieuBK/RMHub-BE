package rmhub.model.mivisu.ssil;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CentraleResponse {

  @JsonProperty("MESURE")
  private List<Mesure> mesures;
  
  @JsonProperty("Eqt_Nb_Mesure")
  private int eqt_Nb_Mesure;

  @JsonProperty("externalId")
  private String externalId;
}
