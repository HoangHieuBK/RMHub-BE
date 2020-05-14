package rmhub.mod.trafficlogger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rmhub.mod.trafficlogger.entity.TrafficMeasurementDetail;

public interface TrafficMeasurementDetailRepo extends JpaRepository<TrafficMeasurementDetail, Long> {

}
