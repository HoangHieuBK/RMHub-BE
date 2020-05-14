package rmhub.mod.weatherstation.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rmhub.mod.weatherstation.entity.WeatherMeasurement;
import rmhub.mod.weatherstation.entity.WeatherMeasurementDetail;
import rmhub.mod.weatherstation.repository.WSMeasurementDetailRepo;

@ExtendWith(SpringExtension.class)
class WSMeasurementDetailServiceImplTest {

  @InjectMocks
  private WSMeasurementDetailServiceImpl wsMeasurementDetailServiceImpl;

  @Mock
  private WSMeasurementDetailRepo wsMeasurementDetailRepo;

  @Test
  void testNotNull() {
    Assertions.assertNotNull(wsMeasurementDetailRepo);
  }

  @Test
  void create_Ok() {
    WeatherMeasurementDetail detailtMeasurement = WeatherMeasurementDetail.builder().eqtMesKlif("&1").eqtMesVal("1")
        .index(1).weatherMeasurement(new WeatherMeasurement()).build();
    when(wsMeasurementDetailRepo.save(any())).thenReturn(detailtMeasurement);
    WeatherMeasurementDetail result = wsMeasurementDetailServiceImpl.create(detailtMeasurement);
    Assertions.assertEquals(detailtMeasurement, result);
  }

  @Test
  void update_Ok() {
    WeatherMeasurementDetail detailtMeasurement = WeatherMeasurementDetail.builder().eqtMesKlif("&1").eqtMesVal("1")
        .index(1).weatherMeasurement(new WeatherMeasurement()).build();
    when(wsMeasurementDetailRepo.save(any())).thenReturn(detailtMeasurement);
    WeatherMeasurementDetail result = wsMeasurementDetailServiceImpl.update(detailtMeasurement);
    Assertions.assertEquals(detailtMeasurement, result);
  }

  @Test
  void delete_Ok() {
    Long id = 1L;
    wsMeasurementDetailServiceImpl.delete(id);
    // It's Ok with no exception thrown
  }

  @Test
  void findAll_Ok() {
    WeatherMeasurementDetail detailtMeasurement = WeatherMeasurementDetail.builder().eqtMesKlif("&1").eqtMesVal("1")
        .index(1).weatherMeasurement(new WeatherMeasurement()).build();
    List<WeatherMeasurementDetail> lsdetailtMeasurement = new ArrayList<>();
    lsdetailtMeasurement.add(detailtMeasurement);
    when(wsMeasurementDetailRepo.findAll()).thenReturn(lsdetailtMeasurement);

    List<WeatherMeasurementDetail> lsAlertResult = wsMeasurementDetailServiceImpl.findAll();
    Assertions.assertEquals(lsdetailtMeasurement, lsAlertResult);
  }

  @Test
  void findById_Ok() {
    WeatherMeasurementDetail detailtMeasurement = WeatherMeasurementDetail.builder().eqtMesKlif("&1").eqtMesVal("1")
        .index(1).weatherMeasurement(new WeatherMeasurement()).build();

    when(wsMeasurementDetailRepo.findById(1L)).thenReturn(Optional.of(detailtMeasurement));

    WeatherMeasurementDetail alertResult = wsMeasurementDetailServiceImpl.findById(1L);
    Assertions.assertEquals(detailtMeasurement, alertResult);
  }
}
