package rmhub.mod.devicemgmt.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmhub.common.common.ErrorCode;
import rmhub.common.exception.BusinessException;
import rmhub.mod.devicemgmt.entity.DeviceType;
import rmhub.mod.devicemgmt.repository.DeviceTypeRepo;

@Service
class DeviceTypeServiceImpl implements DeviceTypeService {

  @Autowired
  private DeviceTypeRepo deviceTypeRepo;

  @Override
  public DeviceType create(DeviceType deviceType) {
    return deviceTypeRepo.save(deviceType);
  }

  @Override
  public DeviceType update(DeviceType deviceType) {
    return deviceTypeRepo.save(deviceType);
  }

  @Override
  public void delete(Long id) {
    deviceTypeRepo.deleteById(id);
  }

  @Override
  public List<DeviceType> findAll() {
    return deviceTypeRepo.findAll();
  }

  @Override
  public DeviceType findById(Long id) {
    return deviceTypeRepo.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Device doesn't exist."));
  }
}
