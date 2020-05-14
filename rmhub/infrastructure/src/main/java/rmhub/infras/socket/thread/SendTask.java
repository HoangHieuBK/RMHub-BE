package rmhub.infras.socket.thread;

import java.util.Queue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import rmhub.infras.socket.SocketIO;
import rmhub.infras.socket.service.ByteSend;

@Component
@Scope("prototype")
@Slf4j
public class SendTask {

  private String name;

  public void setName(String name) {
    this.name = name;
  }

  public <T extends ByteSend> Runnable runnableSend(T t, Queue<byte[]> incoming, SocketIO io) {
    return () -> {
      Thread tt = Thread.currentThread();
      log.info("Thread {}", name + " is running");
      while (!tt.isInterrupted()) {
        try {
          Thread.sleep(100);
          t.write(incoming, io);
        } catch (InterruptedException e) {
          log.error("Thread is interrupted!");
        }
      }
    };
  }

}
