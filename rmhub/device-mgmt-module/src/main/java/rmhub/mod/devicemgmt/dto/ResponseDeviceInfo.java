package rmhub.mod.devicemgmt.dto;

import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDeviceInfo {
  
  private Long deviceTypeId;
  private Long id;
  private Integer eqt_actif;
  private String externalId;
  private String deviceName;
  private String description;
  private Date lastUpdate;
  private Double latitude;
  private Double longitude;
  private List<PoolInfo> pools;
}
