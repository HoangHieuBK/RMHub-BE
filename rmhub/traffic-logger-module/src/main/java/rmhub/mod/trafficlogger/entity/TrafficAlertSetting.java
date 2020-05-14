package rmhub.mod.trafficlogger.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import rmhub.mod.trafficlogger.common.StatusEnum;
import rmhub.mod.trafficlogger.entity.converter.StatusConverter;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "traffic_alert_setting")
public class TrafficAlertSetting {

  @Id
  @GeneratedValue
  @Column
  private Long id;

  @NotNull
  @Column(nullable = false)
  private Integer level;

  /**
   * color code
   */
  @NotNull
  @Column
  private String color;

  @NotNull
  @Column
  private String description;

  /**
   * min speed
   */
  @NotNull
  @Column
  private Integer min;

  /**
   * max speed
   */
  @NotNull
  @Column
  private Integer max;

  @NotNull
  @Column(nullable = false)
  @Convert(converter = StatusConverter.class)
  private StatusEnum status;

  /**
   * Auto-generated create date by Hibernate framework. <br> TODO can be separated using Spring data Auditable interface
   */
  @Column(nullable = false, updatable = false)
  @CreationTimestamp
  private Date createdDate;

  @Column
  @UpdateTimestamp
  private Date lastModifiedDate;
}
