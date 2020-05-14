package rmhub.mod.trafficlogger.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import rmhub.common.utility.JsonHelper;
import rmhub.mod.trafficlogger.behaviour.GenerateTrafficAlert;
import rmhub.model.mivisu.ssil.MivisuXml;

@Profile("!test")
@Service
@Slf4j
class TrafficConsumerImpl implements TrafficConsumer {

  @Autowired
  private GenerateTrafficAlert generateTrafficAlert;

  @Override
  @KafkaListener(topics = "${rmhub.mivisu.topic.traffic.counting}")
  public void listenRequestFromKafka(String message) {

    log.info("Received response from mivisu connector.");

    // convert kafka message into java object
    MivisuXml mivisuXml = JsonHelper.convertJson2Object(message, MivisuXml.class);

    if (log.isDebugEnabled()) {
      log.debug("Data received from mivisu connector: {}", mivisuXml);
    }

    // internally process the data from Mivisu
    generateTrafficAlert.processData(mivisuXml);
  }
}
