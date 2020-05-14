package rmhub.mod.devicemgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rmhub.mod.devicemgmt.entity.Period;

public interface PeriodRepo extends JpaRepository<Period, Long> {

//  List<Period> findByNameIgnoreCase(String name);

  Period findByValue(String value);
}
