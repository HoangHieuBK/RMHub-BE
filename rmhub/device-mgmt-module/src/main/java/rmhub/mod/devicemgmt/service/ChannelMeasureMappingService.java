package rmhub.mod.devicemgmt.service;

import rmhub.mod.devicemgmt.entity.ChannelMesureMapping;

public interface ChannelMeasureMappingService {

  ChannelMesureMapping create(ChannelMesureMapping channelMesureMapping);

  void deleteAll();

  ChannelMesureMapping find(Long channelId, Long mesureConfigId);
}
