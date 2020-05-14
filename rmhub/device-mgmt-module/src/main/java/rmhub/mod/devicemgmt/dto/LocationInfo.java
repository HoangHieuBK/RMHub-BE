package rmhub.mod.devicemgmt.dto;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rmhub.mod.devicemgmt.common.DeviceConst;

@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
public class LocationInfo {

  @NotNull
  @DecimalMax(value = DeviceConst.LATITUDE_MAX)
  @DecimalMin(value = DeviceConst.LATITUDE_MIN)
  private Double latitude;

  @NotNull
  @DecimalMax(value = DeviceConst.LONGITUDE_MAX)
  @DecimalMin(value = DeviceConst.LONGITUDE_MIN)
  private Double longitude;
}
