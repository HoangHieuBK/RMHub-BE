package rmhub.model.mivisu.ssil;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Mesure {

  @JsonProperty("NUM")
  int num;

  @JsonProperty("Eqt_Dt_Mes")
  String eqt_Dt_Mes;

  @JsonProperty("Eqt_Mes_Per")
  int eqt_Mes_Per;

  @JsonProperty("Eqt_Mes_Lg_Id")
  int eqt_Mes_Lg_Id;

  @JsonProperty("Eqt_Mes_Id")
  String eqt_Mes_Id;

  @JsonProperty("Eqt_Mes_Lg_Type")
  int eqt_Mes_Lg_Type;

  @JsonProperty("Eqt_Mes_Type")
  String eqt_Mes_Type;

  @JsonProperty("Eqt_Mes_Nb_Val")
  int eqt_Mes_Nb_Val;

  @JsonIgnoreProperties
  @JsonProperty("Eqt_Mes_Val_1")
  int eqt_Mes_Val_1;

  @JsonIgnoreProperties
  @JsonProperty("Eqt_Mes_Klif_1")
  String eqt_Mes_Klif_1;

  //Alert alert;
  AlertBase alertBase;
}
