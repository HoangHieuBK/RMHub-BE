package rmhub.connector.mivisu.api.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import rmhub.common.constant.MivisuMessageConstant;
import rmhub.common.utility.JsonHelper;

@Slf4j
@ExtendWith(SpringExtension.class)
class ConsumerServiceTest {

  @InjectMocks
  private ConsumerService consumerService;

  @Mock
  private RestTemplate restTemplate;

  @Mock
  private ProducerService producerService;

  @Test
  void listenRequestFromKafka() throws IOException {

    String type = MivisuMessageConstant.EQT_TC_TYPE;

    Map<String, String> requestParams = new HashMap<>();
    requestParams.put("deviceType", type);
    requestParams.put("requestId", "1");
    requestParams.put("deploymentId", "1");

    String response = IOUtils.toString(
        getClass().getResourceAsStream("/response_api_connect_2.json"),
        StandardCharsets.UTF_8
    );

    when(restTemplate.getForObject(any(), any())).thenReturn(response);
    ReflectionTestUtils.setField(consumerService, "url", "http://test");
    String json = JsonHelper.convertObject2JsonNoWrapRoot(requestParams);

    consumerService.listenRequestFromKafka(json);

    verify(producerService, atLeastOnce()).responseToKafka(anyString(), anyString(), anyString(), anyString());
  }

}
