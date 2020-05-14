package rmhub.mod.weatherstation.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmhub.mod.weatherstation.entity.WeatherAlert;
import rmhub.mod.weatherstation.repository.WeatherAlertDataRepo;

@Service
class WeatherAlertServiceImpl implements WeatherAlertService {

  @Autowired
  private WeatherAlertDataRepo weatherAlertDataRepo;

  @Override
  public WeatherAlert create(WeatherAlert weatherAlert) {
    return weatherAlertDataRepo.save(weatherAlert);
  }

  @Override
  public WeatherAlert update(WeatherAlert weatherAlert) {
    return weatherAlertDataRepo.save(weatherAlert);
  }

  @Override
  public void delete(Long id) {
    weatherAlertDataRepo.deleteById(id);
  }

  @Override
  public List<WeatherAlert> findAll() {
    return weatherAlertDataRepo.findAll();
  }

  @Override
  public WeatherAlert findById(Long id) {
    // FIXME handle Not Found Exception
    return weatherAlertDataRepo.findById(id).get();
  }
}
