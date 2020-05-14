package rmhub.mod.notification.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import rmhub.model.mivisu.ssil.AlertResponse;

@Component
@Slf4j
public class WeatherMeasurementCache {

  @Cacheable(value = "weatherMeasure", key = "#p0")
  public AlertResponse getWeatherMeasure(int deploymentId) {
    return new AlertResponse();
  }

  @CachePut(value = "weatherMeasure", key = "#alertResponse.deploymentId")
  public AlertResponse updateAlertResponse(AlertResponse alertResponse, AlertResponse cacheAlertResponse) {

    log.info("Weather measurement updating cache data...");

    if (cacheAlertResponse.getCentraleResponses() == null) {
      return alertResponse;
    }

    return WeatherMeasurementCacheHelper.mergeAlertResponse(alertResponse, cacheAlertResponse);
  }
}
