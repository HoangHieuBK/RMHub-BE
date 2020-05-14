package rmhub.model.mivisu.api;

import java.sql.Time;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MessageBase {
  int id;
  String deploymentId;
  Time timestamp;
  String requestId;
  int deviceType;
}
