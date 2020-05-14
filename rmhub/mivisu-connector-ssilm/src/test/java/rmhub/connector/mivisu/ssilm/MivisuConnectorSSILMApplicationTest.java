package rmhub.connector.mivisu.ssilm;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@EmbeddedKafka
@DirtiesContext
class MivisuConnectorSSILMApplicationTest {

  @Test
  void contextLoads() {
  }
}
