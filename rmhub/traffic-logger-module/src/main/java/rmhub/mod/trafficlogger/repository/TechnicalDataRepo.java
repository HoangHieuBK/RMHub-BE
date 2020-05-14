package rmhub.mod.trafficlogger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rmhub.mod.trafficlogger.entity.TechnicalData;

public interface TechnicalDataRepo extends JpaRepository<TechnicalData, Long> {

}
