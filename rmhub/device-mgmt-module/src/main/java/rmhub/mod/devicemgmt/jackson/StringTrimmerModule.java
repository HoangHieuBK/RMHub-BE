package rmhub.mod.devicemgmt.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import org.springframework.stereotype.Component;

/**
 * Custom Jackson module that register a Trimmer for any String properties that is handled by Jackson.
 *
 * @author Hino &lt;ntquan@cmc.com.vn&gt;
 */
@Component
public class StringTrimmerModule extends SimpleModule {

  public StringTrimmerModule() {
    super();
    addDeserializer(String.class, new StringTrimmerDeserializer());
  }

  public static class StringTrimmerDeserializer extends StringDeserializer {

    @Override
    public String deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {

      return parser.getValueAsString() != null ? parser.getValueAsString().trim() : null;
    }
  }
}
