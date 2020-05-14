package rmhub.mod.notification.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import rmhub.mod.notification.constant.Destinations;

@Service
@Slf4j
public class TrafficConsumer {

  @Autowired
  private SimpMessageSendingOperations messagingTemplate;

  @KafkaListener(topics = "${rmhub.mivisu.topic.notification.traffic.data}")
  public void pushNotification(String data) {

    if (log.isDebugEnabled()) {
      log.debug("The Notification-Module listened a traffic data from kafka: {}", data);
    }

    messagingTemplate.convertAndSend(Destinations.TRAFFIC_DATA, data);

    log.info("Message sent to: {}!", Destinations.TRAFFIC_DATA);
  }
}
