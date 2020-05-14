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
@Table(name = "period")
public class Period {

  @Id
  @GeneratedValue
  @Column
  private Long id;

  @Column
  private String name;

  @Column
  private String value;

  @Column
  private Integer status;

  public Period(String name, String value, Integer status) {
    this.name = name;
    this.value = value;
    this.status = status;
  }
}
