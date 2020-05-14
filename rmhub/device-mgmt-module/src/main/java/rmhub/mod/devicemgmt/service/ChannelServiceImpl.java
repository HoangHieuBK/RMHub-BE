package rmhub.mod.devicemgmt.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmhub.common.common.ErrorCode;
import rmhub.common.exception.BusinessException;
import rmhub.mod.devicemgmt.common.StatusEnum;
import rmhub.mod.devicemgmt.entity.Channel;
import rmhub.mod.devicemgmt.repository.ChannelRepo;

@Service
class ChannelServiceImpl implements ChannelService {

  @Autowired
  private ChannelRepo channelRepo;

  @Override
  public Channel create(Channel channel) {
    return channelRepo.save(channel);
  }

  @Override
  public Channel update(Channel channel) {
    return channelRepo.save(channel);
  }

  // FIXME should be deleted logically
  @Override
  public void delete(Long id) {
    channelRepo.deleteById(id);
  }

  @Override
  public void deleteAll() {
    List<Channel> channels = channelRepo.findAll();

    for (Channel channel : channels) {
      channel.setStatus(StatusEnum.DELETE.getValue());
    }

    channelRepo.saveAll(channels);
  }

  @Override
  public Channel findByValue(String value) {
    return channelRepo.findByValue(value);
  }

  @Override
  public Channel find(String value, Long deviceTypeId) {
    return channelRepo.findByValueAndDeviceTypeId(value, deviceTypeId);
  }

  @Override
  public List<Channel> findAll() {
    return channelRepo.findAll();
  }

  // FIXME check isPresent(), if not presented, throw NOT_FOUND exception
  @Override
  public Channel findById(Long id) {
    return channelRepo.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Channel doesn't exist."));
  }
}
