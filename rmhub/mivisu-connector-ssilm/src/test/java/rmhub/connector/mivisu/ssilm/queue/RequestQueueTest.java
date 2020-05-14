package rmhub.connector.mivisu.ssilm.queue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class RequestQueueTest {

  @Test
  void testRequestQueueTest() {
    RequestQueue requestQueue = new RequestQueue();
  }

  @Test
  void testPushResponseInfo() {
    ResponseQueue.pushResponseInfo("test".getBytes());
    ResponseQueue.getResponseQueue().clear();
  }
}
