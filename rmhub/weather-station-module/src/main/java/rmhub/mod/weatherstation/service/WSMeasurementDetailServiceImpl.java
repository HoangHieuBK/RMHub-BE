package rmhub.mod.weatherstation.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmhub.mod.weatherstation.entity.WeatherMeasurementDetail;
import rmhub.mod.weatherstation.repository.WSMeasurementDetailRepo;

@Service
class WSMeasurementDetailServiceImpl implements WSMeasurementDetailService {

  @Autowired
  private WSMeasurementDetailRepo wsMeasurementDetailRepo;

  @Override
  public WeatherMeasurementDetail create(WeatherMeasurementDetail weatherMeasurementDetail) {
    return wsMeasurementDetailRepo.save(weatherMeasurementDetail);
  }

  @Override
  public WeatherMeasurementDetail update(WeatherMeasurementDetail weatherMeasurementDetail) {
    return wsMeasurementDetailRepo.save(weatherMeasurementDetail);
  }

  @Override
  public void delete(Long id) {
    wsMeasurementDetailRepo.deleteById(id);
  }

  @Override
  public List<WeatherMeasurementDetail> findAll() {
    return wsMeasurementDetailRepo.findAll();
  }

  @Override
  public WeatherMeasurementDetail findById(Long id) {
    return wsMeasurementDetailRepo.findById(id).get();
  }
}
