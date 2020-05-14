package rmhub.model.weatherStation;

import java.io.Serializable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertRule implements Serializable {

  private Long id;

  @NotBlank
  private String alertCode;

  @NotBlank
  @Length(max = 100)
  private String content;

  @NotNull
  @Max(3)
  @Min(1)
  private Integer condition;

  @NotNull
  @Min(0)
  @Max(1000)
  private Integer value;

  @NotNull
  @Max(5)
  @Min(1)
  private Integer level;

  @NotBlank
  @Length(max = 30)
  private String color;

  // FIXME
  // @NotNull
  private Integer deploymentId = 1;
}
