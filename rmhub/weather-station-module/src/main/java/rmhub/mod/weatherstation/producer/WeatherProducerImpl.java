package rmhub.mod.weatherstation.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rmhub.common.kafkaconnector.messagebased.KafkaProducible;
import rmhub.common.utility.JsonHelper;
import rmhub.model.mivisu.ssil.AlertResponse;

@Service
class WeatherProducerImpl implements WeatherProducer {

  @Autowired
  private KafkaProducible<String, String> kafkaProducible;

  @Value("${rmhub.mivisu.topic.notification.weather.data}")
  private String notificationWeatherTopic;

  public void sendAlertToKafka(AlertResponse alertResponse) {
    kafkaProducible.send(notificationWeatherTopic, JsonHelper.convertObject2Json(alertResponse));
  }
}
