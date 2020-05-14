package rmhub.connector.mivisu.ssilm.queue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestQueue {

  @Getter
  private static final Queue<byte[]> requestQueue = new ConcurrentLinkedQueue<>();

  public static void pushRequestInfo(byte[] byteRequest) {
    log.info("Queue push request info: {}", new String(byteRequest));
    requestQueue.add(byteRequest);
  }
}
