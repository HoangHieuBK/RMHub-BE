package rmhub.mod.notification.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import rmhub.common.utility.JsonHelper;
import rmhub.mod.notification.cache.TrafficTechnicalCache;
import rmhub.mod.notification.constant.Destinations;
import rmhub.model.mivisu.ssil.TechnicalStatus;

@Service
@Slf4j
public class TechnicalConsumer {

  @Autowired
  private TrafficTechnicalCache trafficTechnicalCache;

  @Autowired
  private SimpMessageSendingOperations messagingTemplate;

  @KafkaListener(topics = "${rmhub.mivisu.topic.notification.technical.data}")
  public void pushNotification(String data) {

    if (log.isDebugEnabled()) {
      log.debug("The Notification-Module listened a technical data from kafka: {}", data);
    }
    TechnicalStatus technicalStatus = JsonHelper.convertJson2Object(data, TechnicalStatus.class);
    TechnicalStatus cacheTechnicalStatus = trafficTechnicalCache.getTrafficTechnicalStatus(technicalStatus.getDeploymentId());

    technicalStatus = trafficTechnicalCache.updateTrafficTechnicalStatus(technicalStatus, cacheTechnicalStatus);

    String jsonTechnicalData = JsonHelper.convertObject2Json(technicalStatus);

    messagingTemplate.convertAndSend(Destinations.TECHNICAL_DATA, jsonTechnicalData);

    log.info("Message sent to: {}!", Destinations.TECHNICAL_DATA);
  }
}
