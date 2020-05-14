package rmhub.mod.weatherstation.service;

import java.util.List;
import rmhub.mod.weatherstation.entity.WeatherMeasurement;

public interface WSMeasurementService {

  WeatherMeasurement create(WeatherMeasurement weatherMeasurement);

  WeatherMeasurement update(WeatherMeasurement weatherMeasurement);

  void delete(Long id);

  List<WeatherMeasurement> findAll();

  WeatherMeasurement findById(Long id);
}
