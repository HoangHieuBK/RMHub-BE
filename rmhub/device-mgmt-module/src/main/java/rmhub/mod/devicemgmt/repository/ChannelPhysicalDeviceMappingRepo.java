package rmhub.mod.devicemgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rmhub.mod.devicemgmt.entity.ChannelPhysicalDeviceMapping;

public interface ChannelPhysicalDeviceMappingRepo extends JpaRepository<ChannelPhysicalDeviceMapping, Long> {

  ChannelPhysicalDeviceMapping findByChannelIdAndPhysicalDeviceId(Long channelId, Long physicalDeviceId);
}
