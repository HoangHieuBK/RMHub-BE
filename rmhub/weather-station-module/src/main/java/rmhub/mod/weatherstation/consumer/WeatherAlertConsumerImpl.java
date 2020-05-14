package rmhub.mod.weatherstation.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import rmhub.common.utility.JsonHelper;
import rmhub.mod.weatherstation.behaviour.GenerateWeatherAlert;
import rmhub.model.mivisu.ssil.MivisuXml;

@Service
@Slf4j
class WeatherAlertConsumerImpl implements WeatherAlertConsumer {

  @Autowired
  private GenerateWeatherAlert generateWeatherAlert;

  @Override
  @KafkaListener(topics = "${rmhub.mivisu.topic.weather.station}")
  public void listenRequestFromKafka(String message) {

    log.info("Received response from mivisu connector.");

    MivisuXml mivisuXml = JsonHelper.convertJson2Object(message, MivisuXml.class);

    if (log.isDebugEnabled()) {
      log.debug("Receive response type from mivisu: {}", mivisuXml);
    }

    // internally process the data from Mivisu
    generateWeatherAlert.process(mivisuXml);
  }
}
