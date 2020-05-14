package rmhub.mod.trafficlogger.consumer;

public interface TrafficConsumer {

  void listenRequestFromKafka(String message);
}
