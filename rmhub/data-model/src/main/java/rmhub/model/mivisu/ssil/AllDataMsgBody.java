package rmhub.model.mivisu.ssil;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true, value = "CORPS")
public class AllDataMsgBody extends AbstractMsgBody{

  @JsonProperty("Eqt_Id")
  private String eqt_Id;
}
