package rmhub.mod.trafficlogger.dto.response;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrafficAlertDto {

  private Long id;

  private Long physicalDeviceId;

  private Long trafficMeasurementId;

  private Long deploymentId;

  private Long externalId;

  private String description;

  private String cause;

  private Boolean isHandled;

  private Date handledAt;

  private String handledBy;

  private Date createdDate;
}
