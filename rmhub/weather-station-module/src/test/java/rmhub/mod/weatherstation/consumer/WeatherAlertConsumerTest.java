package rmhub.mod.weatherstation.consumer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rmhub.common.constant.MivisuMessageConstant;
import rmhub.common.exception.RmhubException;
import rmhub.mod.weatherstation.behaviour.GenerateWeatherAlert;
import rmhub.model.mivisu.ssil.Body;
import rmhub.model.mivisu.ssil.Centrale;
import rmhub.model.mivisu.ssil.MivisuXml;

@ExtendWith(SpringExtension.class)
class WeatherAlertConsumerTest {

  @InjectMocks
  private WeatherAlertConsumerImpl consumerImpl;

  @Mock
  GenerateWeatherAlert generateWeatherAlert;

  private static ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  @Test
  void listenRequestFromKafka_msgNull_expectNullPointerException() {
    Assertions.assertThrows(NullPointerException.class, () -> consumerImpl.listenRequestFromKafka(null));
  }

  @Test
  void listenRequestFromKafka_msgEmpty_expectMismatchedInputException() {
    RmhubException exception = Assertions.assertThrows(RmhubException.class, () -> consumerImpl.listenRequestFromKafka(""));
    Assertions.assertTrue(exception.getCause() instanceof MismatchedInputException);
  }

  @Test
  void listenRequestFromKafka_msgWrongType_expectMismatchedInputException() {
    RmhubException exception = Assertions.assertThrows(RmhubException.class, () -> consumerImpl.listenRequestFromKafka("1"));
    Assertions.assertTrue(exception.getCause() instanceof MismatchedInputException);
  }

  @Test
  void listenRequestFromKafka_runOver() throws Exception {
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

    String json = OBJECT_MAPPER.writeValueAsString(mivisuXml);

    consumerImpl.listenRequestFromKafka(json);
    Mockito.verify(generateWeatherAlert, Mockito.only()).process(mivisuXml);
  }
}
