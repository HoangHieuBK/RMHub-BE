package rmhub.model.mivisu.ssil;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JacksonXmlRootElement(localName = "MIVISU")
@JsonRootName(value = "MIVISU")
public class SSILMessage {

  @JsonProperty("info_txt")
  private String info_txt;

  @JsonProperty("ENT")
  private SSILMessageHeader ent;

  @JsonProperty("CORPS")
  private AbstractMsgBody corps;

  public SSILMessage(AbstractMsgBody msgBody) {
    this.corps = msgBody;
  }
}
