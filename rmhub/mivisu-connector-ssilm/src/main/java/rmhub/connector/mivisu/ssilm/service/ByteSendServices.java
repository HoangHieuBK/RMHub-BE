package rmhub.connector.mivisu.ssilm.service;

import java.util.Queue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rmhub.connector.mivisu.ssilm.behaviour.SsilmBehaviour;
import rmhub.infras.socket.SocketIO;
import rmhub.infras.socket.service.ByteSend;

@Service
@Slf4j
public class ByteSendServices implements ByteSend {

  @Autowired
  private SsilmBehaviour ssilmBehaviour;

  @Value("${mivisu.socket.ip}")
  private String ipAddress;

  public void write(Queue<byte[]> incoming, SocketIO io) {
    try {
      if (!incoming.isEmpty() && io.isConnected()) {
        log.info("write to mivisu");
        Thread.sleep(1000);
        ssilmBehaviour.writeToMivisu(incoming, io);
        Thread.sleep(1000);
      }
    } catch (InterruptedException e) {
      log.warn("Error at send service: {} ", e.getMessage());
    }
  }
}
