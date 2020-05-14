package rmhub.mod.weatherstation.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmhub.mod.weatherstation.entity.WeatherMeasurement;
import rmhub.mod.weatherstation.repository.WSMeasurementRepo;

@Service
class WSMeasurementServiceImpl implements WSMeasurementService {

  @Autowired
  private WSMeasurementRepo wsMeasurementRepo;

  @Override
  public WeatherMeasurement create(WeatherMeasurement weatherMeasurement) {
    return wsMeasurementRepo.save(weatherMeasurement);
  }

  @Override
  public WeatherMeasurement update(WeatherMeasurement weatherMeasurement) {
    return wsMeasurementRepo.save(weatherMeasurement);
  }

  @Override
  public void delete(Long id) {
    wsMeasurementRepo.deleteById(id);
  }

  @Override
  public List<WeatherMeasurement> findAll() {
    return wsMeasurementRepo.findAll();
  }

  @Override
  public WeatherMeasurement findById(Long id) {
    return wsMeasurementRepo.findById(id).get();
  }
}
