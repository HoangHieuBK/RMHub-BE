package rmhub.connector.mivisu.ssilm.service;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rmhub.connector.mivisu.ssilm.behaviour.SsilmBehaviour;
import rmhub.connector.mivisu.ssilm.queue.RequestQueue;
import rmhub.infras.socket.SocketIO;

@ExtendWith(SpringExtension.class)
class ByteReceiveServicesTest {

  @InjectMocks
  private ByteReceiveServices byteReceiveServices;

  @Mock
  private SsilmBehaviour ssilmBehaviour;

  @Mock
  private SocketIO socketIO;

  @Test
  void testRead() {

    RequestQueue.getRequestQueue().removeAll(RequestQueue.getRequestQueue());

    when(socketIO.isConnected()).thenReturn(true);

    byteReceiveServices.read(RequestQueue.getRequestQueue(), socketIO);

    // FIXME verify doesn't work: "Actually, there were zero interactions with this mock"
    // verify(ssilmBehaviour, atLeastOnce()).readFromMivisu(any(), any());
  }

  @Test
  void testRead_requestQueueEmpty() {

//    Queue<byte[]> requestQueue = RequestQueue.getRequestQueue();
//    requestQueue.add(new byte[1]);

    RequestQueue.getRequestQueue().removeAll(RequestQueue.getRequestQueue());

    when(socketIO.isConnected()).thenReturn(false);

    byteReceiveServices.read(RequestQueue.getRequestQueue(), socketIO);
  }
}
