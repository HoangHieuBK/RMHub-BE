package rmhub.mod.notification;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@EmbeddedKafka
@DirtiesContext
class NotificationModuleApplicationTest {

  @Test
  void contextLoads() {
  }
}
