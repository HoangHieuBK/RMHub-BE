package rmhub.mod.devicemgmt.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import rmhub.mod.devicemgmt.entity.DeviceType;
import rmhub.mod.devicemgmt.repository.DeviceTypeRepo;

@Slf4j
@ExtendWith(SpringExtension.class)
class DeviceTypeServiceImplTest {

  @InjectMocks
  private DeviceTypeServiceImpl deviceTypeService;

  @Mock
  private DeviceTypeRepo deviceTypeRepo;

  @Test
  void testCreate() {
    DeviceType expected = new DeviceType();
    expected.setId(1L);
    expected.setName("WS");

    when(deviceTypeRepo.save(Mockito.any(DeviceType.class))).thenReturn(expected);

    DeviceType actual = deviceTypeService.create(expected);

    assertEquals(expected, actual);
  }

  @Test
  void update() {
    DeviceType expected = new DeviceType();
    expected.setId(1L);
    expected.setName("WS");

    when(deviceTypeRepo.save(Mockito.any(DeviceType.class))).thenReturn(expected);

    DeviceType actual = deviceTypeService.update(expected);

    assertEquals(expected, actual);
  }

  @Test
  void delete() {
    deviceTypeService.delete(Mockito.anyLong());

    verify(deviceTypeRepo, times(1)).deleteById(Mockito.anyLong());
  }

  @Test
  void findAll() {

    List<DeviceType> expected = new ArrayList<>();

    DeviceType data = new DeviceType();
    data.setId(1L);
    data.setName("WS");

    expected.add(data);

    when(deviceTypeRepo.findAll()).thenReturn(expected);

    List<DeviceType> actual = deviceTypeService.findAll();

    assertEquals(expected, actual);
  }

  @Test
  void findById() {
    DeviceType expected = new DeviceType();
    expected.setId(1L);
    expected.setName("WS");

    when(deviceTypeRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(expected));

    DeviceType actual = deviceTypeService.findById(Mockito.anyLong());

    assertEquals(expected, actual);
  }

  @Test
  void findById_null() {
    assertThrows(BusinessException.class, () -> deviceTypeService.findById(Mockito.anyLong()), "DeviceType doesn't exist.");
  }
}
