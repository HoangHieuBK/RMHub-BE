package rmhub.mod.devicemgmt.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmhub.mod.devicemgmt.common.StatusEnum;
import rmhub.mod.devicemgmt.entity.ChannelPhysicalDeviceMapping;
import rmhub.mod.devicemgmt.repository.ChannelPhysicalDeviceMappingRepo;

@Service
class ChannelPhysicalDeviceMappingServiceImpl implements ChannelPhysicalDeviceMappingService {

  @Autowired
  private ChannelPhysicalDeviceMappingRepo channelPhysicalDeviceMappingRepo;

  @Override
  public ChannelPhysicalDeviceMapping create(ChannelPhysicalDeviceMapping channelPhysicalDeviceMapping) {
    return channelPhysicalDeviceMappingRepo.save(channelPhysicalDeviceMapping);
  }

  @Override
  public void deleteAll() {
    List<ChannelPhysicalDeviceMapping> channelPhysicalDeviceMappings = channelPhysicalDeviceMappingRepo.findAll();

    for (ChannelPhysicalDeviceMapping channelPhysicalDeviceMapping : channelPhysicalDeviceMappings) {
      channelPhysicalDeviceMapping.setStatus(StatusEnum.DELETE.getValue());
    }

    channelPhysicalDeviceMappingRepo.saveAll(channelPhysicalDeviceMappings);
  }

  @Override
  public ChannelPhysicalDeviceMapping find(Long channelId, Long physicalDeviceId) {
    return channelPhysicalDeviceMappingRepo.findByChannelIdAndPhysicalDeviceId(channelId, physicalDeviceId);
  }
}
