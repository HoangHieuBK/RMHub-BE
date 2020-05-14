package rmhub.model.mivisu.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseMsgBase extends MessageBase{
  int status;
  String message;
}
