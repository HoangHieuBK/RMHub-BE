package rmhub.mod.trafficlogger.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import rmhub.mod.trafficlogger.validator.MinMaxCorrelation;
import rmhub.mod.trafficlogger.validator.group.Correlation;

@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
@MinMaxCorrelation(min = "min", max = "max", groups = Correlation.class)
public class MinMaxDto {

  private Integer min;

  private Integer max;
}
