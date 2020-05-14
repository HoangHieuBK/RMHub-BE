package rmhub.mod.weatherstation.service;

import java.util.List;
import rmhub.mod.weatherstation.entity.WeatherAlertRule;
import rmhub.model.weatherStation.AlertRule;

public interface AlertSettingsService {

  AlertRule create(AlertRule weatherAlertData);

  AlertRule update(AlertRule weatherAlertData);

  void delete(Long id);

  List<AlertRule> findAll(Integer deploymentId);

  WeatherAlertRule findById(Long id);

  List<WeatherAlertRule> findByAlertCode(String alertCode, Integer deploymentId);
}
