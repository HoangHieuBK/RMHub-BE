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
import rmhub.mod.devicemgmt.entity.Period;
import rmhub.mod.devicemgmt.repository.PeriodRepo;

@Slf4j
@ExtendWith(SpringExtension.class)
class PeriodServiceImplTest {

  @InjectMocks
  private PeriodServiceImpl periodService;

  @Mock
  private PeriodRepo periodRepo;

  @Test
  void testCreate() {
    Period expected = new Period();
    expected.setId(1L);
    expected.setName("_60");
    expected.setValue("_60");

    when(periodRepo.save(Mockito.any(Period.class))).thenReturn(expected);

    Period actual = periodService.create(expected);

    assertEquals(expected, actual);
  }

  @Test
  void update() {
    Period expected = new Period();
    expected.setId(1L);
    expected.setName("_60");
    expected.setValue("_60");

    when(periodRepo.save(Mockito.any(Period.class))).thenReturn(expected);

    Period actual = periodService.update(expected);

    assertEquals(expected, actual);
  }

  @Test
  void delete() {
    periodService.delete(Mockito.anyLong());

    verify(periodRepo, times(1)).deleteById(Mockito.anyLong());
  }

  @Test
  void deleteAll() {
    Period expected = new Period();
    expected.setId(1L);
    expected.setName("_60");
    expected.setValue("_60");

    when(periodRepo.findAll()).thenReturn(Collections.singletonList(expected));

    periodService.deleteAll();

    verify(periodRepo, times(1)).saveAll(Mockito.anyList());
  }

  @Test
  void find() {
    Period expected = new Period();
    expected.setId(1L);
    expected.setName("_60");
    expected.setValue("_60");

    when(periodRepo.findByValue(Mockito.anyString())).thenReturn(expected);

    Period actual = periodService.find(Mockito.anyString());

    assertEquals(expected, actual);
  }

  @Test
  void findAll() {

    List<Period> expected = new ArrayList<>();

    Period data = new Period();
    data.setId(1L);
    data.setName("_60");
    data.setValue("_60");

    expected.add(data);

    when(periodRepo.findAll()).thenReturn(expected);

    List<Period> actual = periodService.findAll();

    assertEquals(expected, actual);
  }

  @Test
  void findById() {
    Period expected = new Period();
    expected.setId(1L);
    expected.setName("_60");
    expected.setValue("_60");

    when(periodRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(expected));

    Period actual = periodService.findById(Mockito.anyLong());

    assertEquals(expected, actual);
  }

  @Test
  void findById_null() {
    assertThrows(BusinessException.class, () -> periodService.findById(Mockito.anyLong()), "Period doesn't exist.");
  }
}
