package rmhub.mod.weatherstation.producer;

import rmhub.model.mivisu.ssil.AlertResponse;

public interface WeatherProducer {

  void sendAlertToKafka(AlertResponse alertResponse);
}
