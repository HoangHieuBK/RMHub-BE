package rmhub.mod.devicemgmt.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import rmhub.mod.devicemgmt.entity.Pool;

public interface PoolRepo extends JpaRepository<Pool, Long> {

  List<Pool> findByNameIgnoreCase(String name);

  Pool findByValue(String value);
}
