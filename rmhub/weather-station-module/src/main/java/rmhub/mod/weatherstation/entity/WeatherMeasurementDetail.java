package rmhub.mod.weatherstation.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "weather_measurement_detail")
public class WeatherMeasurementDetail {

  @Id
  @GeneratedValue
  @Column
  private Long id;

  @Column
  private String eqtMesKlif;

  @Column
  private String eqtMesVal;

  @Column
  private Integer index;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.EAGER, optional = false, targetEntity = WeatherMeasurement.class)
  @JoinColumn(name = "wsMeasurementId", nullable = false)
  private WeatherMeasurement weatherMeasurement;
}
