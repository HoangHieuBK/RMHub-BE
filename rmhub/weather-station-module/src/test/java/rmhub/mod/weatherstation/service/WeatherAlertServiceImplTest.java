package rmhub.mod.weatherstation.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rmhub.mod.weatherstation.entity.WeatherAlert;
import rmhub.mod.weatherstation.repository.WeatherAlertDataRepo;

@ExtendWith(SpringExtension.class)
class WeatherAlertServiceImplTest {

  @InjectMocks
  private WeatherAlertServiceImpl weatherAlertServiceImpl;

  @Mock
  private WeatherAlertDataRepo weatherAlertDataRepo;

  @Test
  void testNotNull() {
    Assertions.assertNotNull(weatherAlertDataRepo);
  }

  @Test
  void create_Ok() {
    WeatherAlert alert = WeatherAlert.builder().deploymentId(1L).description("Create new Alert").externalId("WS_51_000")
        .handledAt(new Date()).handledBy(1L).isHandled(true)
        .physicalDeviceId(123L).weatherMeasurementId(456L).build();
    when(weatherAlertDataRepo.save(any())).thenReturn(alert);
    WeatherAlert result = weatherAlertServiceImpl.create(alert);
    Assertions.assertEquals(alert, result);
  }

  @Test
  void update_Ok() {
    WeatherAlert alert = WeatherAlert.builder().deploymentId(1L).description("Update Alert").externalId("WS_51_000").handledAt(new Date())
        .handledBy(1L).isHandled(true)
        .physicalDeviceId(123L).weatherMeasurementId(456L).build();
    when(weatherAlertDataRepo.save(any())).thenReturn(alert);
    WeatherAlert result = weatherAlertServiceImpl.update(alert);
    Assertions.assertEquals(alert, result);
  }

  @Test
  void delete_Ok() {
    Long id = 1L;
    weatherAlertServiceImpl.delete(id);
    // It's Ok with no exception thrown
  }

  @Test
  void findAll_Ok() {
    WeatherAlert alert = WeatherAlert.builder().deploymentId(1L).description("one Alert in list").externalId("WS_51_000")
        .handledAt(new Date()).handledBy(1L).isHandled(true)
        .physicalDeviceId(123L).weatherMeasurementId(456L).build();
    List<WeatherAlert> lsAlert = new ArrayList<>();
    lsAlert.add(alert);
    when(weatherAlertDataRepo.findAll()).thenReturn(lsAlert);

    List<WeatherAlert> lsAlertResult = weatherAlertServiceImpl.findAll();
    Assertions.assertEquals(lsAlert, lsAlertResult);
  }

  @Test
  void findById_Ok() {
    WeatherAlert alert = WeatherAlert.builder().id(1L).deploymentId(1L).description("Alert to find").externalId("WS_51_000")
        .handledAt(new Date()).handledBy(1L).isHandled(true)
        .physicalDeviceId(123L).weatherMeasurementId(456L).build();

    when(weatherAlertDataRepo.findById(1L)).thenReturn(Optional.of(alert));

    WeatherAlert alertResult = weatherAlertServiceImpl.findById(1L);
    Assertions.assertEquals(alert, alertResult);
  }
}
