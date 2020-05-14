package rmhub.mod.trafficlogger.producer;

import rmhub.model.mivisu.ssil.AlertResponse;

public interface TrafficProducer {

  void sendAlertToKafka(AlertResponse alertResponse);
}
