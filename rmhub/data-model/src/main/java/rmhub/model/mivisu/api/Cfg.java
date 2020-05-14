package rmhub.model.mivisu.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Cfg implements Serializable {

  @JsonProperty("type")
  private String type;

  @JsonProperty("idInterne")
  private String id_interne;

  @JsonProperty("profil")
  private String profil;

  @JsonProperty("sousType")
  private String sous_type;

  @JsonProperty("eqtActif")
  private String eqt_actif;

  @JsonProperty("description")
  private String description;

  @JsonProperty("mesures")
  private Mesure mesure;
}
