package rmhub.mod.devicemgmt.consumer;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import rmhub.common.kafkaconnector.messagebased.KafkaProducible;
import rmhub.mod.devicemgmt.dto.PhysicalDeviceResponse;
import rmhub.mod.devicemgmt.entity.Channel;
import rmhub.mod.devicemgmt.entity.ChannelMesureMapping;
import rmhub.mod.devicemgmt.entity.ChannelPhysicalDeviceMapping;
import rmhub.mod.devicemgmt.entity.DeviceType;
import rmhub.mod.devicemgmt.entity.MesureConfig;
import rmhub.mod.devicemgmt.entity.Nature;
import rmhub.mod.devicemgmt.entity.Period;
import rmhub.mod.devicemgmt.entity.PhysicalDevice;
import rmhub.mod.devicemgmt.entity.Pool;
import rmhub.mod.devicemgmt.service.PhysicalDeviceService;
import rmhub.mod.devicemgmt.service.SyncDevicesService;

@Slf4j
@ExtendWith(SpringExtension.class)
class DeviceMgmtConsumerServiceTest {

  @InjectMocks
  private DeviceMgmtConsumerService deviceMgmtConsumerService;

  @Mock
  private SyncDevicesService syncDevicesService;

  @Mock
  private PhysicalDeviceService physicalDeviceService;

  @Mock
  private KafkaProducible<String, String> kafkaProducible;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(deviceMgmtConsumerService, "syncDevices", "sync-devices");
    ReflectionTestUtils.setField(deviceMgmtConsumerService, "requestDevice", "request.device");
  }

  @Test
  void testListenRequestSync() {

    deviceMgmtConsumerService.listenRequestSync("data test");

    verify(syncDevicesService, times(1)).resetCurrentData();
  }

  @Test
  void testListenConfigurationChange() {
    deviceMgmtConsumerService.listenConfigurationChange("test");

    verify(kafkaProducible, times(1)).send(Mockito.anyString(), Mockito.anyString());
  }

  @Test
  void testListenRequestWeather() throws Exception {

    File file = new ClassPathResource("dataTestWeather.json").getFile();

    BufferedReader br = new BufferedReader(new FileReader(file));

    String st;
    StringBuilder tmp = new StringBuilder();
    while ((st = br.readLine()) != null) {
      tmp.append(st);
    }

    PhysicalDeviceResponse physicalDeviceResponse = new PhysicalDeviceResponse();

    DeviceType deviceType = new DeviceType(1L, "WS");
    Nature nature = new Nature(1L, "tSR", "tSR", 1);
    Period period = new Period(1L, "_60", "_60", 1);

    when(physicalDeviceService.find(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Collections.singletonList(physicalDeviceResponse));
    when(syncDevicesService.getDeviceType(Mockito.anyLong())).thenReturn(deviceType);
    when(syncDevicesService.syncNature(Mockito.any(Nature.class))).thenReturn(nature);
    when(syncDevicesService.syncPeriod(Mockito.any(Period.class))).thenReturn(period);

    deviceMgmtConsumerService.listenRequestSyncWeather(tmp.toString());

    verify(syncDevicesService, times(1)).resetPhysicalDeviceData(Mockito.anyLong());
    verify(syncDevicesService, atLeastOnce()).getDeviceType(Mockito.anyLong());
    verify(syncDevicesService, atLeastOnce()).syncPhysicalDevice(Mockito.any(PhysicalDevice.class));
    verify(syncDevicesService, atLeastOnce()).syncPool(Mockito.any(Pool.class));
    verify(syncDevicesService, atLeastOnce()).syncChannel(Mockito.any(Channel.class));
    verify(syncDevicesService, atLeastOnce()).syncChannelPhysicalDeviceMapping(Mockito.any(ChannelPhysicalDeviceMapping.class));
    verify(syncDevicesService, atLeastOnce()).syncNature(Mockito.any(Nature.class));
    verify(syncDevicesService, atLeastOnce()).syncPeriod(Mockito.any(Period.class));
    verify(syncDevicesService, atLeastOnce()).syncMesureConfig(Mockito.any(MesureConfig.class));
    verify(syncDevicesService, atLeastOnce()).syncChannelMesureMapping(Mockito.any(ChannelMesureMapping.class));
  }

  @Test
  void testListenRequestTraffic() throws Exception {

    SyncDevicesService.NEW_DEVICES.add("TC_40_400");

    File file = new ClassPathResource("dataTestTraffic.json").getFile();

    BufferedReader br = new BufferedReader(new FileReader(file));

    String st;
    StringBuilder tmp = new StringBuilder();
    while ((st = br.readLine()) != null) {
      tmp.append(st);
    }

    PhysicalDeviceResponse physicalDeviceResponse = new PhysicalDeviceResponse();

    DeviceType deviceType = new DeviceType(1L, "WS");
    Nature nature = new Nature(1L, "tSR", "tSR", 1);
    Period period = new Period(1L, "_60", "_60", 1);

    when(physicalDeviceService.find(Mockito.anyLong(), Mockito.anyLong())).thenReturn(Collections.singletonList(physicalDeviceResponse));
    when(syncDevicesService.getDeviceType(Mockito.anyLong())).thenReturn(deviceType);
    when(syncDevicesService.syncNature(Mockito.any(Nature.class))).thenReturn(nature);
    when(syncDevicesService.syncPeriod(Mockito.any(Period.class))).thenReturn(period);

    deviceMgmtConsumerService.listenRequestSyncTraffic(tmp.toString());

    verify(syncDevicesService, times(1)).resetPhysicalDeviceData(Mockito.anyLong());
    verify(syncDevicesService, atLeastOnce()).getDeviceType(Mockito.anyLong());
    verify(syncDevicesService, atLeastOnce()).syncPhysicalDevice(Mockito.any(PhysicalDevice.class));
    verify(syncDevicesService, atLeastOnce()).syncPool(Mockito.any(Pool.class));
    verify(syncDevicesService, atLeastOnce()).syncChannel(Mockito.any(Channel.class));
    verify(syncDevicesService, atLeastOnce()).syncChannelPhysicalDeviceMapping(Mockito.any(ChannelPhysicalDeviceMapping.class));
    verify(syncDevicesService, atLeastOnce()).syncNature(Mockito.any(Nature.class));
    verify(syncDevicesService, atLeastOnce()).syncPeriod(Mockito.any(Period.class));
    verify(syncDevicesService, atLeastOnce()).syncMesureConfig(Mockito.any(MesureConfig.class));
    verify(syncDevicesService, atLeastOnce()).syncChannelMesureMapping(Mockito.any(ChannelMesureMapping.class));

    SyncDevicesService.NEW_DEVICES.clear();
  }

  @Test
  void testSyncDeviceTraffic_infoPhysicNull() throws Exception {

    File file = new ClassPathResource("dataTestInfoPhysical_null.json").getFile();

    BufferedReader br = new BufferedReader(new FileReader(file));

    String st;
    StringBuilder tmp = new StringBuilder();
    while ((st = br.readLine()) != null) {
      tmp.append(st);
    }

    deviceMgmtConsumerService.listenRequestSyncTraffic(tmp.toString());

    verify(syncDevicesService, times(1)).resetPhysicalDeviceData(Mockito.anyLong());
  }

  @Test
  void testSyncDeviceWeather_infoPhysicNull() throws Exception {

    File file = new ClassPathResource("dataTestInfoPhysical_null.json").getFile();

    BufferedReader br = new BufferedReader(new FileReader(file));

    String st;
    StringBuilder tmp = new StringBuilder();
    while ((st = br.readLine()) != null) {
      tmp.append(st);
    }

    deviceMgmtConsumerService.listenRequestSyncWeather(tmp.toString());

    verify(syncDevicesService, times(1)).resetPhysicalDeviceData(Mockito.anyLong());
  }

  @Test
  void testSyncDevice_infoGenericsNull() throws Exception {

    File file = new ClassPathResource("dataTestInfoGenerics_null.json").getFile();

    BufferedReader br = new BufferedReader(new FileReader(file));

    String st;
    StringBuilder tmp = new StringBuilder();
    while ((st = br.readLine()) != null) {
      tmp.append(st);
    }

    deviceMgmtConsumerService.listenRequestSyncTraffic(tmp.toString());

    verify(syncDevicesService, times(1)).resetPhysicalDeviceData(Mockito.anyLong());
  }

  @Test
  void testSyncDevice_infoGenericsEmpty() throws Exception {

    File file = new ClassPathResource("dataTestInfoGenerics_empty.json").getFile();

    BufferedReader br = new BufferedReader(new FileReader(file));

    String st;
    StringBuilder tmp = new StringBuilder();
    while ((st = br.readLine()) != null) {
      tmp.append(st);
    }

    deviceMgmtConsumerService.listenRequestSyncTraffic(tmp.toString());

    verify(syncDevicesService, times(1)).resetPhysicalDeviceData(Mockito.anyLong());
  }

  @Test
  void testSyncDevice_cfgNull() throws Exception {

    File file = new ClassPathResource("dataTestCfg_null.json").getFile();

    BufferedReader br = new BufferedReader(new FileReader(file));

    String st;
    StringBuilder tmp = new StringBuilder();
    while ((st = br.readLine()) != null) {
      tmp.append(st);
    }

    deviceMgmtConsumerService.listenRequestSyncTraffic(tmp.toString());

    verify(syncDevicesService, times(1)).resetPhysicalDeviceData(Mockito.anyLong());
  }

  @Test
  void testSyncDeviceTraffic_Null() throws Exception {

    deviceMgmtConsumerService.listenRequestSyncTraffic("null");

    verify(syncDevicesService, times(1)).resetPhysicalDeviceData(Mockito.anyLong());
  }

  @Test
  void testSyncDeviceWeather_Null() throws Exception {

    deviceMgmtConsumerService.listenRequestSyncWeather("null");

    verify(syncDevicesService, times(1)).resetPhysicalDeviceData(Mockito.anyLong());
  }
}
