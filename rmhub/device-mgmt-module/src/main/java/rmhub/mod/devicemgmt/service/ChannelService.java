package rmhub.mod.devicemgmt.service;

import java.util.List;
import rmhub.mod.devicemgmt.entity.Channel;

public interface ChannelService {

  Channel create(Channel channel);

  Channel update(Channel channel);

  void delete(Long id);

  void deleteAll();

  Channel findByValue(String value);

  Channel find(String value, Long deviceTypeId);

  List<Channel> findAll();

  Channel findById(Long id);
}
