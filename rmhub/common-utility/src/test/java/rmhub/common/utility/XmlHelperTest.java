package rmhub.common.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class XmlHelperTest {

  @Test
  void convertObject2XmlString() throws JsonProcessingException {
    Map<String, String> data = new HashMap<>();
    data.put("id", "1");

    String actual = XmlHelper.convertObject2XmlString(data);

    Assertions.assertNotNull(actual);

    Assertions.assertNotEquals("{}", actual);
  }

  @Test
  void convertXmlString2Object() throws IOException {
    String data = "<HashMap>\n"
        + "  <id>1</id>\n"
        + "</HashMap>\n";
    Map<String, String> actual = XmlHelper.convertXmlString2Object(data, Map.class);
    Assertions.assertNotNull(actual);
    Assertions.assertEquals("1", actual.get("id"));
  }

  @Test
  void convertXmlString2StringJson() {
    String data = "<HashMap>\n"
        + "  <id>1</id>\n"
        + "</HashMap>\n";
    String actual = XmlHelper.convertXmlString2StringJson(data);
    Assertions.assertNotNull(actual);
  }

  @Test
  void convertJsonString2XmlString() {
    String jsonString = "{\"id\":3,\"deviceId\":\"4c5a9ee6-0b21-4477-9602-f59701c7055c\","
        + "\"attachedTo\":\"c21abd85-80c5-4bf6-844d-31a3764aa7cc\",\"subsystem\":\"weather3\",\"host\":\"192.168.4.3\",\"port\":\"0003\"}";
    String actual = XmlHelper.convertJsonString2XmlString(jsonString);
    Assertions.assertNotNull(actual);
  }
}
