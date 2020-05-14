package rmhub.mod.trafficlogger.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmhub.mod.trafficlogger.entity.TrafficMeasurementDetail;
import rmhub.mod.trafficlogger.repository.TrafficMeasurementDetailRepo;

@Service
class TrafficMeasurementDetailServiceImpl implements TrafficMeasurementDetailService {

  private final TrafficMeasurementDetailRepo repository;

  @Autowired
  public TrafficMeasurementDetailServiceImpl(TrafficMeasurementDetailRepo repository) {
    this.repository = repository;
  }

  @Override
  public TrafficMeasurementDetail create(TrafficMeasurementDetail tlMeasurementDetail) {
    return repository.save(tlMeasurementDetail);
  }

  @Override
  public TrafficMeasurementDetail update(TrafficMeasurementDetail tlMeasurementDetail) {
    return repository.save(tlMeasurementDetail);
  }

  @Override
  public void delete(Long id) {
    repository.deleteById(id);
  }

  @Override
  public List<TrafficMeasurementDetail> findAll() {
    return repository.findAll();
  }

  @Override
  public TrafficMeasurementDetail findById(Long id) {
    return repository.findById(id).get();
  }
}
