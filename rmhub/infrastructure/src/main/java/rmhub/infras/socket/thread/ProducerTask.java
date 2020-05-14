package rmhub.infras.socket.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import rmhub.infras.socket.service.DataProducer;

@Component
@Scope("prototype")
@Slf4j
public class ProducerTask {

  private String name;

  public void setName(String name) {
    this.name = name;
  }

  public <P extends DataProducer> Runnable runnableProducer(P producer) {
    return () -> {
      Thread currentThread = Thread.currentThread();
      log.info("Thread {}", name + " is running");
      while (!currentThread.isInterrupted()) {
        try {
          Thread.sleep(100);
          producer.sendToKafka();
        } catch (InterruptedException e) {
          log.error("Thread is interrupted!");
        }
      }
    };
  }

}
