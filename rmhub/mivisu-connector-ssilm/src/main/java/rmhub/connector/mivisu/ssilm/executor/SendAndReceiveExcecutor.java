package rmhub.connector.mivisu.ssilm.executor;

import java.util.Queue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import rmhub.connector.mivisu.ssilm.queue.RequestQueue;
import rmhub.connector.mivisu.ssilm.queue.ResponseQueue;
import rmhub.connector.mivisu.ssilm.service.ByteReceiveServices;
import rmhub.connector.mivisu.ssilm.service.ByteSendServices;
import rmhub.connector.mivisu.ssilm.service.ConnectorService;
import rmhub.connector.mivisu.ssilm.service.ProducerService;
import rmhub.infras.socket.SocketIO;
import rmhub.infras.socket.thread.ConnectorTask;
import rmhub.infras.socket.thread.ProducerTask;
import rmhub.infras.socket.thread.ReceiveTask;
import rmhub.infras.socket.thread.SendTask;

@Component
@Scope("prototype")
@Slf4j
public class SendAndReceiveExcecutor implements CommandLineRunner {

  @Autowired
  private ByteSendServices byteSend;

  @Autowired
  private SendTask sendThread;

  @Autowired
  private SocketIO io;

  @Autowired
  private ConnectorTask connectorTask;

  @Autowired
  private ProducerTask producerTask;

  @Autowired
  private ReceiveTask receiveTask;

  @Autowired
  private ThreadPoolTaskExecutor taskExecutor;

  @Autowired
  private ByteReceiveServices byteReceive;

  @Autowired
  private ConnectorService connectorService;

  @Autowired
  private ProducerService producerService;

  @Override
  public void run(String... args) throws Exception {
    synchronized (io) {
      connectorExecutor();
      Thread.sleep(1500);
      sendExcecutor();
      Thread.sleep(1500);
      receiveExecutor();
      Thread.sleep(1500);
      producerExcecutor();
    }
  }

  private void sendExcecutor() {
    sendThread.setName("Send");
    taskExecutor.execute(sendThread.runnableSend(byteSend, RequestQueue.getRequestQueue(), io));
  }

  private void receiveExecutor() {
    log.info("Calling receiveExecutor");
    receiveTask.setName("Receive");
    Queue<byte[]> outgoing = ResponseQueue.getResponseQueue();
    taskExecutor.execute(receiveTask.runnableReceive(byteReceive, outgoing, io));
  }

  private void connectorExecutor() {
    log.info("Calling connectorExecutor");
    connectorTask.setName("Connector");
    taskExecutor.execute(connectorTask.runnableConnector(connectorService, io));
  }

  private void producerExcecutor() {
    log.info("Calling producerExcecutor");
    producerTask.setName("Producer");
    taskExecutor.execute(producerTask.runnableProducer(producerService));
  }
}
