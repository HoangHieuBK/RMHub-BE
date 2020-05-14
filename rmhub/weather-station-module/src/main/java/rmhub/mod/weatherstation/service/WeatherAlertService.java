package rmhub.mod.weatherstation.service;

import java.util.List;
import rmhub.mod.weatherstation.entity.WeatherAlert;

public interface WeatherAlertService {

  WeatherAlert create(WeatherAlert weatherAlert);

  WeatherAlert update(WeatherAlert weatherAlert);

  void delete(Long id);

  List<WeatherAlert> findAll();

  WeatherAlert findById(Long id);
}
