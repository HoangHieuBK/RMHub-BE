package rmhub.infras.socket.thread;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import rmhub.infras.socket.SocketIO;
import rmhub.infras.socket.service.Connector;

@Component
@Scope("prototype")
@Slf4j
public class ConnectorTask {

  @Setter
  private String name;

  public <T extends Connector> Runnable runnableConnector(T connector, SocketIO io) {
    return () -> {
      log.info("Thread {} ", name + " is running");
      Thread thread = Thread.currentThread();
      while (!thread.isInterrupted()) {
        try {
          Thread.sleep(5000);
          connector.connect(io);
        } catch (InterruptedException e) {
          log.error("Thread is interrupted!");
        }
      }
    };
  }
}
