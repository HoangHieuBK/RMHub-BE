package rmhub.mod.devicemgmt.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import rmhub.mod.devicemgmt.common.PhysicalDeviceConst;
import rmhub.mod.devicemgmt.entity.DeviceType;
import rmhub.mod.devicemgmt.entity.EntityPackageMarker;
import rmhub.mod.devicemgmt.entity.PhysicalDevice;
import rmhub.mod.devicemgmt.repository.specification.PhysicalDeviceSpecification;
import rmhub.mod.devicemgmt.runner.DataLoader;

@DataJpaTest
@EntityScan(basePackageClasses = EntityPackageMarker.class)
@EnableJpaRepositories(basePackageClasses = RepoPackageMarker.class)
@ComponentScan(basePackageClasses = DataLoader.class)
class PhysicalDeviceRepoIT {

  @Autowired
  private PhysicalDeviceRepo physicalDeviceRepo;

  @Test
  void testSpecification_sortByName() {

    DeviceType weather = new DeviceType(1L, "WS");

    PhysicalDevice data = new PhysicalDevice();
    data.setExternalId("WS_83_000");
    data.setDeploymentId(1L);
    data.setDeviceType(weather);
    data.setName("WS_83_000");
    data.setStatus(1);
    data.setIsRegistered(true);

    PhysicalDevice data1 = new PhysicalDevice();
    data1.setExternalId("WS_92_845");
    data1.setDeploymentId(1L);
    data1.setDeviceType(weather);
    data1.setName("WS_81_000");
    data1.setStatus(0);
    data1.setIsRegistered(false);

    physicalDeviceRepo.save(data);
    physicalDeviceRepo.save(data1);

    PhysicalDevice dataExcepted = new PhysicalDevice();
    dataExcepted.setExternalId("WS_83_000");
    dataExcepted.setDeploymentId(1L);
    dataExcepted.setDeviceType(weather);
    dataExcepted.setName("WS_83_000");
    dataExcepted.setStatus(1);
    dataExcepted.setIsRegistered(true);

    PhysicalDevice data1Excepted = new PhysicalDevice();
    data1Excepted.setExternalId("WS_92_845");
    data1Excepted.setDeploymentId(1L);
    data1Excepted.setDeviceType(weather);
    data1Excepted.setName("WS_81_000");
    data1Excepted.setStatus(0);
    data1Excepted.setIsRegistered(false);

    Sort sort = Sort.by(Sort.Order.asc(PhysicalDeviceConst.NAME),
        Sort.Order.desc(PhysicalDeviceConst.IS_REGISTERED), Sort.Order.desc(PhysicalDeviceConst.STATUS));

    List<PhysicalDevice> actual = physicalDeviceRepo
        .findAll(PhysicalDeviceSpecification.getAllDevice(1L, 1L, PhysicalDeviceConst.VALID_STATUS), sort);

    assertEquals(data1Excepted.getName(), actual.get(0).getName());
    assertEquals(dataExcepted.getName(), actual.get(1).getName());
  }

  @Test
  void testSpecification_sortByIsRegistered() {

    DeviceType weather = new DeviceType(1L, "WS");

    PhysicalDevice data = new PhysicalDevice();
    data.setExternalId("WS_83_000");
    data.setDeploymentId(1L);
    data.setDeviceType(weather);
    data.setName("WS_83_000");
    data.setStatus(1);
    data.setIsRegistered(true);

    PhysicalDevice data1 = new PhysicalDevice();
    data1.setExternalId("WS_92_845");
    data1.setDeploymentId(1L);
    data1.setDeviceType(weather);
    data1.setName("WS_83_000");
    data1.setStatus(0);
    data1.setIsRegistered(false);

    physicalDeviceRepo.save(data);
    physicalDeviceRepo.save(data1);

    PhysicalDevice dataExcepted = new PhysicalDevice();
    dataExcepted.setExternalId("WS_83_000");
    dataExcepted.setDeploymentId(1L);
    dataExcepted.setDeviceType(weather);
    dataExcepted.setName("WS_83_000");
    dataExcepted.setStatus(1);
    dataExcepted.setIsRegistered(true);

    PhysicalDevice data1Excepted = new PhysicalDevice();
    data1Excepted.setExternalId("WS_92_845");
    data1Excepted.setDeploymentId(1L);
    data1Excepted.setDeviceType(weather);
    data1Excepted.setName("WS_83_000");
    data1Excepted.setStatus(0);
    data1Excepted.setIsRegistered(false);

    Sort sort = Sort.by(Sort.Order.asc(PhysicalDeviceConst.NAME),
        Sort.Order.desc(PhysicalDeviceConst.IS_REGISTERED), Sort.Order.desc(PhysicalDeviceConst.STATUS));

    List<PhysicalDevice> actual = physicalDeviceRepo
        .findAll(PhysicalDeviceSpecification.getAllDevice(1L, 1L, PhysicalDeviceConst.VALID_STATUS), sort);

    assertEquals(data1Excepted.getIsRegistered(), actual.get(1).getIsRegistered());
    assertEquals(dataExcepted.getIsRegistered(), actual.get(0).getIsRegistered());
  }

  @Test
  void testSpecification_sortByStatus() {

    DeviceType weather = new DeviceType(1L, "WS");

    PhysicalDevice data = new PhysicalDevice();
    data.setExternalId("WS_83_000");
    data.setDeploymentId(1L);
    data.setDeviceType(weather);
    data.setName("WS_83_000");
    data.setStatus(1);
    data.setIsRegistered(true);

    PhysicalDevice data1 = new PhysicalDevice();
    data1.setExternalId("WS_92_845");
    data1.setDeploymentId(1L);
    data1.setDeviceType(weather);
    data1.setName("WS_83_000");
    data1.setStatus(0);
    data1.setIsRegistered(true);

    physicalDeviceRepo.save(data);
    physicalDeviceRepo.save(data1);

    PhysicalDevice dataExcepted = new PhysicalDevice();
    dataExcepted.setExternalId("WS_83_000");
    dataExcepted.setDeploymentId(1L);
    dataExcepted.setDeviceType(weather);
    dataExcepted.setName("WS_83_000");
    dataExcepted.setStatus(1);
    dataExcepted.setIsRegistered(true);

    PhysicalDevice data1Excepted = new PhysicalDevice();
    data1Excepted.setExternalId("WS_92_845");
    data1Excepted.setDeploymentId(1L);
    data1Excepted.setDeviceType(weather);
    data1Excepted.setName("WS_83_000");
    data1Excepted.setStatus(0);
    data1Excepted.setIsRegistered(true);

    Sort sort = Sort.by(Sort.Order.asc(PhysicalDeviceConst.NAME),
        Sort.Order.desc(PhysicalDeviceConst.IS_REGISTERED), Sort.Order.desc(PhysicalDeviceConst.STATUS));

    List<PhysicalDevice> actual = physicalDeviceRepo
        .findAll(PhysicalDeviceSpecification.getAllDevice(1L, 1L, PhysicalDeviceConst.VALID_STATUS), sort);

    assertEquals(data1Excepted.getExternalId(), actual.get(1).getExternalId());
    assertEquals(dataExcepted.getExternalId(), actual.get(0).getExternalId());
  }
}
