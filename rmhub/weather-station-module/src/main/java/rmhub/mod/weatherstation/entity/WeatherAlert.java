package rmhub.mod.weatherstation.entity;

import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "weather_alert")
public class WeatherAlert {

  @Id
  @GeneratedValue
  @Column
  private Long id;

  @Column
  private String cause;

  @Column
  private Long deploymentId;

  @Column
  private String description;

  @Column
  private Boolean isHandled;

  @Column
  private Date handledAt;

  @Column
  private Long handledBy;

  @Column(nullable = false)
  private String externalId;

  @Column
  private Timestamp timestamp;

  @Column
  private Long physicalDeviceId;

  @Column
  private Long weatherMeasurementId;
}
