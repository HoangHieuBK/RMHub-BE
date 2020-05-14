package rmhub.connector.mivisu.ssilm.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Queue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rmhub.connector.mivisu.ssilm.behaviour.SsilmBehaviour;
import rmhub.connector.mivisu.ssilm.queue.RequestQueue;
import rmhub.infras.socket.SocketIO;

@ExtendWith(SpringExtension.class)
class ByteSendServicesTest {

  @InjectMocks
  private ByteSendServices byteSendServices;

  @Mock
  private SsilmBehaviour ssilmBehaviour;

  @Mock
  private SocketIO socketIO;

  @Test
  void testWrite_OK() {
    Queue<byte[]> requestQueue = RequestQueue.getRequestQueue();
    requestQueue.add(new byte[1]);

    when(socketIO.isConnected()).thenReturn(true);

    byteSendServices.write(requestQueue, socketIO);

    verify(ssilmBehaviour, atLeastOnce()).writeToMivisu(any(), any());
  }

  @Test
  void testWrite_notOK() {
    Queue<byte[]> requestQueue = RequestQueue.getRequestQueue();
    requestQueue.add(new byte[1]);

    when(socketIO.isConnected()).thenReturn(true);

    Thread.currentThread().interrupt();

    byteSendServices.write(requestQueue, socketIO);

    verify(ssilmBehaviour, never()).writeToMivisu(any(), any());
  }
}
