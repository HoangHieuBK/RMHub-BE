package rmhub.mod.trafficlogger.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * TODO Used for updating an alert information or when being handled.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTrafficAlertDto {

  private String description;

  private Boolean isHandled;
}
