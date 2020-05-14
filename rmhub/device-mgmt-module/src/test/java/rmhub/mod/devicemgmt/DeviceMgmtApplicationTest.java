package rmhub.mod.devicemgmt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@EmbeddedKafka
@DirtiesContext
class DeviceMgmtApplicationTest {

  @Test
  void contextLoads() {
  }
}
