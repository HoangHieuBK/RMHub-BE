package rmhub.infras.socket.thread;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.stubbing.Answer;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rmhub.infras.socket.SocketIO;
import rmhub.infras.socket.service.Connector;

@Slf4j
@ExtendWith(SpringExtension.class)
class ConnectorTaskTest {

  @InjectMocks
  private ConnectorTask connectorTask;

  @Spy
  private SocketIO io;

  @Spy
  private Connector connector;

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
  }

  @Test
  void runnableConnector_InterruptedException() {
    doAnswer((Answer<InterruptedException>) invocation -> {
      Thread t = Thread.currentThread();
      t.interrupt();
      throw new InterruptedException("Interrupted Connect thread");
    }).when(connector).connect(any());

    connectorTask.setName("Connect thread throw InterruptedException");
    Runnable runnable = connectorTask.runnableConnector(connector, io);
    Thread thread1 = new Thread(runnable);
    thread1.run();
  }

  @Test
  void runnableConnector_Ok() {

    connectorTask.setName("Connect thread ");
    Runnable runnable = connectorTask.runnableConnector(connector, io);
    Thread thread1 = new Thread(runnable);
    thread1.start();
  }

}
