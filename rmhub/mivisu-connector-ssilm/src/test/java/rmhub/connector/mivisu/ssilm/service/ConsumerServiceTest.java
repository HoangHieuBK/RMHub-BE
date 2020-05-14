package rmhub.connector.mivisu.ssilm.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class ConsumerServiceTest {

  @InjectMocks
  private ConsumerService consumerService;

  @Test
  void testListenRequestFromKafka() {
    consumerService.listenRequestFromKafka("{}");
  }
}
