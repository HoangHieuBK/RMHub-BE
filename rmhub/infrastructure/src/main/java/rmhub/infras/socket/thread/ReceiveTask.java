package rmhub.infras.socket.thread;

import java.util.Queue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import rmhub.infras.socket.SocketIO;
import rmhub.infras.socket.service.ByteReceive;

@Component
@Scope("prototype")
@Slf4j
public class ReceiveTask {

  private String name;

  public void setName(String name) {
    this.name = name;
  }

  public <T extends ByteReceive> Runnable runnableReceive(T t, Queue<byte[]> outgoing,
      SocketIO io) {
    return () -> {
      log.info("Thread {} ", name + " is running");
      Thread tt = Thread.currentThread();
      while (!tt.isInterrupted()) {
        synchronized (io) {
          try {
            Thread.sleep(1000);
            t.read(outgoing, io);
          } catch (InterruptedException e) {
            log.error("Thread is interrupted!");
          }
        }
      }
    };
  }
}
