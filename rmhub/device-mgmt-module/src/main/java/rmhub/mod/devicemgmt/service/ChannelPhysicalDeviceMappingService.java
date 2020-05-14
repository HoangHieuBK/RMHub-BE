package rmhub.mod.devicemgmt.service;

import rmhub.mod.devicemgmt.entity.ChannelPhysicalDeviceMapping;

public interface ChannelPhysicalDeviceMappingService {

  ChannelPhysicalDeviceMapping create(ChannelPhysicalDeviceMapping channelPhysicalDeviceMapping);

  void deleteAll();

  ChannelPhysicalDeviceMapping find(Long channelId, Long physicalDeviceId);
}
