package rmhub.mod.trafficlogger.dto.request;

import javax.validation.GroupSequence;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;
import rmhub.mod.trafficlogger.common.TrafficAlertConst;
import rmhub.mod.trafficlogger.dto.MinMaxDto;
import rmhub.mod.trafficlogger.validator.group.Correlation;
import rmhub.mod.trafficlogger.validator.group.Format;

@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@GroupSequence({UpdateTrafficAlertSettingDto.class, Format.class, Correlation.class})
public class UpdateTrafficAlertSettingDto extends MinMaxDto {

  @NotNull
  @Min(TrafficAlertConst.MIN_LEVEL)
  @Max(TrafficAlertConst.MAX_LEVEL)
  private Integer level;

  @Pattern(regexp = TrafficAlertConst.COLOR_CODE_REGEX, groups = Format.class)
  @NotBlank
  private String color;

  @Length(max = TrafficAlertConst.DESCRIPTION_MAX_LENGTH)
  @NotBlank
  private String description;

  @NotNull
  @Min(TrafficAlertConst.MIN_SPEED)
  @Max(TrafficAlertConst.MAX_SPEED)
  public Integer getMin() {
    return super.getMin();
  }

  @NotNull
  @Min(TrafficAlertConst.MIN_SPEED)
  @Max(TrafficAlertConst.MAX_SPEED)
  public Integer getMax() {
    return super.getMax();
  }
}
