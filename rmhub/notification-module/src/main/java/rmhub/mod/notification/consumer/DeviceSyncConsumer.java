package rmhub.mod.notification.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import rmhub.common.core.response.PhysicalDeviceResult;
import rmhub.common.utility.JsonHelper;
import rmhub.mod.notification.constant.Destinations;

@Slf4j
@Service
public class DeviceSyncConsumer {

  @Autowired
  private SimpMessageSendingOperations messagingTemplate;

  @KafkaListener(topics = "sync-devices")
  public void responseDataAfterSync(String data) {

    if (log.isDebugEnabled()) {
      log.debug("Message listened from Kafka: {}", data);
    }

    PhysicalDeviceResult cmdBaseResult = JsonHelper.convertJson2Object(data, PhysicalDeviceResult.class);

    messagingTemplate.convertAndSendToUser(cmdBaseResult.getRequestId(), Destinations.SYNC_DEVICES, cmdBaseResult,
        createMessageHeader(cmdBaseResult.getRequestId()));

    log.info("Message sent to: {}!", Destinations.SYNC_DEVICES);
  }

  private MessageHeaders createMessageHeader(String requestId) {
    SimpMessageHeaderAccessor simpMessageHeaderAccessor = SimpMessageHeaderAccessor.create();
    simpMessageHeaderAccessor.setSessionId(requestId);
    return simpMessageHeaderAccessor.getMessageHeaders();
  }
}
