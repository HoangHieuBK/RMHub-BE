package rmhub.mod.devicemgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rmhub.mod.devicemgmt.entity.Channel;

public interface ChannelRepo extends JpaRepository<Channel, Long> {

  Channel findByValue(String value);

  Channel findByValueAndDeviceTypeId(String value, Long deviceTypeId);
}
