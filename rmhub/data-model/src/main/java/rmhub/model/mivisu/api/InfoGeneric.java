package rmhub.model.mivisu.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class InfoGeneric implements Serializable {

  @JsonProperty("titre")
  String titre;

  @JsonProperty("DATEINFO")
  DateInfo dateInfo;

  @JsonProperty("SOURCE")
  Source source;

  @JsonProperty("CONTEXTE")
  Contexte contexte;

  @JsonProperty("DESCRIPTION")
  Description description;

  @JsonProperty("TYPE ")
  Type type;

  @JsonProperty("SSTYPE")
  Sstype sstype;

  @JsonProperty("ID")
  Id id;

  @JsonProperty("TITRE")
  Titre objTitre;

  @JsonProperty("DATEUPDATE")
  DateUpdate dateUpdate;

  @JsonProperty("DATEFIN")
  DateFin dateFin;

  @JsonProperty("cfg")
  Cfg cfg;
}
