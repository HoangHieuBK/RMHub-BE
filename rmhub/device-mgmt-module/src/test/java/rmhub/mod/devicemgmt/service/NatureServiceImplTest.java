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
import rmhub.mod.devicemgmt.entity.Nature;
import rmhub.mod.devicemgmt.repository.NatureRepo;

@Slf4j
@ExtendWith(SpringExtension.class)
class NatureServiceImplTest {

  @InjectMocks
  private NatureServiceImpl natureService;

  @Mock
  private NatureRepo natureRepo;

  @Test
  void testCreate() {
    Nature expected = new Nature();
    expected.setId(1L);
    expected.setName("tSR");
    expected.setValue("tSR");

    when(natureRepo.save(Mockito.any(Nature.class))).thenReturn(expected);

    Nature actual = natureService.create(expected);

    assertEquals(expected, actual);
  }

  @Test
  void update() {
    Nature expected = new Nature();
    expected.setId(1L);
    expected.setName("tSR");
    expected.setValue("tSR");

    when(natureRepo.save(Mockito.any(Nature.class))).thenReturn(expected);

    Nature actual = natureService.update(expected);

    assertEquals(expected, actual);
  }

  @Test
  void delete() {
    natureService.delete(Mockito.anyLong());

    verify(natureRepo, times(1)).deleteById(Mockito.anyLong());
  }

  @Test
  void deleteAll() {
    Nature expected = new Nature();
    expected.setId(1L);
    expected.setName("tSR");
    expected.setValue("tSR");

    when(natureRepo.findAll()).thenReturn(Collections.singletonList(expected));

    natureService.deleteAll();

    verify(natureRepo, times(1)).saveAll(Mockito.anyList());
  }

  @Test
  void find() {
    Nature expected = new Nature();
    expected.setId(1L);
    expected.setName("tSR");
    expected.setValue("tSR");

    when(natureRepo.findByValue(Mockito.anyString())).thenReturn(expected);

    Nature actual = natureService.find(Mockito.anyString());

    assertEquals(expected, actual);
  }

  @Test
  void findAll() {

    List<Nature> expected = new ArrayList<>();

    Nature data = new Nature();
    data.setId(1L);
    data.setName("tSR");
    data.setValue("tSR");

    expected.add(data);

    when(natureRepo.findAll()).thenReturn(expected);

    List<Nature> actual = natureService.findAll();

    assertEquals(expected, actual);
  }

  @Test
  void findById() {
    Nature expected = new Nature();
    expected.setId(1L);
    expected.setName("tSR");
    expected.setValue("tSR");

    when(natureRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(expected));

    Nature actual = natureService.findById(Mockito.anyLong());

    assertEquals(expected, actual);
  }

  @Test
  void findById_null() {
    assertThrows(BusinessException.class, () -> natureService.findById(Mockito.anyLong()), "Nature doesn't exist.");
  }
}
