package rmhub.mod.trafficlogger.consumer;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import rmhub.common.utility.JsonHelper;
import rmhub.mod.trafficlogger.entity.TechnicalData;
import rmhub.mod.trafficlogger.producer.TechnicalProducer;
import rmhub.mod.trafficlogger.service.TechnicalDataService;
import rmhub.model.mivisu.ssil.Et;
import rmhub.model.mivisu.ssil.TechnicalStatus;

@Profile("!test")
@Component
@Slf4j
public class TechnicalConsumer {

  @Autowired
  private TechnicalDataService technicalDataService;

  @Autowired
  private TechnicalProducer technicalProducer;

  @KafkaListener(topics = "${rmhub.mivisu.topic.technical.status}")
  public void listenRequestFromKafka(String msg) {

    log.info("Received response from mivisu connector.");

    TechnicalStatus techStatus = JsonHelper.convertJson2Object(msg, TechnicalStatus.class);

    if (log.isDebugEnabled()) {
      log.debug("Receive technical status from mivisu: {}", techStatus);
    }

    process(techStatus);
    technicalProducer.sendTechnicalStatusToKafka(techStatus);
  }

  private void process(TechnicalStatus data) {
    List<Et> ets = data.getEts();
    if (data.getNb_Et() > 0) {
      // process with stream api
      ets.forEach(this::saveTechnicalStatus);
    }
  }

  private void saveTechnicalStatus(Et et) {
    TechnicalData technicalData = TechnicalData.builder()
        .externalId(et.getId_ext())
        .eqtConfVersion(et.getEqt_Conf_Version())
        .etatAlim(et.getEtat_Alim())
        .etatCom(et.getEtat_Com())
        .etatDate(et.getEtat_Date())
        .etatSys(et.getEtat_Sys())
        .build();
    technicalDataService.create(technicalData);
  }
}
