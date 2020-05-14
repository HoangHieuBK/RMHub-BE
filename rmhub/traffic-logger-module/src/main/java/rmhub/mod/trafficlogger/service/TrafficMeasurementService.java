package rmhub.mod.trafficlogger.service;

import java.util.List;
import rmhub.mod.trafficlogger.entity.TrafficMeasurement;

public interface TrafficMeasurementService {

  TrafficMeasurement create(TrafficMeasurement tlMeasurement);

  TrafficMeasurement update(TrafficMeasurement tlMeasurement);

  void delete(Long id);

  List<TrafficMeasurement> findAll();

  TrafficMeasurement findById(Long id);
}
