package rmhub.model.mivisu.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ClassMesure extends MesureBase {

  @JsonProperty("mesureId")
  private MesureId id_mesure;

  @JsonProperty("type")
  private MesureType mesureType;

  @JsonProperty("libelle")
  private Libelle mesureLibelle;
}
