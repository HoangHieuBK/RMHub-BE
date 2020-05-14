package rmhub.mod.devicemgmt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rmhub.mod.devicemgmt.entity.DeviceType;

public interface DeviceTypeRepo extends JpaRepository<DeviceType, Long> {

}
