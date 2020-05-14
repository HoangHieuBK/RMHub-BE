package rmhub.mod.devicemgmt.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.util.StringUtils;
import rmhub.mod.devicemgmt.common.PhysicalDeviceConst;

@Builder
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "physical_device")
public class PhysicalDevice {

  @Id
  @GeneratedValue
  @Column
  private Long id;

  @Column
  private Integer atKilometers;

  @Column
  private Integer atMeters;

  @Column
  private Long cmcDeviceId;

  @Column
  private Long connectionInfoId;

  @Column(name = PhysicalDeviceConst.DEPLOYMENT_ID)
  private Long deploymentId;

  @Column
  private String description;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne
  @JoinColumn(name = "deviceTypeId")
  private DeviceType deviceType;

  @NotBlank
  @Column(nullable = false, unique = true)
  private String externalId;

  @Column
  private Double latitude;

  @Column
  private Double longitude;

  @Column(name = PhysicalDeviceConst.NAME)
  private String name;

  @Column
  private Long mappingInfoId;

  @Column(name = PhysicalDeviceConst.STATUS)
  private Integer status;

  // TODO will be @LastModifiedBy after integrate spring-security
  @Column
  private Long lastModifiedBy;

  @UpdateTimestamp
  @Column(name = PhysicalDeviceConst.LAST_MODIFIED_DATE)
  private Date lastModifiedDate;

  @Column
  private Date installedDate;

  @Builder.Default
  @Column(name = PhysicalDeviceConst.IS_REGISTERED)
  private Boolean isRegistered = false;

  @Column
  private String context;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @Builder.Default
  @OneToMany(mappedBy = "physicalDevice", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<ChannelPhysicalDeviceMapping> channelPhysicalDeviceMapping = new HashSet<>();

  public Integer extractKM() {
    if (StringUtils.isEmpty(externalId)) {
      return null;
    }

    String[] data = externalId.split("_");

    if (data.length < 2) {
      return null;
    }

    return Integer.valueOf(data[1]);
  }

  public Integer extractMeter() {
    if (StringUtils.isEmpty(externalId)) {
      return null;
    }

    String[] data = externalId.split("_");

    if (data.length < 3) {
      return null;
    }

    return Integer.valueOf(data[2]);
  }
}
