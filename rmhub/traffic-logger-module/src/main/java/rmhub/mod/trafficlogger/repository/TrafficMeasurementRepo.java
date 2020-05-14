package rmhub.mod.trafficlogger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rmhub.mod.trafficlogger.entity.TrafficMeasurement;

public interface TrafficMeasurementRepo extends JpaRepository<TrafficMeasurement, Long> {

}
