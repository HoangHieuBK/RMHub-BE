package rmhub.model.mivisu.ssil;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class AlertWeather extends AlertBase {

  public AlertWeather(String color, String description, String alertCode) {
    super(color, description, alertCode);
  }
}
