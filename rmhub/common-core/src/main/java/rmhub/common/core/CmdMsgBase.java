package rmhub.common.core;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CmdMsgBase {

  private String requestId;
  private Long deploymentId;
  private Date timestamp;
}
