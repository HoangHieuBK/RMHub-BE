package rmhub.connector.mivisu.ssilm.service;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import rmhub.connector.mivisu.ssilm.queue.RequestQueue;
import rmhub.infras.socket.SocketIO;

@ExtendWith(SpringExtension.class)
class ConnectorServiceTest {

  @InjectMocks
  private ConnectorService connectorService;

  @Mock
  private SocketIO socketIO;

  @BeforeEach
  void setupBefore() {
    ReflectionTestUtils.setField(connectorService, "requestCollection",
        "<MIVISU><info_txt>Externe 2 Mesure.Demande identification</info_txt><CORPS><abo>LABOCOM</abo><pwd>labocom</pwd></CORPS><ENT><type>w</type><version>2</version><lgmes>0</lgmes><id>A123</id></ENT></MIVISU>::<MIVISU><CORPS></CORPS><ENT><type>y</type><version>1</version><lgmes>0</lgmes><id>ZYX</id></ENT></MIVISU>");
  }

  @Test
  void testConnector_ok() {
    RequestQueue.getRequestQueue().removeAll(RequestQueue.getRequestQueue());
    connectorService.connect(socketIO);
  }

  @Test
  void testConnector_notOk() {
    RequestQueue.getRequestQueue().removeAll(RequestQueue.getRequestQueue());

    Thread.currentThread().interrupt();

    connectorService.connect(socketIO);
  }

  @Test
  void testConnector_null() {
    connectorService.connect(null);
  }

  @Test
  void testConnector_hasNetworkConnection_false() {
    when(socketIO.hasNetworkConnection()).thenReturn(false);
    connectorService.connect(socketIO);
  }

  @Test
  void testConnector_hasNetworkConnection_true() {
    when(socketIO.hasNetworkConnection()).thenReturn(true);
    connectorService.connect(socketIO);
  }
}
