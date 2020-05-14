package rmhub.mod.notification.consumer;

import static rmhub.mod.notification.TestConst.RMHUB_LOGGER_NAME;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
import rmhub.model.mivisu.ssil.Body;
import rmhub.model.mivisu.ssil.Centrale;
import rmhub.model.mivisu.ssil.MivisuXml;

@ExtendWith(SpringExtension.class)
class TrafficConsumerTest {

  @InjectMocks
  private TrafficConsumer consumer;

  @Mock
  private SimpMessageSendingOperations messagingTemplate;

  private static ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  @BeforeEach
  void setUp() {
    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel(RMHUB_LOGGER_NAME, LogLevel.INFO);
  }

  @Test
  void pushNotification() throws Exception {

    List<Map<String, Object>> mesure = new ArrayList<>();
    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_DT_MES, "test");
    mesure.add(mesureMap);

    var centrale = new Centrale();
    centrale.setMesure(mesure);

    var body = new Body();
    body.setRep_Nb_Eqt(1);
    body.setCentrale(Collections.singletonList(centrale));

    var mivisuXml = new MivisuXml();
    mivisuXml.setBody(body);

    var json = OBJECT_MAPPER.writeValueAsString(mivisuXml);

    consumer.pushNotification(json);
    Mockito.verify(messagingTemplate, Mockito.times(1)).convertAndSend("/traffic/data", json);
  }

  @Test
  void debugCoverTrick() throws Exception {

    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel(RMHUB_LOGGER_NAME, LogLevel.DEBUG);

    consumer.pushNotification(OBJECT_MAPPER.writeValueAsString("test"));
  }
}
