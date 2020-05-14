package rmhub.common.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import rmhub.common.exception.RmhubException;

@Slf4j
class JsonHelperTest {

  private static String LINE_SEPARATOR = System.lineSeparator();

  private static String LINE_FEED = "\n";

  @Test
  void convertJson2Object() {
    String jsonString = "{\"id\":3,\"deviceId\":\"4c5a9ee6-0b21-4477-9602-f59701c7055c\","
        + "\"attachedTo\":\"c21abd85-80c5-4bf6-844d-31a3764aa7cc\",\"subsystem\":\"weather3\",\"host\":\"192.168.4.3\",\"port\":\"0003\"}";

    Object actual = JsonHelper.convertJson2Object(jsonString, Object.class);

    Assertions.assertNotNull(actual);

    Assertions.assertNotEquals("", actual);
  }

  @Test
  void convertJson2Object_exception() {
    String jsonString = "{\"id\":3,\"deviceId\":\"4c5a9ee6-0b21-4477-9602-f59701c7055c\","
        + "\"attachedTo\":\"c21abd85-80c5-4bf6-844d-31a3764aa7cc\"\"subsystem\":\"weather3\",\"host\":\"192.168.4.3\",\"port\":\"0003\"}";

    RmhubException ex = Assertions.assertThrows(RmhubException.class, () -> JsonHelper.convertJson2Object(jsonString, Object.class));

    Assertions.assertNotNull(ex);

  }

  @Test
  void convertObject2Json() {
    Map<String, String> data = new HashMap<>();
    data.put("id", "1");

    String actual = JsonHelper.convertObject2Json(data);

    Assertions.assertNotNull(actual);

    Assertions.assertNotEquals("{}", actual);
  }

  @Test
  void convertObject2Json_exception() throws JsonProcessingException {
    Map<String, String> data = new HashMap<>();
    data.put(null, null);

    RmhubException ex = Assertions.assertThrows(RmhubException.class, () -> JsonHelper.convertObject2Json(data));

    Assertions.assertNotNull(ex);

  }

  @Test
  void convertObject2JsonNoWrapRoot() {
    Map<String, String> data = new HashMap<>();
    data.put("id", "1");

    String actual = JsonHelper.convertObject2JsonNoWrapRoot(data);

    Assertions.assertNotNull(actual);

    Assertions.assertNotEquals("{}", actual);
  }

  @Test
  void convertObject2JsonNoWrapRoot_exception() throws JsonProcessingException {
    Map<String, String> data = new HashMap<>();
    data.put(null, null);

    RmhubException ex = Assertions.assertThrows(RmhubException.class, () -> JsonHelper.convertObject2JsonNoWrapRoot(data));

    Assertions.assertNotNull(ex);

  }

  @Test
  void convertStringToClass() {

    SimpleJson simpleJson = new SimpleJson("value");
    Assertions.assertEquals(simpleJson, JsonHelper.convertJson2Object("{\"key\": \"value\"}", SimpleJson.class));
  }

  @Test
  void objectToJsonString() {
    SimpleJson simpleJson = new SimpleJson("value");
    Assertions.assertEquals(JsonHelper.objectToJsonString(simpleJson),
        "{" + LINE_SEPARATOR
            + "  \"key\" : \"value\"" + LINE_SEPARATOR
            + "}");
  }

  @Test
  void objectToJsonString_Exception() {
    Map<String, String> data = new HashMap<>();
    data.put(null, null);
    RmhubException ex = Assertions.assertThrows(RmhubException.class, () -> JsonHelper.objectToJsonString(data));
    Assertions.assertNotNull(ex);
  }

  @Test
  void convertList2JsonString() {

    List<?> simpleJsons = Collections.singletonList(new SimpleJson("value"));
    Assertions.assertEquals("[ {" + LINE_SEPARATOR
        + "  \"key\" : \"value\"" + LINE_SEPARATOR
        + "} ]", JsonHelper.convertList2JsonString(simpleJsons));
  }

  @Test
  void convertList2JsonString_exception() {
    Map<String, String> data = new HashMap<>();
    data.put(null, null);
    RmhubException ex = Assertions.assertThrows(RmhubException.class, () -> JsonHelper.convertList2JsonString(data));
    Assertions.assertNotNull(ex);
  }

  @Data
  @NoArgsConstructor
  @AllArgsConstructor
  static class SimpleJson {

    String key;
  }
}
