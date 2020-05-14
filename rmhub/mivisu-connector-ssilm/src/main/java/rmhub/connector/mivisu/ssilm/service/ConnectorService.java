package rmhub.connector.mivisu.ssilm.service;

import java.util.Queue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import rmhub.connector.mivisu.ssilm.queue.RequestQueue;
import rmhub.infras.socket.SocketIO;
import rmhub.infras.socket.service.Connector;

@Service
@Slf4j
public class ConnectorService implements Connector {

  @Value("${mivisu.request.collection}")
  private String requestCollection;

  @Value("${mivisu.socket.ip}")
  private String ipAddress;

  @Value("${mivisu.socket.port}")
  private int port;

  private void initCommandsToMivisu() {
    Queue<byte[]> requestQueue = RequestQueue.getRequestQueue();
    if (requestQueue.isEmpty()) {
      String[] arrOfCmd = requestCollection.split("::");
      for (String cmd : arrOfCmd) {
        RequestQueue.pushRequestInfo(cmd.getBytes());
      }
    }
  }

  @Override
  public void connect(SocketIO io) {
    if (io != null && !io.hasNetworkConnection()) {
      synchronized (io) {
        io.connect(ipAddress, port);
        initCommandsToMivisu();
      }
    }
  }
}
