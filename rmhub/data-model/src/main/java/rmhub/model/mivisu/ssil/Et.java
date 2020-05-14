package rmhub.model.mivisu.ssil;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Et implements Serializable {

  @JsonProperty("ID_EXT")
  private String id_ext;

  @JsonProperty("Eqt_Conf_Version")
  private int eqt_Conf_Version;

  @JsonProperty("Etat_Date")
  private String etat_Date;

  @JsonProperty("Etat_Sys")
  private int etat_Sys;

  @JsonProperty("Etat_Alim")
  private int etat_Alim;

  @JsonProperty("Etat_Com")
  private int etat_Com;

  @JsonIgnoreProperties
  @Builder.Default
  private boolean isInCache = false;
}
