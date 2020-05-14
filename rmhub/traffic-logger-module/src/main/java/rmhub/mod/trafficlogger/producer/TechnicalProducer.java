package rmhub.mod.trafficlogger.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import rmhub.common.kafkaconnector.messagebased.KafkaProducible;
import rmhub.common.utility.JsonHelper;
import rmhub.model.mivisu.ssil.TechnicalStatus;

@Profile("!test")
@Component
public class TechnicalProducer {

  @Autowired
  private KafkaProducible<String, String> kafkaProducible;

  @Value("${rmhub.mivisu.topic.notification.technical.data}")
  private String notificationTechnicalDataTopic;

  public void sendTechnicalStatusToKafka(TechnicalStatus techStatus) {
    kafkaProducible.send(notificationTechnicalDataTopic, JsonHelper.convertObject2JsonNoWrapRoot(techStatus));
  }
}
