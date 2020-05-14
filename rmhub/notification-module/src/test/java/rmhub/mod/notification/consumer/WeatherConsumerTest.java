package rmhub.mod.notification.consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static rmhub.mod.notification.TestConst.RMHUB_LOGGER_NAME;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.Map;
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
import rmhub.common.constant.MivisuMessageConstant;
import rmhub.mod.notification.cache.WeatherMeasurementCache;
import rmhub.model.mivisu.ssil.AlertResponse;
import rmhub.model.mivisu.ssil.Body;
import rmhub.model.mivisu.ssil.Centrale;
import rmhub.model.mivisu.ssil.MivisuXml;

@ExtendWith(SpringExtension.class)
class WeatherConsumerTest {

  @InjectMocks
  private WeatherConsumer consumer;

  @Mock
  private SimpMessageSendingOperations messageSendingOperations;

  @Mock
  private WeatherMeasurementCache weatherMeasurementCache;

  private static ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  @BeforeEach
  void setUp() {
    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel(RMHUB_LOGGER_NAME, LogLevel.INFO);
  }

  @Test
  void pushNotification() throws Exception {

    Map<String, Object> mesureMap = Collections.singletonMap(MivisuMessageConstant.EQT_DT_MES, "test");
    var mesure = Collections.singletonList(mesureMap);

    var centrale = Centrale.builder().mesure(mesure).build();

    var body = Body.builder().rep_Nb_Eqt(1).centrale(Collections.singletonList(centrale)).build();

    var mivisuXml = new MivisuXml();
    mivisuXml.setBody(body);

    String json = OBJECT_MAPPER.writeValueAsString(mivisuXml);

    when(weatherMeasurementCache.updateAlertResponse(any(AlertResponse.class), any(AlertResponse.class))).thenReturn(new AlertResponse());

    consumer.pushNotification(json);

    Mockito.verify(messageSendingOperations, Mockito.times(1)).convertAndSend("/weather/data", json);
  }

  @Test
  void debugCoverTrick() throws Exception {

    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel(RMHUB_LOGGER_NAME, LogLevel.DEBUG);

    Map<String, Object> mesureMap = Collections.singletonMap(MivisuMessageConstant.EQT_DT_MES, "test");
    var mesure = Collections.singletonList(mesureMap);

    var centrale = Centrale.builder().mesure(mesure).build();

    var body = Body.builder().rep_Nb_Eqt(1).centrale(Collections.singletonList(centrale)).build();

    var mivisuXml = new MivisuXml();
    mivisuXml.setBody(body);

    consumer.pushNotification(OBJECT_MAPPER.writeValueAsString(mivisuXml));
  }
}
