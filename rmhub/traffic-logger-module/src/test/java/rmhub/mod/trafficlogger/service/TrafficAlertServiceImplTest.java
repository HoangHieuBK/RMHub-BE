package rmhub.mod.trafficlogger.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rmhub.common.exception.BusinessException;
import rmhub.mod.trafficlogger.dto.request.UpdateTrafficAlertDto;
import rmhub.mod.trafficlogger.dto.response.TrafficAlertDto;
import rmhub.mod.trafficlogger.entity.TrafficAlert;
import rmhub.mod.trafficlogger.repository.TrafficAlertRepo;

@ExtendWith(SpringExtension.class)
class TrafficAlertServiceImplTest {

  @Mock
  private TrafficAlertRepo repository;

  @InjectMocks
  private TrafficAlertServiceImpl serviceImpl;

  @Mock
  private ModelMapper modelMapper;

  private final TrafficAlert trafficAlert = TrafficAlert.builder()
      .id(1L)
      .deploymentId(1)
      .description("123")
      .externalId("123")
      .cause("abc")
      .isHandled(false)
      .handledBy("132")
      .trafficMeasurementId(123L)
      .build();

  private final TrafficAlert trafficAlert1 = TrafficAlert.builder()
      .id(2L)
      .deploymentId(1)
      .description("123")
      .externalId("123")
      .cause("abc")

      .isHandled(false)
      .handledBy("132")
      .trafficMeasurementId(123L)
      .createdDate(new Date())
      .build();

  private final TrafficAlert trafficAlert2 = TrafficAlert.builder()
      .id(3L)
      .deploymentId(1)
      .description("123")
      .externalId("123")
      .cause("abc")
      .isHandled(false)
      .handledBy("132")
      .trafficMeasurementId(123L)
      .createdDate(new Date())
      .build();

  private final TrafficAlertDto trafficAlertDto = TrafficAlertDto.builder()
      .id(1L)
      .physicalDeviceId(1L)
      .trafficMeasurementId(1L)
      .deploymentId(1L)
      .description("123")
      .externalId(123L)
      .cause("abc")
      .isHandled(false)
      .handledBy("132")
      .build();

  private final UpdateTrafficAlertDto updateTrafficAlertDto = UpdateTrafficAlertDto.builder()
      .description("abc")
      .isHandled(false)
      .build();

  @BeforeEach
  void set() {
    when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(trafficAlert));
  }

  @Test
  void test() {
    var trafficAlert = TrafficAlert.builder().deploymentId(1).build();
    Mockito.when(repository.save(ArgumentMatchers.any(TrafficAlert.class))).thenReturn(trafficAlert);
    var result = serviceImpl.create(trafficAlert);
    Assertions.assertNotNull(result);
  }

  @Test
  void create_trafficAlert() {
    when(repository.save(trafficAlert)).thenReturn(trafficAlert);
    TrafficAlert actual = serviceImpl.create(trafficAlert);
    assertEquals(trafficAlert, actual);
  }

  @Test
  void update_trafficAlert() {
    when(repository.save(trafficAlert)).thenReturn(trafficAlert);
    TrafficAlert actual = serviceImpl.update(trafficAlert);
    assertEquals(trafficAlert, actual);
  }

  @Test
  void deleteTrafficAlert_byId() {
    doNothing().when(repository).deleteById(Mockito.anyLong());
    serviceImpl.delete(1L);
    verify(repository, Mockito.only()).deleteById(1L);
  }

  @Test
  void findAll_trafficAlert() {
    List<TrafficAlert> trafficAlertList = new ArrayList<>();
    trafficAlertList.add(trafficAlert);
    trafficAlertList.add(trafficAlert1);
    trafficAlertList.add(trafficAlert2);
    when(repository.findAll()).thenReturn(trafficAlertList);
    List<TrafficAlert> result = serviceImpl.findAll();
    assertEquals(3, result.size());
    assertEquals(trafficAlert, trafficAlertList.get(0));
    assertEquals(trafficAlert1, trafficAlertList.get(1));
    assertEquals(trafficAlert2, trafficAlertList.get(2));
  }

  @Test
  void findById() {
    TrafficAlert actual = serviceImpl.findById(1L);
    Assertions.assertEquals(trafficAlert, actual);
  }

  @Test
  void findById_null() {
    BusinessException exception = assertThrows(BusinessException.class, () -> serviceImpl.findById(null));
    assertEquals(exception.getMessage(), "Not found!");
  }

  @Test
  void getList_trafficAlertDto() {
    List<TrafficAlert> trafficAlertList = new ArrayList<>();
    trafficAlertList.add(trafficAlert);
    when(repository.findAll()).thenReturn(trafficAlertList);
    when(modelMapper.map(Mockito.any(TrafficAlert.class), any())).thenReturn(trafficAlertDto);
    Assertions.assertEquals(1, serviceImpl.list().size());
    verify(modelMapper, Mockito.atLeastOnce()).map(Mockito.any(TrafficAlert.class), any());
  }

  @Test
  void get_trafficAlertDto() {
    when(modelMapper.map(Mockito.any(TrafficAlert.class), eq(TrafficAlertDto.class))).thenReturn(trafficAlertDto);
    assertEquals(serviceImpl.get(1L), trafficAlertDto);
  }

  @Test
  void update_trafficAlertDto() {
    when(modelMapper.map(Mockito.any(TrafficAlertDto.class), Mockito.any())).thenReturn(trafficAlert);
    when(repository.save(Mockito.any(TrafficAlert.class))).thenReturn(trafficAlert);
    when(modelMapper.map(Mockito.any(TrafficAlert.class), Mockito.any())).thenReturn(trafficAlertDto);
    var actual = serviceImpl.update(1L, updateTrafficAlertDto);
    assertEquals(trafficAlertDto, actual);
  }
}
