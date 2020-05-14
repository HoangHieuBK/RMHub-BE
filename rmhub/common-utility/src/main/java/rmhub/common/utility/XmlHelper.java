package rmhub.common.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.IOException;
import org.json.JSONObject;
import org.json.XML;

public class XmlHelper {

  private XmlHelper() {
  }

  private static XmlMapper xmlMapper = new XmlMapper();

  /**
   * This method used for parser java object to xml string
   *
   * @param data data need to parser
   * @return string xml
   * @throws JsonProcessingException throw exception white parser data to xml string format
   */
  public static <T> String convertObject2XmlString(T data) throws JsonProcessingException {
    return xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
  }

  /**
   * This method used for parser xml string to java object<br> Be aware that HTML entities will be translated, for ex: &amp;amp; -> &
   *
   * @param xmlString data need to parser
   * @param clazz type's object must parser
   * @return java object with type passed
   * @throws IOException throw exception white parser string xml to java object
   */
  public static <T> T convertXmlString2Object(String xmlString, Class<T> clazz) throws IOException {

    JacksonXmlModule xmlModule = new JacksonXmlModule();
    xmlModule.setDefaultUseWrapper(false);
    ObjectMapper objectMapper = new XmlMapper(xmlModule);

    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    return objectMapper.readValue(xmlString, clazz);
  }

  /**
   * This method used for parser xml string object to json string
   *
   * @return xmlString is string xml need to parser
   */
  public static String convertXmlString2StringJson(String xmlString) {
    JSONObject xmlJSONObj = XML.toJSONObject(xmlString);
    return xmlJSONObj.toString(4);
  }

  /**
   * This method used for parser json string object to xml string
   *
   * @return jsonString is string json need to parser
   */
  public static String convertJsonString2XmlString(String jsonString) {
    JSONObject obj = new JSONObject(jsonString);
    return XML.toString(obj);
  }
}
