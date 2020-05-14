package rmhub.model.mivisu.ssil;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertBase {

  protected String color;
  protected String description;
  private String alertCode;

  public AlertBase(String color, String description) {
    this.color = color;
    this.description = description;
  }
}
