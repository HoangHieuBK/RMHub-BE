package rmhub.mod.devicemgmt.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import rmhub.mod.devicemgmt.dto.DevicePoolInfo;
import rmhub.mod.devicemgmt.entity.PhysicalDevice;

public interface PhysicalDeviceRepo extends JpaRepository<PhysicalDevice, Long>, JpaSpecificationExecutor<PhysicalDevice> {

  PhysicalDevice findByExternalId(String externalId);

  List<PhysicalDevice> findAllByDeviceType_id(Long deviceType);

  @Query(
      "SELECT NEW rmhub.mod.devicemgmt.dto.DevicePoolInfo(dt.id, p.id, p.status, p.externalId, p.name as deviceName, p.description, p.lastModifiedDate, p.latitude, p.longitude, po.name as poolName, SUBSTRING(po.value, 2) as poolValue) "
          + "FROM PhysicalDevice p LEFT JOIN p.deviceType dt LEFT JOIN p.channelPhysicalDeviceMapping"
          + " cp LEFT JOIN cp.channel cl LEFT JOIN cl.pool po WHERE p.status = 1 AND p.isRegistered = TRUE AND p.deploymentId = ?1"
          + " AND dt.id = ?2 GROUP BY dt.id, p.id, p.status, p.externalId, p.name, p.description, p.lastModifiedDate, p.latitude, p.longitude, po.name, po.value")
  List<DevicePoolInfo> findAllDevicesByDeviceType(Long deploymentId, Long deviceType);

  @Query(
      "SELECT NEW rmhub.mod.devicemgmt.dto.DevicePoolInfo(dt.id, p.id, p.status, p.externalId, p.name as deviceName, p.description, p.lastModifiedDate, p.latitude, p.longitude, po.name as poolName, SUBSTRING(po.value, 2) as poolValue) "
          + "FROM PhysicalDevice p LEFT JOIN p.deviceType dt LEFT JOIN p.channelPhysicalDeviceMapping"
          + " cp LEFT JOIN cp.channel cl LEFT JOIN cl.pool po WHERE p.status = 1 AND p.isRegistered = TRUE AND p.deploymentId = ?1"
          + " GROUP BY dt.id, p.id, p.status, p.externalId, p.name, p.description, p.lastModifiedDate, p.latitude, p.longitude, po.name, po.value")
  List<DevicePoolInfo> findAllDevicesByDeploymentId(Long deploymentId);

  Optional<PhysicalDevice> findByLatitudeAndLongitudeAndStatusNotAndDeviceType_Id(
      Double latitude, Double longitude, Integer status, Long deviceTypeId);
}
