package rmhub.common.core.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import rmhub.common.core.CmdMsgBase;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdBaseResult extends CmdMsgBase {

  private int status;
  private String message;
  // TODO: define more parameters command here
}
