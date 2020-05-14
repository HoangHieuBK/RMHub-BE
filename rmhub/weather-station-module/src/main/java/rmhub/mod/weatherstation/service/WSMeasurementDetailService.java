package rmhub.mod.weatherstation.service;

import java.util.List;
import rmhub.mod.weatherstation.entity.WeatherMeasurementDetail;

public interface WSMeasurementDetailService {

  WeatherMeasurementDetail create(WeatherMeasurementDetail weatherMeasurementDetail);

  WeatherMeasurementDetail update(WeatherMeasurementDetail weatherMeasurementDetail);

  void delete(Long id);

  List<WeatherMeasurementDetail> findAll();

  WeatherMeasurementDetail findById(Long id);

}
