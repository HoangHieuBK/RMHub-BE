package rmhub.mod.trafficlogger.consumer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import rmhub.mod.trafficlogger.behaviour.GenerateTrafficAlert;
import rmhub.model.mivisu.ssil.Body;
import rmhub.model.mivisu.ssil.Centrale;
import rmhub.model.mivisu.ssil.MivisuXml;

@ExtendWith(SpringExtension.class)
class TrafficConsumerImplTest {

  @InjectMocks
  private TrafficConsumerImpl consumer;

  @Mock
  private GenerateTrafficAlert generator;

  private static ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  /**
   * For Happy case.
   */
  @Test
  void listenRequestFromKafka_passThrough() throws Exception {

    // given MESURE
    List<Map<String, Object>> mesure = new ArrayList<>();
    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_DT_MES, "test");
    mesure.add(mesureMap);

    // given CENTRALE
    var centrale = new Centrale();
    centrale.setMesure(mesure);

    // given CORPS as Body
    var body = new Body();
    body.setRep_Nb_Eqt(1); // CENTRALE quantity
    body.setCentrale(Collections.singletonList(centrale)); // CENTRALE itself

    // given data from Mivisu
    var mivisuXml = new MivisuXml();
    mivisuXml.setBody(body);

    // convert mivisu data to JSON
    var json = OBJECT_MAPPER.writeValueAsString(mivisuXml);

    // then
    consumer.listenRequestFromKafka(json);

    // verify
    Mockito.verify(generator, Mockito.times(1)).processData(Mockito.any(MivisuXml.class));
  }

  /**
   * For Exception coverage.
   * <br/>FIXME should we update source code
   */
  @Test
  void listenRequestFromKafka_nullMessage_expectNullPointerException() {

    // in case we pass null to the method
    Assertions.assertThrows(NullPointerException.class, () -> consumer.listenRequestFromKafka(null));
  }

  /**
   * For Exception coverage.
   */
  @Test
  void listenRequestFromKafka_fakeMessage_expectRmhubException() {

    // in case we pass String that can not be parsed to the method
    RmhubException exception = Assertions.assertThrows(RmhubException.class, () -> consumer.listenRequestFromKafka("test"));
    Assertions.assertTrue(exception.getCause() instanceof JsonParseException);
  }
}
