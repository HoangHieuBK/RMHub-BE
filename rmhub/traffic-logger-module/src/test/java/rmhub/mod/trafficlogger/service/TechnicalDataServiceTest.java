package rmhub.mod.trafficlogger.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rmhub.common.exception.BusinessException;
import rmhub.mod.trafficlogger.entity.TechnicalData;
import rmhub.mod.trafficlogger.repository.TechnicalDataRepo;

@ExtendWith(SpringExtension.class)
class TechnicalDataServiceTest {

  @InjectMocks
  TechnicalDataServiceImpl technicalDataService;

  @Mock
  TechnicalDataRepo technicalDataRepo;

  @Test
  void testCreate() {
    TechnicalData expected = new TechnicalData();
    expected.setId(1L);
    expected.setExternalId("TC_40_400");
    expected.setEqtConfVersion(1);
    expected.setEtatAlim(1);
    expected.setEtatCom(1);
    expected.setEtatDate("20/02/2020");
    expected.setEtatSys(1);
    expected.setPhysicalDeviceId(1L);

    when(technicalDataRepo.save(Mockito.any(TechnicalData.class))).thenReturn(expected);

    TechnicalData actual = technicalDataService.create(expected);

    assertEquals(expected, actual);
  }

  @Test
  void update() {
    TechnicalData expected = new TechnicalData();
    expected.setId(1L);
    expected.setExternalId("TC_40_400");
    expected.setEqtConfVersion(1);
    expected.setEtatAlim(1);
    expected.setEtatCom(1);
    expected.setEtatDate("20/02/2020");
    expected.setEtatSys(1);
    expected.setPhysicalDeviceId(1L);

    when(technicalDataRepo.save(Mockito.any(TechnicalData.class))).thenReturn(expected);

    TechnicalData actual = technicalDataService.update(expected);

    assertEquals(expected, actual);
  }

  @Test
  void delete() {
    technicalDataService.delete(Mockito.anyLong());

    verify(technicalDataRepo, times(1)).deleteById(Mockito.anyLong());
  }

  @Test
  void findAll() {

    List<TechnicalData> expected = new ArrayList<>();

    TechnicalData data = new TechnicalData();
    data.setId(1L);
    data.setExternalId("TC_40_400");
    data.setEqtConfVersion(1);
    data.setEtatAlim(1);
    data.setEtatCom(1);
    data.setEtatDate("20/02/2020");
    data.setEtatSys(1);
    data.setPhysicalDeviceId(1L);

    expected.add(data);

    when(technicalDataRepo.findAll()).thenReturn(expected);

    List<TechnicalData> actual = technicalDataService.findAll();

    assertEquals(expected, actual);
  }

  @Test
  void findById() {
    TechnicalData expected = new TechnicalData();
    expected.setId(1L);
    expected.setExternalId("TC_40_400");
    expected.setEqtConfVersion(1);
    expected.setEtatAlim(1);
    expected.setEtatCom(1);
    expected.setEtatDate("20/02/2020");
    expected.setEtatSys(1);
    expected.setPhysicalDeviceId(1L);

    when(technicalDataRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(expected));

    TechnicalData actual = technicalDataService.findById(Mockito.anyLong());

    assertEquals(expected, actual);
  }

  @Test
  void findById_null() {
    assertThrows(BusinessException.class, () -> technicalDataService.findById(Mockito.anyLong()), "TechnicalData doesn't exist.");
  }
}
