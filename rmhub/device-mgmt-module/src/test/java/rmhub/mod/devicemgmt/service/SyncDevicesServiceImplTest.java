package rmhub.mod.devicemgmt.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rmhub.common.common.ErrorCode;
import rmhub.common.exception.BusinessException;
import rmhub.mod.devicemgmt.common.StatusEnum;
import rmhub.mod.devicemgmt.entity.Channel;
import rmhub.mod.devicemgmt.entity.ChannelMesureMapping;
import rmhub.mod.devicemgmt.entity.ChannelPhysicalDeviceMapping;
import rmhub.mod.devicemgmt.entity.DeviceType;
import rmhub.mod.devicemgmt.entity.MesureConfig;
import rmhub.mod.devicemgmt.entity.Nature;
import rmhub.mod.devicemgmt.entity.Period;
import rmhub.mod.devicemgmt.entity.PhysicalDevice;
import rmhub.mod.devicemgmt.entity.Pool;
import rmhub.mod.devicemgmt.repository.DeviceTypeRepo;

@Slf4j
@ExtendWith(SpringExtension.class)
class SyncDevicesServiceImplTest {

  @InjectMocks
  private SyncDevicesServiceImpl syncDevicesService;

  @Mock
  private PhysicalDeviceService physicalDeviceService;

  @Mock
  private DeviceTypeRepo deviceTypeRepo;

  @Mock
  private PoolService poolService;

  @Mock
  private ChannelService channelService;

  @Mock
  private NatureService natureService;

  @Mock
  private PeriodService periodService;

  @Mock
  private ChannelPhysicalDeviceMappingService channelPhysicalDeviceMappingService;

  @Mock
  private MesureConfigsService mesureConfigsService;

  @Mock
  private ChannelMeasureMappingService channelMeasureMappingService;

  @Test
  void testGetDeviceType() {

    DeviceType expected = new DeviceType();
    expected.setId(1L);
    expected.setName("WS");

    when(deviceTypeRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(expected));

    DeviceType actual = syncDevicesService.getDeviceType(1L);

    assertEquals(expected, actual);
  }

  @Test
  void testGetDeviceType_throwException() {

    DeviceType expected = new DeviceType();
    expected.setId(1L);
    expected.setName("WS");

    when(deviceTypeRepo.findById(Mockito.anyLong())).thenThrow(new BusinessException(ErrorCode.NOT_FOUND, "DeviceType doesn't exist."));

    assertThrows(BusinessException.class, () -> syncDevicesService.getDeviceType(1L), "DeviceType doesn't exist.");
  }

  @Test
  void testResetCurrentData() {
    syncDevicesService.resetCurrentData();

    verify(poolService, times(1)).deleteAll();
    verify(channelService, times(1)).deleteAll();
    verify(natureService, times(1)).deleteAll();
    verify(periodService, times(1)).deleteAll();
    verify(channelPhysicalDeviceMappingService, times(1)).deleteAll();
    verify(mesureConfigsService, times(1)).deleteAll();
    verify(channelMeasureMappingService, times(1)).deleteAll();
  }

  @Test
  void testResetPhysicalDeviceData() {
    syncDevicesService.resetPhysicalDeviceData(1L);

    verify(physicalDeviceService, times(1)).deleteAll(Mockito.anyLong());
  }

  @Test
  void testSyncPhysicalDevice_notNull() {
    PhysicalDevice expected = new PhysicalDevice();
    expected.setId(1L);
    expected.setName("TC_40_400");
    expected.setExternalId("TC_40_400");
    expected.setStatus(StatusEnum.ACTIVE.getValue());

    when(physicalDeviceService.findByExternalId(Mockito.anyString())).thenReturn(expected);
    when(physicalDeviceService.createOrUpdate(Mockito.any(PhysicalDevice.class))).thenReturn(expected);

    PhysicalDevice actual = syncDevicesService.syncPhysicalDevice(expected);

    assertEquals(expected, actual);
  }

  @Test
  void testSyncPhysicalDevice_null() {
    PhysicalDevice expected = new PhysicalDevice();
    expected.setId(1L);
    expected.setName("TC_40_400");
    expected.setExternalId("TC_40_400");
    expected.setStatus(StatusEnum.ACTIVE.getValue());

    when(physicalDeviceService.createOrUpdate(Mockito.any(PhysicalDevice.class))).thenReturn(expected);

    PhysicalDevice actual = syncDevicesService.syncPhysicalDevice(expected);

    assertEquals(expected, actual);
  }

  @Test
  void testSyncPool() {
    Pool expected = new Pool();
    expected.setId(1L);
    expected.setName("Direction_1_WE");
    expected.setValue("_1");

    when(poolService.findByValue(Mockito.anyString())).thenReturn(expected);
    when(poolService.create(Mockito.any(Pool.class))).thenReturn(expected);

    Pool actual = syncDevicesService.syncPool(expected);

    assertEquals(expected, actual);
  }

  @Test
  void testSyncPool_null() {
    Pool expected = new Pool();
    expected.setId(1L);
    expected.setName("Direction_1_WE");
    expected.setValue("_1");

    when(poolService.findByValue(Mockito.anyString())).thenReturn(null);
    when(poolService.create(Mockito.any(Pool.class))).thenReturn(expected);

    Pool actual = syncDevicesService.syncPool(expected);

    assertEquals(expected, actual);
  }

  @Test
  void testSyncPool_nullPoolName() {
    Pool expected = new Pool();
    expected.setId(1L);
    expected.setName(null);
    expected.setValue("_1");

    when(poolService.findByValue(Mockito.anyString())).thenReturn(expected);
    when(poolService.create(Mockito.any(Pool.class))).thenReturn(expected);

    Pool actual = syncDevicesService.syncPool(expected);

    assertEquals(expected, actual);
  }

  @Test
  void testSyncChannel() {
    Channel expected = new Channel();
    expected.setId(1L);
    expected.setName("Channel 1");
    expected.setValue("_1");
    expected.setDeviceTypeId(1L);

    when(channelService.find(Mockito.anyString(), Mockito.anyLong())).thenReturn(expected);
    when(channelService.create(Mockito.any(Channel.class))).thenReturn(expected);

    Channel actual = syncDevicesService.syncChannel(expected);

    assertEquals(expected, actual);
  }

  @Test
  void testSyncChannel_null() {
    Channel expected = new Channel();
    expected.setId(1L);
    expected.setName("Channel 1");
    expected.setValue("_1");
    expected.setDeviceTypeId(1L);

    when(channelService.find(Mockito.anyString(), Mockito.anyLong())).thenReturn(null);
    when(channelService.create(Mockito.any(Channel.class))).thenReturn(expected);

    Channel actual = syncDevicesService.syncChannel(expected);

    assertEquals(expected, actual);
  }

  @Test
  void testSyncChannelPhysicalDeviceMapping() {
    Channel channel = new Channel();
    channel.setId(1L);
    channel.setName("Channel 1");
    channel.setValue("_1");

    PhysicalDevice physicalDevice = new PhysicalDevice();
    physicalDevice.setId(1L);
    physicalDevice.setName("TC_40_400");
    physicalDevice.setExternalId("TC_40_400");
    physicalDevice.setStatus(StatusEnum.ACTIVE.getValue());

    ChannelPhysicalDeviceMapping expected = new ChannelPhysicalDeviceMapping();
    expected.setId(1L);
    expected.setChannel(channel);
    expected.setPhysicalDevice(physicalDevice);

    when(channelPhysicalDeviceMappingService.find(Mockito.anyLong(), Mockito.anyLong())).thenReturn(expected);
    when(channelPhysicalDeviceMappingService.create(Mockito.any(ChannelPhysicalDeviceMapping.class))).thenReturn(expected);

    ChannelPhysicalDeviceMapping actual = syncDevicesService.syncChannelPhysicalDeviceMapping(expected);

    assertEquals(expected, actual);
  }

  @Test
  void testSyncChannelPhysicalDeviceMapping_null() {
    Channel channel = new Channel();
    channel.setId(1L);
    channel.setName("Channel 1");
    channel.setValue("_1");

    PhysicalDevice physicalDevice = new PhysicalDevice();
    physicalDevice.setId(1L);
    physicalDevice.setName("TC_40_400");
    physicalDevice.setExternalId("TC_40_400");
    physicalDevice.setStatus(StatusEnum.ACTIVE.getValue());

    ChannelPhysicalDeviceMapping expected = new ChannelPhysicalDeviceMapping();
    expected.setId(1L);
    expected.setChannel(channel);
    expected.setPhysicalDevice(physicalDevice);

    when(channelPhysicalDeviceMappingService.find(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null);
    when(channelPhysicalDeviceMappingService.create(Mockito.any(ChannelPhysicalDeviceMapping.class))).thenReturn(expected);

    ChannelPhysicalDeviceMapping actual = syncDevicesService.syncChannelPhysicalDeviceMapping(expected);

    assertEquals(expected, actual);
  }

  @Test
  void testSyncChannelMesureMapping() {
    Channel channel = new Channel();
    channel.setId(1L);
    channel.setName("Channel 1");
    channel.setValue("_1");

    MesureConfig mesureConfig = new MesureConfig();
    mesureConfig.setId(1L);
    mesureConfig.setMesureId("2/z/uAM/360/Am00");
    mesureConfig.setMesureType("1HG_MP_E0");
    mesureConfig.setNatureId(25L);
    mesureConfig.setPeriodId(25L);
    mesureConfig.setValue("Am00");

    ChannelMesureMapping expected = new ChannelMesureMapping();
    expected.setId(1L);
    expected.setChannel(channel);
    expected.setMesureConfig(mesureConfig);

    when(channelMeasureMappingService.find(Mockito.anyLong(), Mockito.anyLong())).thenReturn(expected);
    when(channelMeasureMappingService.create(Mockito.any(ChannelMesureMapping.class))).thenReturn(expected);

    ChannelMesureMapping actual = syncDevicesService.syncChannelMesureMapping(expected);

    assertEquals(expected, actual);
  }

  @Test
  void testSyncChannelMesureMapping_null() {
    Channel channel = new Channel();
    channel.setId(1L);
    channel.setName("Channel 1");
    channel.setValue("_1");

    MesureConfig mesureConfig = new MesureConfig();
    mesureConfig.setId(1L);
    mesureConfig.setMesureId("2/z/uAM/360/Am00");
    mesureConfig.setMesureType("1HG_MP_E0");
    mesureConfig.setNatureId(25L);
    mesureConfig.setPeriodId(25L);
    mesureConfig.setValue("Am00");

    ChannelMesureMapping expected = new ChannelMesureMapping();
    expected.setId(1L);
    expected.setChannel(channel);
    expected.setMesureConfig(mesureConfig);

    when(channelMeasureMappingService.find(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null);
    when(channelMeasureMappingService.create(Mockito.any(ChannelMesureMapping.class))).thenReturn(expected);

    ChannelMesureMapping actual = syncDevicesService.syncChannelMesureMapping(expected);

    assertEquals(expected, actual);
  }

  @Test
  void testSyncNature() {
    Nature expected = new Nature();
    expected.setId(1L);
    expected.setName("tSR");
    expected.setValue("tSR");

    when(natureService.find(Mockito.anyString())).thenReturn(expected);
    when(natureService.create(Mockito.any(Nature.class))).thenReturn(expected);

    Nature actual = syncDevicesService.syncNature(expected);

    assertEquals(expected, actual);
  }

  @Test
  void testSyncNature_null() {
    Nature expected = new Nature();
    expected.setId(1L);
    expected.setName("tSR");
    expected.setValue("tSR");

    when(natureService.find(Mockito.anyString())).thenReturn(null);
    when(natureService.create(Mockito.any(Nature.class))).thenReturn(expected);

    Nature actual = syncDevicesService.syncNature(expected);

    assertEquals(expected, actual);
  }

  @Test
  void testSyncPeriod() {
    Period expected = new Period();
    expected.setId(1L);
    expected.setName("_60");
    expected.setValue("_60");

    when(periodService.find(Mockito.anyString())).thenReturn(expected);
    when(periodService.create(Mockito.any(Period.class))).thenReturn(expected);

    Period actual = syncDevicesService.syncPeriod(expected);

    assertEquals(expected, actual);
  }

  @Test
  void testSyncPeriod_null() {
    Period expected = new Period();
    expected.setId(1L);
    expected.setName("_60");
    expected.setValue("_60");

    when(periodService.find(Mockito.anyString())).thenReturn(null);
    when(periodService.create(Mockito.any(Period.class))).thenReturn(expected);

    Period actual = syncDevicesService.syncPeriod(expected);

    assertEquals(expected, actual);
  }

  @Test
  void testSyncMesureConfig() {
    MesureConfig expected = new MesureConfig();
    expected.setId(1L);
    expected.setMesureId("2/z/uAM/360/Am00");
    expected.setMesureType("1HG_MP_E0");
    expected.setNatureId(25L);
    expected.setPeriodId(25L);
    expected.setValue("Am00");

    when(mesureConfigsService.find(Mockito.anyString())).thenReturn(expected);
    when(mesureConfigsService.create(Mockito.any(MesureConfig.class))).thenReturn(expected);

    MesureConfig actual = syncDevicesService.syncMesureConfig(expected);

    assertEquals(expected, actual);
  }

  @Test
  void testSyncMesureConfig_null() {
    MesureConfig expected = new MesureConfig();
    expected.setId(1L);
    expected.setMesureId("2/z/uAM/360/Am00");
    expected.setMesureType("1HG_MP_E0");
    expected.setNatureId(25L);
    expected.setPeriodId(25L);
    expected.setValue("Am00");

    when(mesureConfigsService.find(Mockito.anyString())).thenReturn(null);
    when(mesureConfigsService.create(Mockito.any(MesureConfig.class))).thenReturn(expected);

    MesureConfig actual = syncDevicesService.syncMesureConfig(expected);

    assertEquals(expected, actual);
  }
}
