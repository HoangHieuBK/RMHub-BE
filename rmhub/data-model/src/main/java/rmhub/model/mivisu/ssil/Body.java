package rmhub.model.mivisu.ssil;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Body implements Serializable {

  @JsonIgnoreProperties
  @JsonProperty("Rep_Nb_Eqt")
  private int rep_Nb_Eqt;

  @JsonIgnoreProperties
  @JsonProperty("CENTRALE")
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<Centrale> centrale;

  @JsonIgnoreProperties
  @JsonProperty("Cr_Val")
  private int cr_Val;

  @JsonIgnoreProperties
  @JsonProperty("Cr_Txt_Len")
  private int cr_Txt_Len;

  @JsonIgnoreProperties
  @JsonProperty("Cr_Txt")
  private String cr_Txt;

  @JsonIgnoreProperties
  @JsonProperty("Nb_Et")
  private int nb_Et;

  @JsonIgnoreProperties
  @JsonProperty("ET")
  @JacksonXmlElementWrapper(useWrapping = false)
  private List<Et> et;

  @JsonIgnoreProperties
  @JsonProperty("Periode")
  private int periode;
}
