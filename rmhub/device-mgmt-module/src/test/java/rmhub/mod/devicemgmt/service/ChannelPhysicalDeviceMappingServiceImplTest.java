package rmhub.mod.devicemgmt.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rmhub.mod.devicemgmt.common.StatusEnum;
import rmhub.mod.devicemgmt.entity.Channel;
import rmhub.mod.devicemgmt.entity.ChannelPhysicalDeviceMapping;
import rmhub.mod.devicemgmt.entity.PhysicalDevice;
import rmhub.mod.devicemgmt.repository.ChannelPhysicalDeviceMappingRepo;

@Slf4j
@ExtendWith(SpringExtension.class)
class ChannelPhysicalDeviceMappingServiceImplTest {

  @InjectMocks
  private ChannelPhysicalDeviceMappingServiceImpl channelPhysicalDeviceMappingService;

  @Mock
  private ChannelPhysicalDeviceMappingRepo channelPhysicalDeviceMappingRepo;

  @Test
  void testCreate() {
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

    when(channelPhysicalDeviceMappingRepo.save(Mockito.any(ChannelPhysicalDeviceMapping.class))).thenReturn(expected);

    ChannelPhysicalDeviceMapping actual = channelPhysicalDeviceMappingService.create(expected);

    assertEquals(expected, actual);
  }

  @Test
  void testCreate_null() {

    ChannelPhysicalDeviceMapping expected = new ChannelPhysicalDeviceMapping();
    expected.setId(1L);
    expected.setChannel(null);
    expected.setPhysicalDevice(null);

    ChannelPhysicalDeviceMapping actual = channelPhysicalDeviceMappingService.create(expected);

    assertNull(actual);
  }

  @Test
  void find() {
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

    when(channelPhysicalDeviceMappingRepo.findByChannelIdAndPhysicalDeviceId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(expected);

    ChannelPhysicalDeviceMapping actual = channelPhysicalDeviceMappingService.find(Mockito.anyLong(), Mockito.anyLong());

    assertEquals(expected, actual);
  }

  @Test
  void deleteAll() {
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

    when(channelPhysicalDeviceMappingRepo.findAll()).thenReturn(Collections.singletonList(expected));

    channelPhysicalDeviceMappingService.deleteAll();

    verify(channelPhysicalDeviceMappingRepo, times(1)).saveAll(Mockito.anyList());
  }
}
