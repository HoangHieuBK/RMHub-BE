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
import rmhub.mod.devicemgmt.entity.MesureConfig;
import rmhub.mod.devicemgmt.repository.MesureConfigsRepo;

@Slf4j
@ExtendWith(SpringExtension.class)
class MesureConfigsServiceImplTest {

  @InjectMocks
  private MesureConfigsServiceImpl mesureConfigsService;

  @Mock
  private MesureConfigsRepo mesureConfigsRepo;

  @Test
  void testCreate() {
    MesureConfig expected = new MesureConfig();
    expected.setId(1L);
    expected.setMesureId("2/z/uAM/360/Am00");
    expected.setMesureType("1HG_MP_E0");
    expected.setNatureId(25L);
    expected.setPeriodId(25L);
    expected.setValue("Am00");

    when(mesureConfigsRepo.save(Mockito.any(MesureConfig.class))).thenReturn(expected);

    MesureConfig actual = mesureConfigsService.create(expected);

    assertEquals(expected, actual);
  }

  @Test
  void update() {
    MesureConfig expected = new MesureConfig();
    expected.setId(1L);
    expected.setMesureId("2/z/uAM/360/Am00");
    expected.setMesureType("1HG_MP_E0");
    expected.setNatureId(25L);
    expected.setPeriodId(25L);
    expected.setValue("Am00");

    when(mesureConfigsRepo.save(Mockito.any(MesureConfig.class))).thenReturn(expected);

    MesureConfig actual = mesureConfigsService.update(expected);

    assertEquals(expected, actual);
  }

  @Test
  void delete() {
    mesureConfigsService.delete(Mockito.anyLong());

    verify(mesureConfigsRepo, times(1)).deleteById(Mockito.anyLong());
  }

  @Test
  void deleteAll() {
    MesureConfig expected = new MesureConfig();
    expected.setId(1L);
    expected.setMesureId("2/z/uAM/360/Am00");
    expected.setMesureType("1HG_MP_E0");
    expected.setNatureId(25L);
    expected.setPeriodId(25L);
    expected.setValue("Am00");

    when(mesureConfigsRepo.findAll()).thenReturn(Collections.singletonList(expected));

    mesureConfigsService.deleteAll();

    verify(mesureConfigsRepo, times(1)).saveAll(Mockito.anyList());
  }

  @Test
  void find() {
    MesureConfig expected = new MesureConfig();
    expected.setId(1L);
    expected.setMesureId("2/z/uAM/360/Am00");
    expected.setMesureType("1HG_MP_E0");
    expected.setNatureId(25L);
    expected.setPeriodId(25L);
    expected.setValue("Am00");

    when(mesureConfigsRepo.findByMesureId(Mockito.anyString())).thenReturn(expected);

    MesureConfig actual = mesureConfigsService.find(Mockito.anyString());

    assertEquals(expected, actual);
  }

  @Test
  void findAll() {

    List<MesureConfig> expected = new ArrayList<>();

    MesureConfig data = new MesureConfig();
    data.setId(1L);
    data.setMesureId("2/z/uAM/360/Am00");
    data.setMesureType("1HG_MP_E0");
    data.setNatureId(25L);
    data.setPeriodId(25L);
    data.setValue("Am00");

    expected.add(data);

    when(mesureConfigsRepo.findAll()).thenReturn(expected);

    List<MesureConfig> actual = mesureConfigsService.findAll();

    assertEquals(expected, actual);
  }

  @Test
  void findById() {
    MesureConfig expected = new MesureConfig();
    expected.setId(1L);
    expected.setMesureId("2/z/uAM/360/Am00");
    expected.setMesureType("1HG_MP_E0");
    expected.setNatureId(25L);
    expected.setPeriodId(25L);
    expected.setValue("Am00");

    when(mesureConfigsRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(expected));

    MesureConfig actual = mesureConfigsService.findById(Mockito.anyLong());

    assertEquals(expected, actual);
  }

  @Test
  void findById_null() {
    assertThrows(BusinessException.class, () -> mesureConfigsService.findById(Mockito.anyLong()), "MesureConfig doesn't exist.");
  }
}
