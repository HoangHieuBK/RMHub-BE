package rmhub.mod.devicemgmt.entity;

import java.io.Serializable;
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
@Table(name = "channel")
public class Channel implements Serializable {

  @Id
  @GeneratedValue
  @Column
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column
  private String value;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "poolId", nullable = false)
  private Pool pool;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @Builder.Default
  @OneToMany(mappedBy = "channel", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
  Set<ChannelPhysicalDeviceMapping> channelPhysicalDeviceMapping = new HashSet<>();

  @Column
  private Integer status;

  @Column
  private Long deviceTypeId;
}
