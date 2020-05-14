package rmhub.mod.weatherstation.producer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import rmhub.common.kafkaconnector.messagebased.KafkaProducible;
import rmhub.model.mivisu.ssil.AlertResponse;

@SuppressWarnings("unchecked")
@ExtendWith(SpringExtension.class)
class WeatherProducerImplTest {

  @Mock
  private KafkaProducible kafkaProducible;

  @InjectMocks
  private WeatherProducerImpl serviceImpl;

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(serviceImpl, "notificationWeatherTopic", "test");
  }

  @Test
  void sendAlertToKafka() {
    var alertResponse = AlertResponse.builder()
        .deploymentId(1)
        .size(1)
        .build();

    serviceImpl.sendAlertToKafka(alertResponse);
    Mockito.verify(kafkaProducible, Mockito.times(1)).send(Mockito.anyString(), Mockito.anyString());
  }
}
