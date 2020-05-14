package rmhub.mod.devicemgmt.repository.specification;

import java.util.List;
import org.springframework.data.jpa.domain.Specification;
import rmhub.mod.devicemgmt.common.PhysicalDeviceConst;
import rmhub.mod.devicemgmt.entity.PhysicalDevice;

public interface PhysicalDeviceSpecification {

  static Specification<PhysicalDevice> getAllDevice(Long deviceType, Long deploymentId, List<Integer> status) {

    return Specification.where(deviceTypeEqual(deviceType)).and(deploymentIdEqual(deploymentId)).and(statusIn(status));
  }

  static Specification<PhysicalDevice> getByName(Long deviceTypeId, Long deploymentId, List<Integer> status, String deviceName) {
    return Specification.where(deviceTypeEqual(deviceTypeId)).and(deploymentIdEqual(deploymentId)).and(statusIn(status)).and(deviceNameLike(deviceName));
  }

  private static Specification<PhysicalDevice> deviceTypeEqual(Long deviceType) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(PhysicalDeviceConst.DEVICE_TYPE), deviceType);
  }

  private static Specification<PhysicalDevice> deploymentIdEqual(Long deploymentId) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(PhysicalDeviceConst.DEPLOYMENT_ID), deploymentId);
  }

  private static Specification<PhysicalDevice> statusIn(List<Integer> status) {
    return (root, query, criteriaBuilder) -> root.get(PhysicalDeviceConst.STATUS).in(status);
  }

  private static Specification<PhysicalDevice> deviceNameLike(String deviceName) {
    return (root, query, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.lower(root.get(PhysicalDeviceConst.NAME)), "%" + deviceName.toLowerCase() + "%");
  }
}
