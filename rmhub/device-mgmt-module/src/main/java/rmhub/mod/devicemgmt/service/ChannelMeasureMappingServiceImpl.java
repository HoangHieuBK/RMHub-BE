package rmhub.mod.devicemgmt.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmhub.mod.devicemgmt.common.StatusEnum;
import rmhub.mod.devicemgmt.entity.ChannelMesureMapping;
import rmhub.mod.devicemgmt.repository.ChannelMeasureMappingRepo;

@Service
class ChannelMeasureMappingServiceImpl implements ChannelMeasureMappingService {

  @Autowired
  private ChannelMeasureMappingRepo channelMeasureMappingRepo;

  @Override
  public ChannelMesureMapping create(ChannelMesureMapping channelMesureMapping) {

    if (channelMesureMapping != null && channelMesureMapping.getChannel() != null && channelMesureMapping.getMesureConfig() != null) {

      return channelMeasureMappingRepo.save(channelMesureMapping);
    }

    return null;
  }

  @Override
  public void deleteAll() {
    List<ChannelMesureMapping> channelMesureMappings = channelMeasureMappingRepo.findAll();

    for (ChannelMesureMapping channelMesureMapping: channelMesureMappings) {
      channelMesureMapping.setStatus(StatusEnum.DELETE.getValue());
    }

    channelMeasureMappingRepo.saveAll(channelMesureMappings);
  }

  @Override
  public ChannelMesureMapping find(Long channelId, Long mesureConfigId) {
    return channelMeasureMappingRepo.findByChannelIdAndMesureConfigId(channelId, mesureConfigId);
  }
}
