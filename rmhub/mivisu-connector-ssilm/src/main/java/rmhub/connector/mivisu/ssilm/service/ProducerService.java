package rmhub.connector.mivisu.ssilm.service;

import java.util.Map;
import java.util.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import rmhub.common.kafkaconnector.messagebased.KafkaProducible;
import rmhub.connector.mivisu.ssilm.behaviour.SsilmBehaviour;
import rmhub.connector.mivisu.ssilm.queue.ResponseQueue;
import rmhub.infras.socket.service.DataProducer;

@Service
@Slf4j
public class ProducerService implements DataProducer {

  @Autowired
  private SsilmBehaviour ssilmBehaviour;

  @Autowired
  private KafkaProducible<String, String> responseToKafka;

  @Override
  public void sendToKafka() {
    Queue<byte[]> outgoing = ResponseQueue.getResponseQueue();
    while (outgoing.size() > 0) {
      Map<String, String> map = ssilmBehaviour.analysisData(outgoing);
      if (!map.isEmpty()) {
        for (String key : map.keySet()) {
          responseToKafka.send(key, map.get(key));
        }
      }
    }
  }
}
