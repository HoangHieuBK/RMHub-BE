package rmhub.mod.devicemgmt.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rmhub.model.mivisu.api.Period;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mesure_config")
public class MesureConfig {

  @Id
  @GeneratedValue
  @Column
  private Long id;

  @Column
  private boolean isEnable;

  @Column
  private String mesureId;

  @Column
  private String mesureLibelle;

  @Column
  private String mesureType;

  @Column
  private String value;

  @Column
  private Long natureId;

  @Column
  private Long periodId;

  @Column
  private Integer status;

  public MesureConfig(Period itemPeriod, Long periodId, Long natureId, Integer status, boolean isEnable) {
    this.isEnable = isEnable;
    this.mesureId = itemPeriod.getClassMesure().getId_mesure().get_text();
    this.mesureLibelle = itemPeriod.getClassMesure().getMesureLibelle().get_text();
    this.mesureType = itemPeriod.getClassMesure().getMesureType().get_text();
    this.periodId = periodId;
    this.natureId = natureId;
    this.value = itemPeriod.getClassMesure().getValue();
    this.status = status;
  }
}
