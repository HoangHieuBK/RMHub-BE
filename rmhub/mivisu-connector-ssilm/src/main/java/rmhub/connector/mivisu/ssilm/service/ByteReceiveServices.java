package rmhub.connector.mivisu.ssilm.service;

import java.util.Queue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rmhub.connector.mivisu.ssilm.behaviour.SsilmBehaviour;
import rmhub.connector.mivisu.ssilm.queue.RequestQueue;
import rmhub.infras.socket.SocketIO;
import rmhub.infras.socket.service.ByteReceive;

@Service
@Slf4j
public class ByteReceiveServices implements ByteReceive {

  @Autowired
  private SsilmBehaviour ssilmBehaviour;

  @Value("${mivisu.socket.ip}")
  private String ipAddress;

  @Override
  public void read(Queue<byte[]> outgoing, SocketIO io) {
    Queue<byte[]> requestQueue = RequestQueue.getRequestQueue();
    if (requestQueue.isEmpty() && io.isConnected()) {
      log.info("Byte receive response");
      ssilmBehaviour.readFromMivisu(io, outgoing);
    }
  }
}
