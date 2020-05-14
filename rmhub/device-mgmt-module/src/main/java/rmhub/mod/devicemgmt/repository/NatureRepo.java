package rmhub.mod.devicemgmt.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import rmhub.mod.devicemgmt.entity.Nature;

public interface NatureRepo extends JpaRepository<Nature, Long> {

  List<Nature> findByNameIgnoreCase(String name);

  Nature findByValue(String value);
}
