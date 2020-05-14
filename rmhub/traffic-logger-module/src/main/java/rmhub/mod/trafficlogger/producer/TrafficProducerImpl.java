package rmhub.mod.trafficlogger.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import rmhub.common.kafkaconnector.messagebased.KafkaProducible;
import rmhub.common.utility.JsonHelper;
import rmhub.model.mivisu.ssil.AlertResponse;

@Profile("!test")
@Service
public class TrafficProducerImpl implements TrafficProducer {

  @Autowired
  private KafkaProducible<String, String> kafkaProducible;

  @Value("${rmhub.mivisu.topic.notification.traffic.data}")
  private String notificationTrafficTopic;

  @Override
  public void sendAlertToKafka(AlertResponse alertResponse) {
    kafkaProducible.send(notificationTrafficTopic, JsonHelper.convertObject2Json(alertResponse));
  }
}
