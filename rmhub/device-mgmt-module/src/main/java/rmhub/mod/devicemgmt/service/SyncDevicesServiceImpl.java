package rmhub.mod.devicemgmt.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmhub.common.common.ErrorCode;
import rmhub.common.exception.BusinessException;
import rmhub.mod.devicemgmt.entity.Channel;
import rmhub.mod.devicemgmt.entity.ChannelMesureMapping;
import rmhub.mod.devicemgmt.entity.ChannelPhysicalDeviceMapping;
import rmhub.mod.devicemgmt.entity.DeviceType;
import rmhub.mod.devicemgmt.entity.MesureConfig;
import rmhub.mod.devicemgmt.entity.Nature;
import rmhub.mod.devicemgmt.entity.Period;
import rmhub.mod.devicemgmt.entity.PhysicalDevice;
import rmhub.mod.devicemgmt.entity.Pool;
import rmhub.mod.devicemgmt.repository.DeviceTypeRepo;

@Slf4j
@Service
class SyncDevicesServiceImpl implements SyncDevicesService {

  @Autowired
  private PhysicalDeviceService physicalDeviceService;

  @Autowired
  private DeviceTypeRepo deviceTypeRepo;

  @Autowired
  private PoolService poolService;

  @Autowired
  private ChannelService channelService;

  @Autowired
  private NatureService natureService;

  @Autowired
  private PeriodService periodService;

  @Autowired
  private ChannelPhysicalDeviceMappingService channelPhysicalDeviceMappingService;

  @Autowired
  private MesureConfigsService mesureConfigsService;

  @Autowired
  private ChannelMeasureMappingService channelMeasureMappingService;

  @Override
  public DeviceType getDeviceType(Long deviceTypeId) {
    return deviceTypeRepo.findById(deviceTypeId).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "DeviceType doesn't exist."));
  }

  @Override
  public void resetCurrentData() {
    poolService.deleteAll();

    channelService.deleteAll();

    natureService.deleteAll();

    periodService.deleteAll();

    channelPhysicalDeviceMappingService.deleteAll();

    mesureConfigsService.deleteAll();

    channelMeasureMappingService.deleteAll();
  }

  @Override
  public void resetPhysicalDeviceData(Long deviceTypeId) {
    physicalDeviceService.deleteAll(deviceTypeId);
  }

  @Override
  public PhysicalDevice syncPhysicalDevice(PhysicalDevice physicalDevice) {

    PhysicalDevice result = physicalDeviceService.findByExternalId(physicalDevice.getExternalId());

    if (result != null) {

      physicalDevice.setId(result.getId());
      physicalDevice.setIsRegistered(result.getIsRegistered());
      physicalDevice.setLongitude(result.getLongitude());
      physicalDevice.setLatitude(result.getLatitude());
    } else {
      NEW_DEVICES.add(physicalDevice.getExternalId());
    }

    return physicalDeviceService.createOrUpdate(physicalDevice);
  }

  @Override
  public Pool syncPool(Pool poolDto) {

    Pool pool = poolService.findByValue(poolDto.getValue());

    if (pool != null) {
      poolDto.setId(pool.getId());
    }

    if (pool != null && pool.getName() != null) {
      poolDto.setName(pool.getName());
    }

    return poolService.create(poolDto);
  }

  @Override
  public Channel syncChannel(Channel channelDto) {

    Channel channel = channelService.find(channelDto.getValue(), channelDto.getDeviceTypeId());

    if (channel != null) {
      channelDto.setId(channel.getId());
    }

    return channelService.create(channelDto);
  }

  @Override
  public ChannelPhysicalDeviceMapping syncChannelPhysicalDeviceMapping(ChannelPhysicalDeviceMapping channelPhysicalDeviceMappingDto) {

    ChannelPhysicalDeviceMapping channelPhysicalDeviceMapping = channelPhysicalDeviceMappingService
        .find(channelPhysicalDeviceMappingDto.getChannel().getId(), channelPhysicalDeviceMappingDto.getPhysicalDevice().getId());

    if (channelPhysicalDeviceMapping != null) {
      channelPhysicalDeviceMappingDto.setId(channelPhysicalDeviceMapping.getId());
    }

    return channelPhysicalDeviceMappingService.create(channelPhysicalDeviceMappingDto);
  }

  @Override
  public ChannelMesureMapping syncChannelMesureMapping(ChannelMesureMapping channelMesureMappingDto) {

    ChannelMesureMapping channelMesureMapping = channelMeasureMappingService.
        find(channelMesureMappingDto.getChannel().getId(), channelMesureMappingDto.getMesureConfig().getId());

    if (channelMesureMapping != null) {
      channelMesureMappingDto.setId(channelMesureMapping.getId());
    }

    return channelMeasureMappingService.create(channelMesureMappingDto);
  }

  @Override
  public Nature syncNature(Nature natureDto) {

    Nature nature = natureService.find(natureDto.getValue());

    if (nature != null) {
      natureDto.setId(nature.getId());
    }

    return natureService.create(natureDto);
  }

  @Override
  public Period syncPeriod(Period periodDto) {

    Period period = periodService.find(periodDto.getValue());

    if (period != null) {
      periodDto.setId(period.getId());
    }

    return periodService.create(periodDto);
  }

  @Override
  public MesureConfig syncMesureConfig(MesureConfig mesureConfigDto) {
    MesureConfig mesureConfig = mesureConfigsService.find(mesureConfigDto.getMesureId());

    if (mesureConfig != null) {
      mesureConfigDto.setId(mesureConfig.getId());
    }

    return mesureConfigsService.create(mesureConfigDto);
  }
}
