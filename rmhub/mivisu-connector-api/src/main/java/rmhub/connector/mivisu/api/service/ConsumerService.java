package rmhub.connector.mivisu.api.service;

import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import rmhub.common.constant.MivisuMessageConstant;
import rmhub.common.utility.JsonHelper;

@Service
@Slf4j
public class ConsumerService {

  @Autowired
  RestTemplate restTemplate;

  @Autowired
  ProducerService producerService;

  @Value("${rmhub.mivisu.api.url}")
  String url;

  @KafkaListener(id = "mivisu-api", topics = "${rmhub.mivisu.topic.request.device}")
  public void listenRequestFromKafka(String msg) {

    if (log.isDebugEnabled()) {
      log.debug("Receive request info from kafka: {}", msg);
    }

    @SuppressWarnings("unchecked")
    Map<String, String> map = JsonHelper.convertJson2Object(msg, Map.class);

    // FIXME:
    map.put("doto", "1");
    map.put("nf", "2");
    map.put("t1", "S");
    map.put("n1", "/SOURCE");
    map.put("o1", "eq");
    map.put("v1", "mivisu");
    map.put("r1", "1");
    map.put("t2", "S");
    map.put("n2", "/CONTEXTE");
    map.put("o2", "eq");
    map.put("v2", "CXTMVS_MES_STA");
    map.put("r2", "1");
    map.put("format", "json");

    UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);

    for (Entry<String, String> entry : map.entrySet()) {
      String key = entry.getKey();
      String val = entry.getValue();

      if (MivisuMessageConstant.REQUEST_KEY.equals(key)) {
        continue;
      }

      if (MivisuMessageConstant.DEVICE_TYPE.equals(key)) {
        continue;
      }
      if (MivisuMessageConstant.DEPLOYMENT_ID.equals(key)) {
        continue;
      }

      builder.queryParam(key, val);
    }

    String requestId = map.get(MivisuMessageConstant.REQUEST_KEY);
    String deviceType = map.get(MivisuMessageConstant.DEVICE_TYPE);
    String deploymentId = map.get(MivisuMessageConstant.DEPLOYMENT_ID);

    URI uri = builder.build().toUri();

    String response = restTemplate.getForObject(uri, String.class);

    producerService.responseToKafka(response, requestId, deviceType, deploymentId);
  }
}
