package rmhub.infras.socket.thread;

import static java.time.Duration.ofSeconds;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

import java.util.Queue;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rmhub.infras.socket.SocketIO;
import rmhub.infras.socket.service.ByteSend;

@Slf4j
@ExtendWith(SpringExtension.class)
class SendTaskTest {

  @InjectMocks
  private SendTask sendTask;

  @Spy
  private SocketIO io;

  @Spy
  private ByteSend byteSend;

  @Spy
  private Queue<byte[]> incoming;

  @BeforeEach
  void setup() {
    Thread t = Thread.currentThread();
    if (t.isInterrupted()) {
      try {
        t.join();
      } catch (InterruptedException e) {
        log.info("This thread is allowed to be interrupted!");
      }
    }
    ;
  }

  @Test
  void runnableSend_Ok() {

    sendTask.setName("Send thread ");
    Runnable runnable = sendTask.runnableSend(byteSend, incoming, io);
    Thread thread1 = new Thread(runnable);
    assertTimeoutPreemptively(ofSeconds(5), thread1::start);
  }

  @Test
  void runnAbleReceive_InterruptedException() {
    doAnswer((Answer<InterruptedException>) invocation -> {
      Thread t = Thread.currentThread();
      t.interrupt();
      throw new InterruptedException("Interrupted Send thread");
    }).when(byteSend).write(any(), any());
    sendTask.setName("Send thread throw InterruptedException");
    Runnable runnable = sendTask.runnableSend(byteSend, incoming, io);
    Thread thread1 = new Thread(runnable);
    thread1.run();
  }
}
