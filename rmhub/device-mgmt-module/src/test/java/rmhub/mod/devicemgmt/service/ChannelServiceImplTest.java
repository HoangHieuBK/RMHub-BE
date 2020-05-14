package rmhub.mod.devicemgmt.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rmhub.common.exception.BusinessException;
import rmhub.mod.devicemgmt.entity.Channel;
import rmhub.mod.devicemgmt.repository.ChannelRepo;

@Slf4j
@ExtendWith(SpringExtension.class)
class ChannelServiceImplTest {

  @InjectMocks
  private ChannelServiceImpl channelService;

  @Mock
  private ChannelRepo channelRepo;

  @Test
  void testCreate() {
    Channel expected = new Channel();
    expected.setId(1L);
    expected.setDeviceTypeId(1L);
    expected.setName("Channel 1");
    expected.setValue("_1");

    when(channelRepo.save(Mockito.any(Channel.class))).thenReturn(expected);

    Channel actual = channelService.create(expected);

    assertEquals(expected, actual);
  }

  @Test
  void testUpdate() {
    Channel expected = new Channel();
    expected.setId(1L);
    expected.setDeviceTypeId(1L);
    expected.setName("Channel 1");
    expected.setValue("_1");

    when(channelRepo.save(Mockito.any(Channel.class))).thenReturn(expected);

    Channel actual = channelService.update(expected);

    assertEquals(expected, actual);
  }

  @Test
  void testDelete() {
    channelService.delete(1L);

    verify(channelRepo, times(1)).deleteById(Mockito.anyLong());
  }

  @Test
  void testDeleteAll() {
    Channel expected = new Channel();
    expected.setId(1L);
    expected.setDeviceTypeId(1L);
    expected.setName("Channel 1");
    expected.setValue("_1");

    when(channelRepo.findAll()).thenReturn(Collections.singletonList(expected));

    channelService.deleteAll();

    verify(channelRepo, times(1)).saveAll(Mockito.anyList());
  }

  @Test
  void testFindByValue() {
    Channel expected = new Channel();
    expected.setId(1L);
    expected.setDeviceTypeId(1L);
    expected.setName("Channel 1");
    expected.setValue("_1");

    when(channelRepo.findByValue(Mockito.anyString())).thenReturn(expected);

    Channel actual = channelService.findByValue("Value");

    assertEquals(expected, actual);
  }

  @Test
  void testFind() {
    Channel expected = new Channel();
    expected.setId(1L);
    expected.setDeviceTypeId(1L);
    expected.setName("Channel 1");
    expected.setValue("_1");

    when(channelRepo.findByValueAndDeviceTypeId(Mockito.anyString(), Mockito.anyLong())).thenReturn(expected);

    Channel actual = channelService.find(Mockito.anyString(), Mockito.anyLong());

    assertEquals(expected, actual);
  }

  @Test
  void testFindAll() {

    List<Channel> expected = new ArrayList<>();

    Channel data = new Channel();
    data.setId(1L);
    data.setDeviceTypeId(1L);
    data.setName("Channel 1");
    data.setValue("_1");

    when(channelRepo.findAll()).thenReturn(expected);

    List<Channel> actual = channelService.findAll();

    assertEquals(expected, actual);
  }

  @Test
  void testFindById() {
    Channel expected = new Channel();
    expected.setId(1L);
    expected.setDeviceTypeId(1L);
    expected.setName("Channel 1");
    expected.setValue("_1");

    when(channelRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(expected));

    Channel actual = channelService.findById(Mockito.anyLong());

    assertEquals(expected, actual);
  }

  @Test
  void testFindById_ThrowException() {
    assertThrows(BusinessException.class, () -> channelService.findById(Mockito.anyLong()), "Channel doesn't exist.");
  }
}
