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
import rmhub.mod.devicemgmt.entity.Channel;
import rmhub.mod.devicemgmt.entity.ChannelMesureMapping;
import rmhub.mod.devicemgmt.entity.MesureConfig;
import rmhub.mod.devicemgmt.repository.ChannelMeasureMappingRepo;

@Slf4j
@ExtendWith(SpringExtension.class)
class ChannelMeasureMappingServiceImplTest {

  @InjectMocks
  private ChannelMeasureMappingServiceImpl channelMeasureMappingService;

  @Mock
  private ChannelMeasureMappingRepo channelMeasureMappingRepo;

  @Test
  void testCreate() {
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

    when(channelMeasureMappingRepo.save(Mockito.any(ChannelMesureMapping.class))).thenReturn(expected);

    ChannelMesureMapping actual = channelMeasureMappingService.create(expected);

    assertEquals(expected, actual);
  }

  @Test
  void testCreate_null() {

    ChannelMesureMapping expected = new ChannelMesureMapping();
    expected.setId(1L);
    expected.setChannel(null);
    expected.setMesureConfig(null);

    ChannelMesureMapping actual = channelMeasureMappingService.create(expected);

    assertNull(actual);
  }

  @Test
  void testCreate_channelMesureMappingNull() {

    ChannelMesureMapping actual = channelMeasureMappingService.create(null);

    assertNull(actual);
  }

  @Test
  void testCreate_channelNull() {

    MesureConfig mesureConfig = new MesureConfig();
    mesureConfig.setId(1L);
    mesureConfig.setMesureId("2/z/uAM/360/Am00");
    mesureConfig.setMesureType("1HG_MP_E0");
    mesureConfig.setNatureId(25L);
    mesureConfig.setPeriodId(25L);
    mesureConfig.setValue("Am00");

    ChannelMesureMapping expected = new ChannelMesureMapping();
    expected.setId(1L);
    expected.setChannel(null);
    expected.setMesureConfig(mesureConfig);

    ChannelMesureMapping actual = channelMeasureMappingService.create(expected);

    assertNull(actual);
  }

  @Test
  void testCreate_mesureConfigNull() {

    Channel channel = new Channel();
    channel.setId(1L);
    channel.setName("Channel 1");
    channel.setValue("_1");

    ChannelMesureMapping expected = new ChannelMesureMapping();
    expected.setId(1L);
    expected.setChannel(channel);
    expected.setMesureConfig(null);

    ChannelMesureMapping actual = channelMeasureMappingService.create(expected);

    assertNull(actual);
  }

  @Test
  void find() {
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

    when(channelMeasureMappingRepo.findByChannelIdAndMesureConfigId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(expected);

    ChannelMesureMapping actual = channelMeasureMappingService.find(Mockito.anyLong(), Mockito.anyLong());

    assertEquals(expected, actual);
  }

  @Test
  void deleteAll() {
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

    when(channelMeasureMappingRepo.findAll()).thenReturn(Collections.singletonList(expected));

    channelMeasureMappingService.deleteAll();

    verify(channelMeasureMappingRepo, times(1)).saveAll(Mockito.anyList());
  }
}
