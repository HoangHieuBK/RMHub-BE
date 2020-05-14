package rmhub.mod.devicemgmt.service;

import java.util.List;
import rmhub.mod.devicemgmt.entity.DeviceType;

public interface DeviceTypeService {

  DeviceType create(DeviceType deviceType);

  DeviceType update(DeviceType deviceType);

  void delete(Long id);

  List<DeviceType> findAll();

  DeviceType findById(Long id);
}
