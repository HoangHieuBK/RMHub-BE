package rmhub.mod.notification.consumer;

import static org.mockito.Mockito.when;
import static rmhub.mod.notification.TestConst.RMHUB_LOGGER_NAME;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rmhub.common.utility.JsonHelper;
import rmhub.mod.notification.cache.TrafficTechnicalCache;
import rmhub.model.mivisu.ssil.TechnicalStatus;

@ExtendWith(SpringExtension.class)
class TechnicalConsumerTest {

  @InjectMocks
  private TechnicalConsumer consumer;

  @Mock
  private TrafficTechnicalCache trafficTechnicalCache;

  @Mock
  private SimpMessageSendingOperations messageSendingOperations;

  private static ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  @BeforeEach
  void setUp() {
    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel(RMHUB_LOGGER_NAME, LogLevel.INFO);
  }

  @Test
  void pushNotification() throws Exception {

    var technicalStatus = TechnicalStatus.builder().nb_Et(1).build();

    String json = OBJECT_MAPPER.writeValueAsString(technicalStatus);

    TechnicalStatus technicalStatus1 = new TechnicalStatus();
    technicalStatus1.setNb_Et(1);
    technicalStatus1.setEts(null);
    technicalStatus1.setDeploymentId(0);

    when(trafficTechnicalCache.getTrafficTechnicalStatus(Mockito.anyInt())).thenReturn(technicalStatus1);
    when(trafficTechnicalCache.updateTrafficTechnicalStatus(Mockito.any(TechnicalStatus.class), Mockito.any(TechnicalStatus.class)))
        .thenReturn(technicalStatus1);

    consumer.pushNotification(json);
    Mockito.verify(messageSendingOperations, Mockito.times(1))
        .convertAndSend("/technical/data", JsonHelper.convertObject2Json(technicalStatus1));
  }

  @Test
  void debugCoverTrick() throws Exception {

    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel(RMHUB_LOGGER_NAME, LogLevel.DEBUG);

    var json = OBJECT_MAPPER.writeValueAsString(TechnicalStatus.builder().nb_Et(1).build());

    consumer.pushNotification(json);
  }
}
