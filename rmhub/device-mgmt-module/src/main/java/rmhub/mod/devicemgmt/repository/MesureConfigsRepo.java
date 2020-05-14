package rmhub.mod.devicemgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rmhub.mod.devicemgmt.entity.MesureConfig;

public interface MesureConfigsRepo extends JpaRepository<MesureConfig, Long> {

  MesureConfig findByMesureId(String mesureId);
}
