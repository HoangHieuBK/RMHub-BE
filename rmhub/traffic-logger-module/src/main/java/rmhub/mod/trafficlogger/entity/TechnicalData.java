package rmhub.mod.trafficlogger.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "traffic_technical_data")
public class TechnicalData {

  @Id
  @GeneratedValue
  @Column
  private Long id;

  @Column(nullable = false)
  private String externalId;

  @Column
  private int eqtConfVersion;

  @Column
  private String etatDate;

  @Column
  private int etatSys;

  @Column
  private int etatAlim;

  @Column
  private int etatCom;

  @Column
  private Long physicalDeviceId;
}
