package rmhub.mod.devicemgmt.service;

import java.util.ArrayList;
import java.util.List;
import rmhub.mod.devicemgmt.entity.Channel;
import rmhub.mod.devicemgmt.entity.ChannelMesureMapping;
import rmhub.mod.devicemgmt.entity.ChannelPhysicalDeviceMapping;
import rmhub.mod.devicemgmt.entity.DeviceType;
import rmhub.mod.devicemgmt.entity.MesureConfig;
import rmhub.mod.devicemgmt.entity.Nature;
import rmhub.mod.devicemgmt.entity.Period;
import rmhub.mod.devicemgmt.entity.PhysicalDevice;
import rmhub.mod.devicemgmt.entity.Pool;

public interface SyncDevicesService {

  List<String> NEW_DEVICES = new ArrayList<>();

  DeviceType getDeviceType(Long deviceTypeId);

  void resetCurrentData();

  void resetPhysicalDeviceData(Long deviceTypeId);

  PhysicalDevice syncPhysicalDevice(PhysicalDevice physicalDeviceDto);

  Pool syncPool(Pool poolDto);

  Channel syncChannel(Channel channelDto);

  ChannelPhysicalDeviceMapping syncChannelPhysicalDeviceMapping(ChannelPhysicalDeviceMapping channelPhysicalDeviceMappingDto);

  ChannelMesureMapping syncChannelMesureMapping(ChannelMesureMapping channelMesureMappingDto);

  Nature syncNature(Nature natureDto);

  Period syncPeriod(Period periodDto);

  MesureConfig syncMesureConfig(MesureConfig mesureConfigDto);
}
