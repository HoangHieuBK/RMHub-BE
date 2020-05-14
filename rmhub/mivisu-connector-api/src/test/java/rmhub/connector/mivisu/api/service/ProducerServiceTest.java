package rmhub.connector.mivisu.api.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import rmhub.common.constant.MivisuMessageConstant;
import rmhub.common.kafkaconnector.messagebased.KafkaProducible;
import rmhub.common.utility.JsonHelper;
import rmhub.model.mivisu.api.InfoGetIgx;

@Slf4j
@ExtendWith(SpringExtension.class)
class ProducerServiceTest {

  @InjectMocks
  private ProducerService producerService;

  @Mock
  private KafkaProducible<String, String> template;

  @Test
  void responseToKafka_Traffic_Ok() throws IOException {
    String jsonParam = IOUtils.toString(
        getClass().getResourceAsStream("/response_api_connect_2.json"),
        StandardCharsets.UTF_8
    );

    producerService.responseToKafka(jsonParam, "1", MivisuMessageConstant.EQT_TC_TYPE, "1");

    verify(template, atLeastOnce()).send(any(), any());
  }

  @Test
  void responseToKafka_Weather_Ok() throws IOException {
    String jsonParam = IOUtils.toString(
        getClass().getResourceAsStream("/response_api_connect_2.json"),
        StandardCharsets.UTF_8
    );

    producerService.responseToKafka(jsonParam, "1", MivisuMessageConstant.EQT_WS_TYPE, "1");

    verify(template, atLeastOnce()).send(any(), any());
  }

  @Test
  void responseToKafka_All_Ok() throws IOException {
    String jsonParam = IOUtils.toString(
        getClass().getResourceAsStream("/response_api_connect_2.json"),
        StandardCharsets.UTF_8
    );

    producerService.responseToKafka(jsonParam, "1", MivisuMessageConstant.EQT_ALL_TYPE, "1");

    verify(template, atLeastOnce()).send(any(), any());
  }

  @Test
  void responseToKafka_Weather_doNothing() throws IOException {
    String jsonParam = IOUtils.toString(
        getClass().getResourceAsStream("/response_api_connect_2.json"),
        StandardCharsets.UTF_8
    );

    producerService.responseToKafka(jsonParam, "1", "VMS", "1");

    verify(template, never()).send(any(), any());
  }

  @Test
  void getInfoGetIgxById_notOk_Tc() throws IOException {
    String jsonParam = IOUtils.toString(
        getClass().getResourceAsStream("/JsonInfoGetIgxByIdNull.json"),
        StandardCharsets.UTF_8
    );

    InfoGetIgx infoGetIgxParam = JsonHelper.convertJson2Object(jsonParam, InfoGetIgx.class);
    ReflectionTestUtils.invokeMethod(producerService, "getInfoGetIgxById",
        infoGetIgxParam, MivisuMessageConstant.EQT_TC_TYPE);
  }

  @Test
  void getInfoGetIgxById_notOk_Ws() throws IOException {
    String jsonParam = IOUtils.toString(
        getClass().getResourceAsStream("/JsonInfoGetIgxByIdNull.json"),
        StandardCharsets.UTF_8
    );

    InfoGetIgx infoGetIgxParam = JsonHelper.convertJson2Object(jsonParam, InfoGetIgx.class);
    ReflectionTestUtils.invokeMethod(producerService, "getInfoGetIgxById",
        infoGetIgxParam, MivisuMessageConstant.EQT_WS_TYPE);
  }

  @Test
  void getInfoGetIgxById_notOk_All() throws IOException {
    String jsonParam = IOUtils.toString(
        getClass().getResourceAsStream("/JsonInfoGetIgxByIdNull.json"),
        StandardCharsets.UTF_8
    );

    InfoGetIgx infoGetIgxParam = JsonHelper.convertJson2Object(jsonParam, InfoGetIgx.class);
    ReflectionTestUtils.invokeMethod(producerService, "getInfoGetIgxById",
        infoGetIgxParam, MivisuMessageConstant.EQT_ALL_TYPE);
  }

  @Test
  void responseToKafka_Null() {
    String jsonParam = "{\n"
        + "  \"INFOS\": {\n"
        + "    \"CRVAL\": \"0\",\n"
        + "    \"CRTXT\": {}\n"
        + "  }\n"
        + "}";

    producerService.responseToKafka(jsonParam, "1", MivisuMessageConstant.EQT_WS_TYPE, "1");

    verify(template, never()).send(any(), any());
  }
}
