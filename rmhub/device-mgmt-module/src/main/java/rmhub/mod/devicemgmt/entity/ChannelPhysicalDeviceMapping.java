package rmhub.mod.devicemgmt.entity;

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
@Table(name = "channel_physical_device_mapping")
public class ChannelPhysicalDeviceMapping {

  @Id
  @GeneratedValue
  @Column
  private Long id;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.EAGER, optional = false, targetEntity = Channel.class)
  @JoinColumn(name = "channel_id", nullable = false)
  Channel channel;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.EAGER, optional = false, targetEntity = PhysicalDevice.class)
  @JoinColumn(name = "physical_device_id", nullable = false)
  PhysicalDevice physicalDevice;

  @Column
  private Integer status;
}
