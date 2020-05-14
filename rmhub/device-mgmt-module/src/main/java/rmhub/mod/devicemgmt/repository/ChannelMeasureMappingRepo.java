package rmhub.mod.devicemgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rmhub.mod.devicemgmt.entity.ChannelMesureMapping;

public interface ChannelMeasureMappingRepo extends JpaRepository<ChannelMesureMapping, Long> {

  ChannelMesureMapping findByChannelIdAndMesureConfigId(Long channelId, Long mesureConfigId);
}
