package rmhub.mod.trafficlogger.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rmhub.mod.trafficlogger.entity.TrafficMeasurement;
import rmhub.mod.trafficlogger.repository.TrafficMeasurementRepo;

@ExtendWith(SpringExtension.class)
class TrafficMeasurementServiceImplTest {

  @Mock
  private TrafficMeasurementRepo repository;

  @InjectMocks
  private TrafficMeasurementServiceImpl serviceImpl;

  private final TrafficMeasurement trafficMeasurement = TrafficMeasurement.builder()
      .id(1L)
      .eqtDtMes("123")
      .eqtMesPer(1)
      .eqtMesLgId(1)
      .eqtMesId("123")
      .eqtMesLgType(1)
      .eqtMesType("123")
      .eqtMesNbVal(1)
      .externalId("123")
      .deploymentId(1)
      .trafficMeasurementDetail(null)
      .createdDate(new Date())
      .build();

  private final TrafficMeasurement trafficMeasurement1 = TrafficMeasurement.builder()
      .id(2L)
      .eqtDtMes("123")
      .eqtMesPer(1)
      .eqtMesLgId(1)
      .eqtMesId("123")
      .eqtMesLgType(1)
      .eqtMesType("123")
      .eqtMesNbVal(1)
      .externalId("123")
      .deploymentId(1)
      .trafficMeasurementDetail(null)
      .createdDate(new Date())
      .build();

  @Test
  void test() {
    var trafficMeasure = TrafficMeasurement.builder().deploymentId(1).build();
    Mockito.when(repository.save(ArgumentMatchers.any(TrafficMeasurement.class))).thenReturn(trafficMeasure);
    var result = serviceImpl.create(trafficMeasure);
    Assertions.assertNotNull(result);
  }

  @Test
  void create_trafficMeasurement() {
    when(repository.save(trafficMeasurement)).thenReturn(trafficMeasurement);
    TrafficMeasurement actual = serviceImpl.create(trafficMeasurement);
    assertEquals(trafficMeasurement, actual);
  }

  @Test
  void update_trafficMeasurement() {
    when(repository.save(trafficMeasurement)).thenReturn(trafficMeasurement);
    TrafficMeasurement actual = serviceImpl.update(trafficMeasurement);
    assertEquals(trafficMeasurement, actual);
  }

  @Test
  void delete_trafficMeasurement_byId() {
    doNothing().when(repository).deleteById(Mockito.anyLong());
    serviceImpl.delete(1L);
    verify(repository, Mockito.only()).deleteById(1L);
  }

  @Test
  void findAll_trafficMeasurement() {
    List<TrafficMeasurement> trafficMeasurements = new ArrayList<>();
    trafficMeasurements.add(trafficMeasurement);
    trafficMeasurements.add(trafficMeasurement1);
    when(repository.findAll()).thenReturn(trafficMeasurements);
    List<TrafficMeasurement> result = serviceImpl.findAll();
    assertEquals(2, result.size());
    assertEquals(trafficMeasurement, trafficMeasurements.get(0));
    assertEquals(trafficMeasurement1, trafficMeasurements.get(1));
  }

  @Test
  void findById() {
    when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(trafficMeasurement));
    TrafficMeasurement actual = serviceImpl.findById(Mockito.anyLong());
    Assertions.assertEquals(trafficMeasurement, actual);
  }

}
