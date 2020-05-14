package rmhub.mod.trafficlogger.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmhub.mod.trafficlogger.entity.TrafficMeasurement;
import rmhub.mod.trafficlogger.repository.TrafficMeasurementRepo;

@Service
class TrafficMeasurementServiceImpl implements TrafficMeasurementService {

  private final TrafficMeasurementRepo repository;

  @Autowired
  public TrafficMeasurementServiceImpl(TrafficMeasurementRepo repository) {
    this.repository = repository;
  }

  @Override
  public TrafficMeasurement create(TrafficMeasurement tlMeasurement) {
    return repository.save(tlMeasurement);
  }

  @Override
  public TrafficMeasurement update(TrafficMeasurement tlMeasurement) {
    return repository.save(tlMeasurement);
  }

  @Override
  public void delete(Long id) {
    repository.deleteById(id);
  }

  @Override
  public List<TrafficMeasurement> findAll() {
    return repository.findAll();
  }

  @Override
  public TrafficMeasurement findById(Long id) {
    return repository.findById(id).get();
  }
}
