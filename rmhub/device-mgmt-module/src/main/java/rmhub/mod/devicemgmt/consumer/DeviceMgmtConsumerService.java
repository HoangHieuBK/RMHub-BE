package rmhub.mod.devicemgmt.consumer;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Proxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import rmhub.common.constant.MivisuMessageConstant;
import rmhub.common.core.response.CmdBaseResult;
import rmhub.common.core.response.PhysicalDeviceResult;
import rmhub.common.kafkaconnector.messagebased.KafkaProducible;
import rmhub.common.utility.JsonHelper;
import rmhub.common.utility.ParserHelper;
import rmhub.mod.devicemgmt.common.DeviceTypeEnum;
import rmhub.mod.devicemgmt.common.StatusEnum;
import rmhub.mod.devicemgmt.dto.PhysicalDeviceResponse;
import rmhub.mod.devicemgmt.entity.Channel;
import rmhub.mod.devicemgmt.entity.ChannelMesureMapping;
import rmhub.mod.devicemgmt.entity.ChannelPhysicalDeviceMapping;
import rmhub.mod.devicemgmt.entity.DeviceType;
import rmhub.mod.devicemgmt.entity.MesureConfig;
import rmhub.mod.devicemgmt.entity.Nature;
import rmhub.mod.devicemgmt.entity.Period;
import rmhub.mod.devicemgmt.entity.PhysicalDevice;
import rmhub.mod.devicemgmt.entity.Pool;
import rmhub.mod.devicemgmt.service.PhysicalDeviceService;
import rmhub.mod.devicemgmt.service.SyncDevicesService;
import rmhub.model.mivisu.api.InfoGeneric;
import rmhub.model.mivisu.api.InfoGetIgx;
import rmhub.model.mivisu.ssil.AbstractMsgBody;
import rmhub.model.mivisu.ssil.AllDataMsgBody;
import rmhub.model.mivisu.ssil.SSILMessage;
import rmhub.model.mivisu.ssil.SSILMessageHeader;

@Service
@Slf4j
@Proxy(lazy = false)
public class DeviceMgmtConsumerService {

  @Autowired
  private SyncDevicesService syncDevicesService;

  @Autowired
  private PhysicalDeviceService physicalDeviceService;

  @Autowired
  private KafkaProducible<String, String> kafkaProducible;

  @Value("${rmhub.mivisu.topic.sync-devices}")
  private String syncDevices;

  @Value("${rmhub.mivisu.ssilm.topic.request}")
  private String requestMivisu;

  @Value("${rmhub.mivisu.topic.request.device}")
  private String requestDevice;

  @KafkaListener(id = "device-management", topics = "${rmhub.mivisu.topic.request.device}")
  public void listenRequestSync(String msg) {

    syncDevicesService.resetCurrentData();
  }

  @KafkaListener(topics = "${rmhub.mivisu.configuration.change}")
  public void listenConfigurationChange(String msg) {

    log.info("Message configuration change {}", msg);

    Map<String, String> requestParams = new HashMap<>();
    requestParams.put("deviceType", MivisuMessageConstant.EQT_ALL_TYPE);
    requestParams.put("requestId", "");
    requestParams.put("deploymentId", "1");

    String json = JsonHelper.convertObject2JsonNoWrapRoot(requestParams);
    kafkaProducible.send(requestDevice, json);
  }

  @KafkaListener(topics = "${rmhub.mivisu.topic.traffic.device}")
  public void listenRequestSyncTraffic(String msg) {
    syncDevicesService.resetPhysicalDeviceData(DeviceTypeEnum.TRAFFIC_COUNTING.getValue());
    InfoGetIgx deviceInfo = JsonHelper.convertJson2Object(msg, InfoGetIgx.class);

    if (deviceInfo == null) {
      log.info("No traffic device to be synced.");
      return;
    }

    try {
      sync(deviceInfo, DeviceTypeEnum.TRAFFIC_COUNTING, deviceInfo.getDeploymentId());

      getDeviceAfterSync(deviceInfo.getRequestId(), deviceInfo.getDeploymentId(), DeviceTypeEnum.TRAFFIC_COUNTING.getValue());

      // send new devices to kafka request.mivisu.ssilm
      SyncDevicesService.NEW_DEVICES.forEach(device -> getDeviceTechnicalData(deviceInfo.getRequestId(), device));

      log.info("Number of device(s) to be synced: {}", SyncDevicesService.NEW_DEVICES.size());

      // reset list new devices
      SyncDevicesService.NEW_DEVICES.clear();

    } catch (Exception ex) {
      log.error("Error when processing traffic device info from connect api: {}", ex.getMessage());
      responseSyncError(deviceInfo.getRequestId(), deviceInfo.getDeploymentId());
    }
  }

  @KafkaListener(topics = "${rmhub.mivisu.topic.weather.device}")
  public void listenRequestSyncWeather(String msg) {
    syncDevicesService.resetPhysicalDeviceData(DeviceTypeEnum.WEATHER_STATION.getValue());
    InfoGetIgx deviceInfo = JsonHelper.convertJson2Object(msg, InfoGetIgx.class);

    if (deviceInfo == null) {
      log.info("No weather device to be synced.");
      return;
    }

    try {
      sync(deviceInfo, DeviceTypeEnum.WEATHER_STATION, deviceInfo.getDeploymentId());

      getDeviceAfterSync(deviceInfo.getRequestId(), deviceInfo.getDeploymentId(), DeviceTypeEnum.WEATHER_STATION.getValue());

    } catch (Exception ex) {
      log.error("Error when processing weather device info from connect api: {}", ex.getMessage());
      responseSyncError(deviceInfo.getRequestId(), deviceInfo.getDeploymentId());
    }
  }

  /**
   * Sync all device into RmHub database
   *
   * @param data received from Mivisu
   */
  private void sync(InfoGetIgx data, DeviceTypeEnum type, Long deploymentId) {

    if (log.isDebugEnabled()) {
      log.debug("processing Device syncing...");
    }

    if (data.getInfoGenerics() != null && data.getInfoGenerics().size() > 0) {

      //Extract device configuration and save configs
      data.getInfoGenerics().forEach(infoGeneric -> syncDevice(infoGeneric, type, deploymentId));
    }
  }

  /**
   * Sync each device
   *
   * @param infoPhysic device information
   */
  private void syncDevice(InfoGeneric infoPhysic, DeviceTypeEnum deviceType, Long deploymentId) {

    // FIXME need to handle exception
    DeviceType deviceTypeEntity = syncDevicesService.getDeviceType(deviceType.getValue());
    String description = null;
    String externalId = infoPhysic.getId().getValue();
//    String name = infoPhysic.getTitre();
    String statusTmp = null;
    if (infoPhysic.getCfg() != null) {
      statusTmp = infoPhysic.getCfg().getEqt_actif().substring(0, 1);
      description = infoPhysic.getCfg().getDescription();
    }
    int status = ParserHelper.convertString2Int(statusTmp);
    String context = infoPhysic.getContexte().getValue();
    var physicalDevice = PhysicalDevice.builder()
        .deviceType(deviceTypeEntity)
        .externalId(externalId)
        .name(externalId)
        .description(description)
        .status(status)
        .context(context)
        .deploymentId(deploymentId)
        .build();

    physicalDevice.setAtKilometers(physicalDevice.extractKM());
    physicalDevice.setAtMeters(physicalDevice.extractMeter());

    PhysicalDevice syncedPhysicalDevice = syncDevicesService.syncPhysicalDevice(physicalDevice);

    // call stream api to process list of Pools
    infoPhysic.getCfg().getMesure().getPools().forEach(poolDto -> {
      Pool pool = new Pool(poolDto.get_text(), poolDto.getValue(), StatusEnum.ACTIVE.getValue());
      Pool syncedPool = syncDevicesService.syncPool(pool);

      // call stream api to process list of Channels
      poolDto.getChannels().forEach(channelDto -> {
        var channel = Channel.builder()
            .pool(syncedPool)
            .name(channelDto.get_text())
            .value(channelDto.getValue())
            .status(StatusEnum.ACTIVE.getValue())
            .deviceTypeId(deviceTypeEntity.getId())
            .build();
        Channel syncedChannel = syncDevicesService.syncChannel(channel);

        var channelPhysicalDeviceMapping = ChannelPhysicalDeviceMapping.builder()
            .channel(syncedChannel)
            .physicalDevice(syncedPhysicalDevice)
            .status(StatusEnum.ACTIVE.getValue())
            .build();
        syncDevicesService.syncChannelPhysicalDeviceMapping(channelPhysicalDeviceMapping);

        // call stream api to process list of Natures
        channelDto.getNatures().forEach(natureDto -> {
          Nature nature = new Nature(natureDto.getValue(), natureDto.getValue(), StatusEnum.ACTIVE.getValue());
          Nature syncedNature = syncDevicesService.syncNature(nature);

          // call stream api to process list of Periods
          natureDto.getPeriods().forEach(periodDto -> {

            Period entity = new Period(periodDto.getValue(), periodDto.getValue(), StatusEnum.ACTIVE.getValue());
            Period syncedPeriod = syncDevicesService.syncPeriod(entity);

            MesureConfig mesureConfig = new MesureConfig(periodDto, syncedPeriod.getId(), syncedNature.getId(),
                StatusEnum.ACTIVE.getValue(), true);
            syncDevicesService.syncMesureConfig(mesureConfig);

            var channelMesureMapping = ChannelMesureMapping.builder()
                .channel(syncedChannel)
                .mesureConfig(mesureConfig)
                .status(StatusEnum.ACTIVE.getValue())
                .build();
            syncDevicesService.syncChannelMesureMapping(channelMesureMapping);
          });
        });
      });
    });

    if (log.isDebugEnabled()) {
      log.debug("Device syncing is successful!");
    }
  }

  private void getDeviceAfterSync(String requestId, Long deploymentId, Long deviceType) {

    List<PhysicalDeviceResponse> physicalDeviceInfos = physicalDeviceService.find(deviceType, deploymentId);

    CmdBaseResult cmdBaseResult = new PhysicalDeviceResult(physicalDeviceInfos);
    cmdBaseResult.setRequestId(requestId);
    cmdBaseResult.setDeploymentId(deploymentId);
    cmdBaseResult.setStatus(HttpStatus.OK.value());
    cmdBaseResult.setMessage("Sync success");
    cmdBaseResult.setTimestamp(new Date());

    kafkaProducible.send(syncDevices, JsonHelper.objectToJsonString(cmdBaseResult));

    if (log.isDebugEnabled()) {
      log.debug("Device syncing data is sent to: {}", syncDevices);
    }
  }

  private void responseSyncError(String requestId, Long deploymentId) {
    CmdBaseResult cmdBaseResult = new CmdBaseResult();
    cmdBaseResult.setRequestId(requestId);
    cmdBaseResult.setDeploymentId(deploymentId);
    cmdBaseResult.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    cmdBaseResult.setMessage("Sync devices failed");
    cmdBaseResult.setTimestamp(new Date());

    kafkaProducible.send(syncDevices, JsonHelper.objectToJsonString(cmdBaseResult));

    if (log.isDebugEnabled()) {
      log.debug("Device syncing failure info is sent to: {}", syncDevices);
    }
  }

  private void getDeviceTechnicalData(String requestId, String externalId) {

    SSILMessage ssilMessage = new SSILMessage();
    ssilMessage.setInfo_txt("IEM_MES_EXP_CPR_STA_EQT");
    SSILMessageHeader ent = new SSILMessageHeader();
    ent.setType("B");
    ent.setVersion(1);
    ent.setLgmes(0);
    ent.setId(requestId);
    ssilMessage.setEnt(ent);
    AbstractMsgBody corps = new AllDataMsgBody(externalId);
    ssilMessage.setCorps(corps);
    String json = JsonHelper.convertObject2Json(ssilMessage);

    kafkaProducible.send(requestMivisu, json);
  }
}
