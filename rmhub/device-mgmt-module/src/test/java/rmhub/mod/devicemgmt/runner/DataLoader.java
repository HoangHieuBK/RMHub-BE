package rmhub.mod.devicemgmt.runner;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rmhub.mod.devicemgmt.entity.DeviceType;
import rmhub.mod.devicemgmt.repository.DeviceTypeRepo;

@Slf4j
@Component
public class DataLoader implements CommandLineRunner {

  private final DeviceTypeRepo deviceTypeRepo;

  @Autowired
  public DataLoader(DeviceTypeRepo repository) {
    this.deviceTypeRepo = repository;
  }

  @Override
  public void run(String... args) {

    List<DeviceType> deviceTypes = new ArrayList<>();

    DeviceType deviceType = new DeviceType(1L, "WS");
    DeviceType deviceType1 = new DeviceType(2L, "TC");

    deviceTypes.add(deviceType);
    deviceTypes.add(deviceType1);

    log.info("loaded: {}", deviceTypeRepo.saveAll(deviceTypes));
  }
}
