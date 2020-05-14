package rmhub.mod.trafficlogger.producer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import rmhub.common.kafkaconnector.messagebased.KafkaProducible;
import rmhub.model.mivisu.ssil.TechnicalStatus;

@SuppressWarnings("unchecked")
@ExtendWith(SpringExtension.class)
class TechnicalProducerTest {

  @Mock
  private KafkaProducible kafkaProducible;

  @InjectMocks
  private TechnicalProducer serviceImpl;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(serviceImpl, "notificationTechnicalDataTopic", "test");
  }

  @Test
  void sendAlertToKafka() {
    var technicalStatus = TechnicalStatus.builder()
        .nb_Et(1)
        .build();
    serviceImpl.sendTechnicalStatusToKafka(technicalStatus);
    Mockito.verify(kafkaProducible, Mockito.times(1)).send(Mockito.anyString(), Mockito.anyString());
  }
}
