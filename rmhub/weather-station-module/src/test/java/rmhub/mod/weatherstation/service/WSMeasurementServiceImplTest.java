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
import rmhub.mod.weatherstation.repository.WSMeasurementRepo;

@ExtendWith(SpringExtension.class)
class WSMeasurementServiceImplTest {

  @InjectMocks
  private WSMeasurementServiceImpl wsMeasurementServiceImpl;

  @Mock
  private WSMeasurementRepo wsMeasurementRepo;

  @Test
  void create_Ok() {
    WeatherMeasurement measurement = WeatherMeasurement.builder().deploymentId(1).eqtDtMes("1").eqtMesLgId(1).eqtMesLgType(1).eqtMesNbVal(1)
        .eqtMesPer(1).externalId("WS_51_000").build();
    when(wsMeasurementRepo.save(any())).thenReturn(measurement);
    WeatherMeasurement result = wsMeasurementServiceImpl.create(measurement);
    Assertions.assertEquals(measurement, result);
  }

  @Test
  void update_Ok() {
    WeatherMeasurement measurement = WeatherMeasurement.builder().deploymentId(1).eqtDtMes("1").eqtMesLgId(1).eqtMesLgType(1).eqtMesNbVal(1)
        .eqtMesPer(1).externalId("WS_51_000").build();
    when(wsMeasurementRepo.save(any())).thenReturn(measurement);
    WeatherMeasurement result = wsMeasurementServiceImpl.update(measurement);
    Assertions.assertEquals(measurement, result);
  }

  @Test
  void delete_Ok() {
    Long id = 1L;
    wsMeasurementServiceImpl.delete(id);
    // It's Ok with no exception thrown
  }

  @Test
  void findAll_Ok() {
    WeatherMeasurement measurement = WeatherMeasurement.builder().deploymentId(1).eqtDtMes("1").eqtMesLgId(1).eqtMesLgType(1).eqtMesNbVal(1)
        .eqtMesPer(1).externalId("WS_51_000").build();
    List<WeatherMeasurement> lsmeasurement = new ArrayList<>();
    lsmeasurement.add(measurement);
    when(wsMeasurementRepo.findAll()).thenReturn(lsmeasurement);

    List<WeatherMeasurement> lsAlertResult = wsMeasurementServiceImpl.findAll();
    Assertions.assertEquals(lsmeasurement, lsAlertResult);
  }

  @Test
  void findById_Ok() {
    WeatherMeasurement measurement = WeatherMeasurement.builder().deploymentId(1).eqtDtMes("1").eqtMesLgId(1).eqtMesLgType(1).eqtMesNbVal(1)
        .eqtMesPer(1).externalId("WS_51_000").build();

    when(wsMeasurementRepo.findById(1L)).thenReturn(Optional.of(measurement));

    WeatherMeasurement alertResult = wsMeasurementServiceImpl.findById(1L);
    Assertions.assertEquals(measurement, alertResult);
  }

}
