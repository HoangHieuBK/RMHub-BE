package rmhub.mod.weatherstation.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "weather_alert_setting")
public class WeatherAlertRule implements Serializable {

  @Id
  @GeneratedValue
  @Column
  private Long id;

  @Column
  private String alertCode;

  @Column
  private String content;

  @Column
  private Integer condition;

  @Column
  @Max(1000)
  private Integer value;

  @Column
  private Integer level;

  @Builder.Default
  @Column
  private Boolean status = true;

  @Column
  private String color;

  @Builder.Default
  @Column
  private Integer deploymentId = 1;
}
