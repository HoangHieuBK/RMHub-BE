package rmhub.common.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import rmhub.common.exception.RmhubException;

@Slf4j
public class JsonHelper {

  private JsonHelper() {
  }

  private static ObjectMapper objMapper = new ObjectMapper();

  static {
    objMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
  }

  /**
   * @param jsonString Data need to parser
   * @param clazz Format want to parser
   * @return Object by format passed
   */
  public static <T> T convertJson2Object(String jsonString, Class<T> clazz) {
    try {
      return objMapper.readValue(jsonString, clazz);
    } catch (IOException e) {
      throw new RmhubException(e.getMessage(), e);
    }
  }

  /**
   * This used for converting a object to json format
   */
  public static String convertObject2Json(Object object) {
    String json;
    try {
      ObjectMapper objMapper = new ObjectMapper();
      objMapper.enable(SerializationFeature.WRAP_ROOT_VALUE);
      objMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
      json = objMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RmhubException(e.getMessage(), e);
    }
    return json;
  }

  /**
   * This used for converting a object to json format
   */
  public static String convertObject2JsonNoWrapRoot(Object object) {
    String json;
    try {
      ObjectMapper objMapper = new ObjectMapper();
      json = objMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    } catch (JsonProcessingException e) {
      throw new RmhubException(e.getMessage(), e);
    }
    return json;
  }

  public static <T> String objectToJsonString(T data) {
    try {
      return objMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
    } catch (JsonProcessingException e) {
      throw new RmhubException(e.getMessage(), e);
    }
  }

  public static <T> String convertList2JsonString(T data) {
    try {
      return objMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
    } catch (JsonProcessingException e) {
      throw new RmhubException(e.getMessage(), e);
    }
  }
}
