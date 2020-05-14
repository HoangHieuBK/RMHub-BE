package rmhub.mod.trafficlogger.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
public class CreateTrafficAlertSettingDto extends UpdateTrafficAlertSettingDto {

}
