package rmhub.mod.devicemgmt.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import rmhub.common.kafkaconnector.configs.KafkaConsumerConfig;
import rmhub.common.kafkaconnector.configs.KafkaProducerConfig;
import rmhub.common.kafkaconnector.messagebased.KafkaProducerBase;
import rmhub.mod.devicemgmt.dto.PhysicalDeviceResponse;
import rmhub.mod.devicemgmt.entity.DeviceType;
import rmhub.mod.devicemgmt.entity.EntityPackageMarker;
import rmhub.mod.devicemgmt.entity.PhysicalDevice;
import rmhub.mod.devicemgmt.repository.RepoPackageMarker;
import rmhub.mod.devicemgmt.runner.DataLoader;

@DataJpaTest
@ComponentScan(basePackageClasses = {DataLoader.class, ServicePackageMarker.class})
@EntityScan(basePackageClasses = EntityPackageMarker.class)
@EnableJpaRepositories(basePackageClasses = RepoPackageMarker.class)
class PhysicalDeviceServiceIT {

  @Configuration
  @Import({KafkaConsumerConfig.class, KafkaProducerConfig.class})
  static class TestConfig {
    @Bean
    public KafkaProducerBase<String, String> kafkaProducerBase() {
      return new KafkaProducerBase<>();
    }
  }

  private DeviceType weather = new DeviceType(1L, "WS");
  private DeviceType traffic = new DeviceType(2L, "TC");

  @Autowired
  private PhysicalDeviceService physicalDeviceService;

  @BeforeEach
  void setupBefore() {

    PhysicalDevice data = new PhysicalDevice();
    data.setExternalId("WS_83_000");
    data.setDeploymentId(1L);
    data.setDeviceType(weather);
    data.setName("WS_83_000");
    data.setStatus(1);
    data.setIsRegistered(true);

    PhysicalDevice data1 = new PhysicalDevice();
    data1.setExternalId("WS_92_841");
    data1.setDeploymentId(1L);
    data1.setDeviceType(weather);
    data1.setName("WS_81_000");
    data1.setStatus(0);
    data1.setIsRegistered(false);

    PhysicalDevice data2 = new PhysicalDevice();
    data2.setExternalId("WS_93_845");
    data2.setDeploymentId(1L);
    data2.setDeviceType(weather);
    data2.setName("WS_82_000");
    data2.setStatus(3);
    data2.setIsRegistered(false);

    PhysicalDevice data3 = new PhysicalDevice();
    data3.setExternalId("WS_92_842");
    data3.setDeploymentId(2L);
    data3.setDeviceType(weather);
    data3.setName("WS_81_000");
    data3.setStatus(0);
    data3.setIsRegistered(false);

    PhysicalDevice data4 = new PhysicalDevice();
    data4.setExternalId("WS_92_843");
    data4.setDeploymentId(2L);
    data4.setDeviceType(traffic);
    data4.setName("WS_81_000");
    data4.setStatus(0);
    data4.setIsRegistered(false);

    PhysicalDevice data5 = new PhysicalDevice();
    data5.setExternalId("TC_81_000");
    data5.setDeploymentId(1L);
    data5.setDeviceType(weather);
    data5.setName("TC_81_000");
    data5.setStatus(0);
    data5.setIsRegistered(false);

    physicalDeviceService.create(data);
    physicalDeviceService.create(data1);
    physicalDeviceService.create(data2);
    physicalDeviceService.create(data3);
    physicalDeviceService.create(data4);
    physicalDeviceService.create(data5);
  }

  @Test
  void testFindByName() {

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

    List<PhysicalDeviceResponse> actual = physicalDeviceService.findByName(1L, 1L, "ws");

    assertEquals(2, actual.size());
    assertEquals(data1Excepted.getName(), actual.get(0).getName());
    assertEquals(dataExcepted.getName(), actual.get(1).getName());
  }
}
