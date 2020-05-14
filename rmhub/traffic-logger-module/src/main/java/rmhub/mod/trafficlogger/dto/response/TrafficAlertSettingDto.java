package rmhub.mod.trafficlogger.dto.response;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TrafficAlertSettingDto implements Serializable {

  private Long id;

  private Integer level;

  private String color;

  private String description;

  private Integer min;

  private Integer max;

  private Date createdDate;

  private Date lastModifiedDate;
}
