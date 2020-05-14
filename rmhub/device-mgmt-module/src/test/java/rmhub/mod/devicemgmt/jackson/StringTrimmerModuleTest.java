package rmhub.mod.devicemgmt.jackson;

import java.io.IOException;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Simple Unit test for {@link StringTrimmerModule} that has been auto-configured with registered jackson modules.
 *
 * @author Hino &lt;ntquan@cmc.com.vn&gt;
 */
@Slf4j
@ExtendWith(SpringExtension.class)
@JsonTest
class StringTrimmerModuleTest {

  @Autowired
  private JacksonTester<Jackie> jackson;

  @Test
  void testSerialize() throws IOException {

    // given
    var jackie = Jackie.builder().name("rmhub").age(9L).build();
    var expectedJson = "{\"name\":\"rmhub\",\"age\":9}";
    var actualJson = jackson.write(jackie);
    log.info(actualJson.getJson());

    // assert
    Assertions.assertEquals(expectedJson, actualJson.getJson());
  }

  @Test
  void testDeserialize() throws IOException {

    // given
    var json = "{\"name\":\"otherRmhub\",\"age\":4}";
    var jackie = Jackie.builder().name("otherRmhub").age(4L).build();
    var actualObjectContent = jackson.parse(json);
    log.info(actualObjectContent.getObject().toString());

    // assert
    Assertions.assertEquals(jackie, actualObjectContent.getObject());
  }

  @Test
  void testDeserialize_missingString() throws IOException {

    // given
    var json = "{\"age\":4}";
    var jackie = Jackie.builder().age(4L).build();
    var actualObjectContent = jackson.parse(json);
    log.info(actualObjectContent.getObject().toString());

    // assert
    Assertions.assertEquals(jackie, actualObjectContent.getObject());
  }

  @Test
  void testDeserialize_nullString() throws IOException {

    // given
    var json = "{\"name\":null,\"age\":4}";
    var jackie = Jackie.builder().age(4L).build();
    var actualObjectContent = jackson.parse(json);
    log.info(actualObjectContent.getObject().toString());

    // assert
    Assertions.assertEquals(jackie, actualObjectContent.getObject());
  }

  @Test
  void testDeserialize_emptyString() throws IOException {

    // given
    var json = "{\"name\":\"\",\"age\":4}";
    var jackie = Jackie.builder().name("").age(4L).build();
    var actualObjectContent = jackson.parse(json);
    log.info(actualObjectContent.getObject().toString());

    // assert
    Assertions.assertEquals(jackie, actualObjectContent.getObject());
  }

  /**
   * TODO dunno why but Jackson deserialize all fields as null
   */
  @Test
  void testDeserialize_invalidString_asObject() throws IOException {

    // given
    var json = "{\"name\":{},\"age\":4}";
    var jackie = Jackie.builder().name(null).age(null).build();
    var actualObjectContent = jackson.parse(json);
    log.info(actualObjectContent.getObject().toString());

    // assert
    Assertions.assertEquals(jackie, actualObjectContent.getObject());
  }

  /**
   * TODO dunno why but Jackson deserialize all fields as null
   */
  @Test
  void testDeserialize_invalidString_asArray() throws IOException {

    // given
    var json = "{\"name\":[],\"age\":4}";
    var jackie = Jackie.builder().name(null).age(null).build();
    var actualObjectContent = jackson.parse(json);
    log.info(actualObjectContent.getObject().toString());

    // assert
    Assertions.assertEquals(jackie, actualObjectContent.getObject());
  }

  /**
   * TODO dunno why but Jackson deserialize all fields as null
   */
  @Test
  void testDeserialize_invalidString_asArrayAlso() throws IOException {

    // given
    var json = "{\"name\":[{}],\"age\":4}";
    var jackie = Jackie.builder().name(null).age(null).build();
    var actualObjectContent = jackson.parse(json);
    log.info(actualObjectContent.getObject().toString());

    // assert
    Assertions.assertEquals(jackie, actualObjectContent.getObject());
  }

  /**
   * simple class for testing with Jackson
   */
  @Builder
  @Data
  private static class Jackie {

    private String name;
    private Long age;
  }
}
