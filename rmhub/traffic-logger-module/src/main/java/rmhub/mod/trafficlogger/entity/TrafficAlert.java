package rmhub.mod.trafficlogger.entity;

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
import org.hibernate.annotations.CreationTimestamp;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "traffic_alert")
public class TrafficAlert {

  @Id
  @GeneratedValue
  @Column
  private Long id;

  @Column
  private Long trafficMeasurementId;

  @Column
  private Integer deploymentId;

  @Column(nullable = false)
  private String externalId;

  @Column
  private String description;

  @Column
  private String cause;

  @Column
  private Boolean isHandled;

  @Column
  private Date handledAt;

  @Column
  private String handledBy;

  /**
   * Auto-generated create date by Spring Data framework. <br>
   * TODO can be separated using Spring data Auditable interface
   */
  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Date createdDate;
}
