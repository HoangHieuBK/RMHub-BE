package rmhub.mod.trafficlogger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rmhub.mod.trafficlogger.entity.TrafficAlert;

public interface TrafficAlertRepo extends JpaRepository<TrafficAlert, Long> {

}
