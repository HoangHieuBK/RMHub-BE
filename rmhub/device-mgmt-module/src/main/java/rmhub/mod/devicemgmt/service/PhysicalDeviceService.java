package rmhub.mod.devicemgmt.service;

import java.util.List;
import java.util.Optional;
import rmhub.mod.devicemgmt.dto.LocationInfo;
import rmhub.mod.devicemgmt.dto.PhysicalDeviceResponse;
import rmhub.mod.devicemgmt.dto.ResponseDeviceInfo;
import rmhub.mod.devicemgmt.entity.PhysicalDevice;

public interface PhysicalDeviceService {

  List<PhysicalDeviceResponse> find(Long deviceType, Long deploymentId);

  List<PhysicalDeviceResponse> find(Long deploymentId, String name, Integer status, Long deviceTypeId);

  /**
   * Find a device which is either Active or Inactive
   */
  PhysicalDeviceResponse findById(Long id);

  PhysicalDevice findByExternalId(String externalId);

  List<ResponseDeviceInfo> findByDeploymentId(Long deploymentId);

  List<ResponseDeviceInfo> findByDeviceType(Long deploymentId, Long deviceType);

  List<PhysicalDeviceResponse> findByName(Long deviceTypeId, Long deploymentId, String deviceName);

  PhysicalDevice create(PhysicalDevice physicalDevice);

  PhysicalDevice createOrUpdate(PhysicalDevice physicalDevice);

  PhysicalDeviceResponse setLocation(Long id, LocationInfo locationDto);

  PhysicalDeviceResponse removeLocation(Long deviceId);

  void deleteAll(Long deviceTypeId);

  void sync(Long deviceType, String requestId, Long deploymentId);

  Optional<String> checkDuplicatedLocation(Long id, LocationInfo dto);
}
