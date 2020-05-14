package rmhub.mod.weatherstation.consumer;

public interface WeatherAlertConsumer {

  void listenRequestFromKafka(String message);
}
