package rmhub.model.mivisu.ssil;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthMsgBody extends AbstractMsgBody {
  @JsonProperty("abo")
  String abo;
  @JsonProperty("pwd")
  String pwd;
}
