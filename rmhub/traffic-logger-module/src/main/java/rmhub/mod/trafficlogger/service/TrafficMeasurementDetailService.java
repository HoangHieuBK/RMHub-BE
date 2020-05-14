package rmhub.mod.trafficlogger.service;

import java.util.List;
import rmhub.mod.trafficlogger.entity.TrafficMeasurementDetail;

public interface TrafficMeasurementDetailService {

  TrafficMeasurementDetail create(TrafficMeasurementDetail tlMeasurementDetail);

  TrafficMeasurementDetail update(TrafficMeasurementDetail tlMeasurementDetail);

  void delete(Long id);

  List<TrafficMeasurementDetail> findAll();

  TrafficMeasurementDetail findById(Long id);
}
