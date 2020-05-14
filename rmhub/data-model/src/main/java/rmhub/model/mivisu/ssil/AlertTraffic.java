package rmhub.model.mivisu.ssil;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"alertCode"})
public class AlertTraffic extends AlertBase {

  private Integer level;

  public AlertTraffic(String color, String description, Integer level) {
    super(color, description);
    this.level = level;
  }
}
