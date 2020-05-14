package rmhub.mod.weatherstation.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "weather_measurement")
public class WeatherMeasurement {

  @Id
  @GeneratedValue
  @Column
  private Long id;

  @Column
  private String eqtDtMes;

  @Column
  private String eqtMesId;

  @Column
  private int eqtMesLgId;

  @Column
  private int eqtMesLgType;

  @Column
  private int eqtMesNbVal;

  @Column
  private int eqtMesPer;

  @Column
  private String eqtMesType;

  @Column
  private Long physicalDeviceId;

  @Column(nullable = false)
  private String externalId;

  @Column
  private Integer deploymentId;

  @Builder.Default
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "weatherMeasurement", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<WeatherMeasurementDetail> weatherMeasurementDetails = new HashSet<>();

  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Date createdDate;
}
