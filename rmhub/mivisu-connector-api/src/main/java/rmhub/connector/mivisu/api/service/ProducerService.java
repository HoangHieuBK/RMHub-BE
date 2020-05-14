package rmhub.connector.mivisu.api.service;

import static rmhub.common.constant.MivisuMessageConstant.EQT_ALL_TYPE;
import static rmhub.common.constant.MivisuMessageConstant.EQT_TC_TYPE;
import static rmhub.common.constant.MivisuMessageConstant.EQT_WS_TYPE;
import static rmhub.common.constant.MivisuMessageConstant.LIST_DEVICE_TYPE;

import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rmhub.common.kafkaconnector.messagebased.KafkaProducible;
import rmhub.common.utility.JsonHelper;
import rmhub.model.mivisu.api.InfoGeneric;
import rmhub.model.mivisu.api.InfoGetIgx;

@Slf4j
@Service
public class ProducerService {

  @Autowired
  private KafkaProducible<String, String> responseToKafka;

  @Value("${rmhub.mivisu.topic.traffic.device}")
  private String topicTraffic;

  @Value("${rmhub.mivisu.topic.weather.device}")
  private String topicWeather;

  private List<InfoGeneric> typeInfoGenericsTC;

  private List<InfoGeneric> typeInfoGenericsWS;

  public void responseToKafka(String response, String requestId, String deviceType, String deploymentId) {

    InfoGetIgx infoGetIgx = ParserHelper.buildNodeInfoGetIgx(response);

    if (infoGetIgx == null) {
      return;
    }

    typeInfoGenericsTC = new ArrayList<>();

    typeInfoGenericsWS = new ArrayList<>();

    getInfoGetIgxById(infoGetIgx, deviceType);

    if (EQT_TC_TYPE.equals(deviceType)) {
      InfoGetIgx trafficInfos = buildInfoGetIgx(typeInfoGenericsTC, requestId, deploymentId);
      sendInfosToKafka(trafficInfos, topicTraffic);

      return;
    }

    if (EQT_WS_TYPE.equals(deviceType)) {
      InfoGetIgx weatherInfos = buildInfoGetIgx(typeInfoGenericsWS, requestId, deploymentId);
      sendInfosToKafka(weatherInfos, topicWeather);

      return;
    }

    if (EQT_ALL_TYPE.equals(deviceType)) {
      InfoGetIgx trafficInfos = buildInfoGetIgx(typeInfoGenericsTC, requestId, deploymentId);
      sendInfosToKafka(trafficInfos, topicTraffic);

      InfoGetIgx weatherInfos = buildInfoGetIgx(typeInfoGenericsWS, requestId, deploymentId);
      sendInfosToKafka(weatherInfos, topicWeather);
    }
  }

  private void getInfoGetIgxById(InfoGetIgx infoGetIgx, String deviceType) {
    InfoGetIgx resInfoGetIgx = SerializationUtils.clone(infoGetIgx);
    List<InfoGeneric> infoGenerics = resInfoGetIgx.getInfoGenerics();

    for (InfoGeneric item : infoGenerics) {
      String value = item.getId().getValue();

      String type = value != null ? value.substring(0, 2) : "";

      // must be a device
      if (!LIST_DEVICE_TYPE.contains(type)) {
        continue;
      }

      if (EQT_TC_TYPE.equals(type) && (EQT_ALL_TYPE.equals(deviceType) || EQT_TC_TYPE.equals(deviceType))) {
        typeInfoGenericsTC.add(item);
      }

      if (EQT_WS_TYPE.equals(type) && (EQT_ALL_TYPE.equals(deviceType) || EQT_WS_TYPE.equals(deviceType))) {
        typeInfoGenericsWS.add(item);
      }
    }
  }

  private InfoGetIgx buildInfoGetIgx(List<InfoGeneric> infoGenerics, String requestId, String deploymentId) {

    InfoGetIgx resInfoGetIgx = new InfoGetIgx();

    resInfoGetIgx.setRequestId(requestId);
    resInfoGetIgx.setInfoGenerics(infoGenerics);
    resInfoGetIgx.setDeploymentId(Long.parseLong(deploymentId));

    return resInfoGetIgx;
  }

  private void sendInfosToKafka(InfoGetIgx infos, String topic) {

    String jsonInfos = JsonHelper.convertObject2JsonNoWrapRoot(infos);

    responseToKafka.send(topic, jsonInfos);
  }
}
