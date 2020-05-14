package rmhub.mod.devicemgmt.service;

import static rmhub.mod.devicemgmt.common.StatusEnum.DELETE;
import static rmhub.mod.devicemgmt.common.StatusEnum.INACTIVE;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Proxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import rmhub.common.common.ErrorCode;
import rmhub.common.constant.MivisuMessageConstant;
import rmhub.common.exception.BusinessException;
import rmhub.common.kafkaconnector.messagebased.KafkaProducible;
import rmhub.common.utility.JsonHelper;
import rmhub.mod.devicemgmt.common.DeviceTypeEnum;
import rmhub.mod.devicemgmt.common.PhysicalDeviceConst;
import rmhub.mod.devicemgmt.common.StatusEnum;
import rmhub.mod.devicemgmt.dto.DevicePoolInfo;
import rmhub.mod.devicemgmt.dto.LocationInfo;
import rmhub.mod.devicemgmt.dto.PhysicalDeviceResponse;
import rmhub.mod.devicemgmt.dto.PoolInfo;
import rmhub.mod.devicemgmt.dto.ResponseDeviceInfo;
import rmhub.mod.devicemgmt.entity.DeviceType;
import rmhub.mod.devicemgmt.entity.PhysicalDevice;
import rmhub.mod.devicemgmt.repository.PhysicalDeviceRepo;
import rmhub.mod.devicemgmt.repository.specification.PhysicalDeviceSpecification;

@Service
@Slf4j
@Transactional
@Proxy(lazy = false)
class PhysicalDeviceServiceImpl implements PhysicalDeviceService {

  @Value("${rmhub.mivisu.topic.request.device}")
  private String requestDevice;

  @Autowired
  private KafkaProducible<String, String> kafkaProducible;

  @Autowired
  private PhysicalDeviceRepo physicalDeviceRepo;

  @Override
  public List<PhysicalDeviceResponse> find(Long deviceType, Long deploymentId) {

    Sort sort = Sort.by(Sort.Order.desc(PhysicalDeviceConst.LAST_MODIFIED_DATE), Sort.Order.asc(PhysicalDeviceConst.NAME),
        Sort.Order.desc(PhysicalDeviceConst.IS_REGISTERED), Sort.Order.desc(PhysicalDeviceConst.STATUS));

    List<PhysicalDevice> physicalDevices = physicalDeviceRepo.findAll(
        PhysicalDeviceSpecification.getAllDevice(deviceType, deploymentId, PhysicalDeviceConst.VALID_STATUS), sort);

    return extractPhysicalDevice(physicalDevices);
  }

  private List<PhysicalDeviceResponse> extractPhysicalDevice(List<PhysicalDevice> physicalDevices) {

    return physicalDevices.stream().map(PhysicalDeviceResponse::new).collect(Collectors.toList());
  }

  public PhysicalDeviceResponse mapperOnePhysicalDevice(PhysicalDevice physicalDevice) {

    return new PhysicalDeviceResponse(physicalDevice);
  }


  @Override
  public List<PhysicalDeviceResponse> findByName(Long deviceTypeId, Long deploymentId, String deviceName) {

    Sort sort = Sort.by(Sort.Order.asc(PhysicalDeviceConst.NAME));

    var specification = PhysicalDeviceSpecification.getByName(deviceTypeId, deploymentId, PhysicalDeviceConst.VALID_STATUS, deviceName);

    List<PhysicalDevice> physicalDevices = physicalDeviceRepo.findAll(specification, sort);

    return extractPhysicalDevice(physicalDevices);
  }

  private List<PhysicalDeviceResponse> getByName(PhysicalDevice physicalDevice, ExampleMatcher exampleMatcher) {

    Example<PhysicalDevice> example = Example.of(physicalDevice, exampleMatcher);

    List<PhysicalDevice> physicalDevices = physicalDeviceRepo.findAll(example, Sort.by(PhysicalDeviceConst.NAME).ascending());

    return extractPhysicalDevice(physicalDevices);
  }

  @Override
  public void sync(Long deviceType, String requestId, Long deploymentId) {

    String type = "";

    if (deviceType.equals(DeviceTypeEnum.WEATHER_STATION.getValue())) {
      type = MivisuMessageConstant.EQT_WS_TYPE;
    }

    if (deviceType.equals(DeviceTypeEnum.TRAFFIC_COUNTING.getValue())) {
      type = MivisuMessageConstant.EQT_TC_TYPE;
    }

    Map<String, String> requestParams = new HashMap<>();
    requestParams.put("deviceType", type);
    requestParams.put("requestId", requestId);
    requestParams.put("deploymentId", deploymentId.toString());

    String json = JsonHelper.convertObject2JsonNoWrapRoot(requestParams);
    kafkaProducible.send(requestDevice, json);
  }

  @Override
  public PhysicalDevice create(PhysicalDevice physicalDevice) {
    return physicalDeviceRepo.save(physicalDevice);
  }

  @Override
  public PhysicalDevice createOrUpdate(PhysicalDevice physicalDevice) {
    return physicalDeviceRepo.save(physicalDevice);
  }

  @Override
  public List<ResponseDeviceInfo> findByDeviceType(Long deploymentId, Long deviceType) {
    List<DevicePoolInfo> deviceAndPool =
        physicalDeviceRepo.findAllDevicesByDeviceType(deploymentId, deviceType);
    return buildDevicesInfo(deviceAndPool);
  }

  @Override
  public List<ResponseDeviceInfo> findByDeploymentId(Long deploymentId) {
    List<DevicePoolInfo> listDevices =
        physicalDeviceRepo.findAllDevicesByDeploymentId(deploymentId);
    return buildDevicesInfo(listDevices);
  }


  private List<ResponseDeviceInfo> buildDevicesInfo(List<DevicePoolInfo> deviceAndPool) {
    List<ResponseDeviceInfo> result = new ArrayList<>();
    List<Integer> positions = new ArrayList<>();
    int size = deviceAndPool.size();
    for (int i = 0; i < size; i++) {
      DevicePoolInfo device1 = deviceAndPool.get(i);
      List<PoolInfo> pools = new ArrayList<>();
      PoolInfo poolInfo1 = new PoolInfo(device1.getPoolName(), device1.getPoolValue());
      pools.add(poolInfo1);

      for (int j = i + 1; j < size; j++) {
        DevicePoolInfo device2 = deviceAndPool.get(j);
        if (device1.getExternalId() != null
            && device1.getExternalId().equals(device2.getExternalId())) {
          PoolInfo poolInfo2 = new PoolInfo(device2.getPoolName(), device2.getPoolValue());
          pools.add(poolInfo2);
          positions.add(j);
        }
      }

      if (!contains(positions, i)) {
        ResponseDeviceInfo responseDeviceInfo =
            new ResponseDeviceInfo(device1.getDeviceTypeId(), device1.getId(), device1.getEqt_actif(),
                device1.getExternalId(), device1.getDeviceName(), device1.getDescription(),
                device1.getLastUpdate(), device1.getLatitude(), device1.getLongitude(), pools);
        responseDeviceInfo.setPools(pools);
        result.add(responseDeviceInfo);
      }
    }
    return result;
  }

  private boolean contains(final List<Integer> array, final int v) {

    boolean result = false;

    for (int i : array) {
      if (i == v) {
        result = true;
        break;
      }
    }

    return result;
  }

  @Override
  public PhysicalDeviceResponse removeLocation(Long deviceId) {
    PhysicalDevice physicalDevice = get(deviceId, true);
    physicalDevice.setIsRegistered(false);
    physicalDevice.setLatitude(null);
    physicalDevice.setLongitude(null);
    return mapperOnePhysicalDevice(physicalDeviceRepo.saveAndFlush(physicalDevice));
  }

  @Override
  public void deleteAll(Long deviceTypeId) {
    List<PhysicalDevice> physicalDevices = physicalDeviceRepo.findAllByDeviceType_id(deviceTypeId);

    for (PhysicalDevice physicalDevice : physicalDevices) {
      if (physicalDevice.getStatus().longValue() == DELETE.getValue()) {
        physicalDevice.setIsRegistered(false);
        physicalDevice.setLongitude(null);
        physicalDevice.setLatitude(null);
      }
      physicalDevice.setStatus(StatusEnum.DELETE.getValue());
    }

    physicalDeviceRepo.saveAll(physicalDevices);
  }

  @Override
  public PhysicalDevice findByExternalId(String externalId) {
    return physicalDeviceRepo.findByExternalId(externalId);
  }

  @Override
  public PhysicalDeviceResponse findById(Long id) {
    return mapperOnePhysicalDevice(get(id, true));
  }

  @Override
  public PhysicalDeviceResponse setLocation(Long id, LocationInfo locationDto) {

    var physicalDevice = get(id, false);
    physicalDevice.setLatitude(locationDto.getLatitude());
    physicalDevice.setLongitude(locationDto.getLongitude());
    physicalDevice.setIsRegistered(true);

    return mapperOnePhysicalDevice(physicalDeviceRepo.saveAndFlush(physicalDevice));
  }

  @Override
  public List<PhysicalDeviceResponse> find(Long deploymentId, String name, Integer status, Long deviceTypeId) {

    PhysicalDevice physicalDevice = new PhysicalDevice();

    physicalDevice.setDeploymentId(deploymentId);
    physicalDevice.setIsRegistered(null);

    if (!StringUtils.isEmpty(name)) {
      physicalDevice.setName(name.toLowerCase());
    }

    if (!StringUtils.isEmpty(status)) {
      physicalDevice.setStatus(status);
    }

    if (!StringUtils.isEmpty(deviceTypeId)) {
      DeviceType deviceBuilder = new DeviceType(deviceTypeId);

      physicalDevice.setDeviceType(deviceBuilder);
    }

    ExampleMatcher exampleMatcher = ExampleMatcher.matching()
        .withMatcher(PhysicalDeviceConst.NAME, matcher -> matcher.contains().ignoreCase());

    return getByName(physicalDevice, exampleMatcher);
  }

  private PhysicalDevice get(Long id, boolean allowInactive) {
    Optional<PhysicalDevice> physicalDevice = physicalDeviceRepo.findById(id);

    if (!allowInactive && physicalDevice.isPresent() && INACTIVE.getValue().equals(physicalDevice.get().getStatus())) {
      throw new BusinessException(ErrorCode.NOT_FOUND, "Device is INACTIVE.");
    }

    if (physicalDevice.isPresent() && DELETE.getValue().equals(physicalDevice.get().getStatus())) {
      throw new BusinessException(ErrorCode.NOT_FOUND, "Device is DELETED.");
    }

    if (physicalDevice.isEmpty()) {
      throw new BusinessException(ErrorCode.NOT_FOUND, "Device doesn't exist.");
    }

    return physicalDevice.get();
  }

  /**
   * Check if the location is duplicated
   *
   * @return device name that is duplicated, empty otherwise
   */
  public Optional<String> checkDuplicatedLocation(Long id, LocationInfo dto) {

    // the physical device to be checked
    var physicalDevice = physicalDeviceRepo.getOne(id);

    // we only need to perform duplication check on devices with the same device type
    var optionalDuplicatedDevice = physicalDeviceRepo
        .findByLatitudeAndLongitudeAndStatusNotAndDeviceType_Id(dto.getLatitude(), dto.getLongitude(), DELETE.getValue(),
            physicalDevice.getDeviceType().getId());

    if (optionalDuplicatedDevice.isEmpty()) {
      // in case no device with the same location found
      return Optional.empty();
    }

    var duplicatedDevice = optionalDuplicatedDevice.get();

    if (!duplicatedDevice.getId().equals(id)) {
      // in case found device with different id, we return it's name as addition information
      return Optional.of(duplicatedDevice.getName());
    }

    // otherwise we consider the location is not duplicated
    return Optional.empty();
  }
}
