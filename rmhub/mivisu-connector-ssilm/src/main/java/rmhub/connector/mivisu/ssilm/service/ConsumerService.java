package rmhub.connector.mivisu.ssilm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import rmhub.common.utility.XmlHelper;
import rmhub.connector.mivisu.ssilm.queue.RequestQueue;

@Service
@Slf4j
public class ConsumerService {

  @KafkaListener(topics = "${rmhub.mivisu.ssilm.topic.request}")
  public void listenRequestFromKafka(String msg) {
    log.info("Receive request info from kafka {} ", msg);
    String xmlString = XmlHelper.convertJsonString2XmlString(msg);
    RequestQueue.pushRequestInfo(xmlString.getBytes());
  }
}
