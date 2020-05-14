package rmhub.mod.trafficlogger.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
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
import rmhub.mod.trafficlogger.entity.TrafficMeasurementDetail;
import rmhub.mod.trafficlogger.repository.TrafficMeasurementDetailRepo;

@ExtendWith(SpringExtension.class)
class TrafficMeasurementDetailServiceImplTest {

  @Mock
  private TrafficMeasurementDetailRepo repository;

  @InjectMocks
  private TrafficMeasurementDetailServiceImpl serviceImpl;

  private final TrafficMeasurementDetail trafficMeasurementDetail = TrafficMeasurementDetail.builder()
      .id(1L)
      .eqtMesVal("123")
      .eqtMesKlif("123")
      .index(1)
      .build();
  private final TrafficMeasurementDetail trafficMeasurementDetail1 = TrafficMeasurementDetail.builder()
      .id(2L)
      .eqtMesVal("123")
      .eqtMesKlif("123")
      .index(1)
      .build();

  @Test
  void test() {
    var trafficMeasureDetail = TrafficMeasurementDetail.builder().eqtMesVal("1").build();
    Mockito.when(repository.save(ArgumentMatchers.any(TrafficMeasurementDetail.class))).thenReturn(trafficMeasureDetail);
    var result = serviceImpl.create(trafficMeasureDetail);
    Assertions.assertNotNull(result);
  }

  @Test
  void create_trafficMeasurementDetail() {
    when(repository.save(trafficMeasurementDetail)).thenReturn(trafficMeasurementDetail);
    TrafficMeasurementDetail actual = serviceImpl.create(trafficMeasurementDetail);
    assertEquals(trafficMeasurementDetail, actual);
  }

  @Test
  void update_trafficMeasurementDetail() {
    when(repository.save(trafficMeasurementDetail)).thenReturn(trafficMeasurementDetail);
    TrafficMeasurementDetail actual = serviceImpl.update(trafficMeasurementDetail);
    assertEquals(trafficMeasurementDetail, actual);
  }

  @Test
  void delete_trafficMeasurementDetail_byId() {
    doNothing().when(repository).deleteById(Mockito.anyLong());
    serviceImpl.delete(1L);
    verify(repository, Mockito.only()).deleteById(1L);
  }

  @Test
  void findAll_trafficMeasurementDetail() {
    List<TrafficMeasurementDetail> measurementDetails = new ArrayList<>();
    measurementDetails.add(trafficMeasurementDetail);
    measurementDetails.add(trafficMeasurementDetail1);
    when(repository.findAll()).thenReturn(measurementDetails);
    List<TrafficMeasurementDetail> result = serviceImpl.findAll();
    assertEquals(2, result.size());
    assertEquals(trafficMeasurementDetail, measurementDetails.get(0));
    assertEquals(trafficMeasurementDetail1, measurementDetails.get(1));

  }

  @Test
  void findById() {
    when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(trafficMeasurementDetail));
    TrafficMeasurementDetail actual = serviceImpl.findById(1L);
    Assertions.assertEquals(trafficMeasurementDetail, actual);
  }
}
