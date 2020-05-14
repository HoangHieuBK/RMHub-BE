package rmhub.model.mivisu.ssil;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MivisuXml implements Serializable {

  @JsonProperty("info_txt")
  private String info_txt;

  @JsonProperty("ENT")
  private SSILMessageHeader ssilMessageHeader;

  @JsonProperty("CORPS")
  private Body body;

  @JsonIgnoreProperties
  private int deploymentId;

  @JsonIgnoreProperties
  private Date receivedDate = new Date();
}
