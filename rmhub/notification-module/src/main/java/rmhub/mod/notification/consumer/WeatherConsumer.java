package rmhub.mod.notification.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import rmhub.common.utility.JsonHelper;
import rmhub.mod.notification.cache.WeatherMeasurementCache;
import rmhub.mod.notification.constant.Destinations;
import rmhub.model.mivisu.ssil.AlertResponse;
import rmhub.model.mivisu.ssil.CacheWeatherMeasure;

@Service
@Slf4j
public class WeatherConsumer {

  @Autowired
  private SimpMessageSendingOperations messagingTemplate;

  @Autowired
  private WeatherMeasurementCache weatherMeasurementCache;

  private static final int DEPLOYMENT_ID = 1;

  @KafkaListener(topics = "${rmhub.mivisu.topic.notification.weather.data}")
  public void pushNotification(String data) {

    if (log.isDebugEnabled()) {
      log.debug("The Notification-Module listened a weather data from kafka: {}", data);
    }

    messagingTemplate.convertAndSend(Destinations.WEATHER_DATA, data);

    log.info("Message sent to: {}!", Destinations.WEATHER_DATA);

    CacheWeatherMeasure weatherMeasureData = JsonHelper.convertJson2Object(data, CacheWeatherMeasure.class);

    AlertResponse cacheAlertResponse = weatherMeasurementCache.getWeatherMeasure(DEPLOYMENT_ID);

    weatherMeasurementCache.updateAlertResponse(weatherMeasureData.getAlertResponse(), cacheAlertResponse);
  }
}
