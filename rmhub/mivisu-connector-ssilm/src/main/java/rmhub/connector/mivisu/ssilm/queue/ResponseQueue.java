package rmhub.connector.mivisu.ssilm.queue;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResponseQueue {

  @Getter
  private static final Queue<byte[]> responseQueue = new ConcurrentLinkedQueue<>();

  public static void pushResponseInfo(byte[] byteResponse) {
    log.info("Queue push response info: {}", byteResponse);
    responseQueue.add(byteResponse);
  }
}
