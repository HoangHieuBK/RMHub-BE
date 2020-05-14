package rmhub.mod.devicemgmt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "device_type")
public class DeviceType {

  public DeviceType(Long id) {
    this.id = id;
  }

  @Id
  @GeneratedValue
  @Column(insertable = false, updatable = false)
  Long id;

  @Column
  String name;
}
