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
import rmhub.mod.devicemgmt.entity.Pool;
import rmhub.mod.devicemgmt.repository.PoolRepo;

@Slf4j
@ExtendWith(SpringExtension.class)
class PoolServiceImplTest {

  @InjectMocks
  private PoolServiceImpl poolService;

  @Mock
  private PoolRepo poolRepo;

  @Test
  void testCreate() {
    Pool expected = new Pool();
    expected.setId(1L);
    expected.setName("Direction_1_WE");
    expected.setValue("_1");

    when(poolRepo.save(Mockito.any(Pool.class))).thenReturn(expected);

    Pool actual = poolService.create(expected);

    assertEquals(expected, actual);
  }

  @Test
  void update() {
    Pool expected = new Pool();
    expected.setId(1L);
    expected.setName("Direction_1_WE");
    expected.setValue("_1");

    when(poolRepo.save(Mockito.any(Pool.class))).thenReturn(expected);

    Pool actual = poolService.update(expected);

    assertEquals(expected, actual);
  }

  @Test
  void delete() {
    poolService.delete(Mockito.anyLong());

    verify(poolRepo, times(1)).deleteById(Mockito.anyLong());
  }

  @Test
  void deleteAll() {
    Pool expected = new Pool();
    expected.setId(1L);
    expected.setName("Direction_1_WE");
    expected.setValue("_1");

    when(poolRepo.findAll()).thenReturn(Collections.singletonList(expected));

    poolService.deleteAll();

    verify(poolRepo, times(1)).saveAll(Mockito.anyList());
  }

  @Test
  void find() {
    Pool expected = new Pool();
    expected.setId(1L);
    expected.setName("Direction_1_WE");
    expected.setValue("_1");

    when(poolRepo.findByValue(Mockito.anyString())).thenReturn(expected);

    Pool actual = poolService.findByValue(Mockito.anyString());

    assertEquals(expected, actual);
  }

  @Test
  void findAll() {

    List<Pool> expected = new ArrayList<>();

    Pool data = new Pool();
    data.setId(1L);
    data.setName("Direction_1_WE");
    data.setValue("_1");

    expected.add(data);

    when(poolRepo.findAll()).thenReturn(expected);

    List<Pool> actual = poolService.findAll();

    assertEquals(expected, actual);
  }

  @Test
  void findById() {
    Pool expected = new Pool();
    expected.setId(1L);
    expected.setName("Direction_1_WE");
    expected.setValue("_1");

    when(poolRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(expected));

    Pool actual = poolService.findById(Mockito.anyLong());

    assertEquals(expected, actual);
  }

  @Test
  void findById_null() {
    assertThrows(BusinessException.class, () -> poolService.findById(Mockito.anyLong()), "Pool doesn't exist.");
  }
}
