package rmhub.infras.socket;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Random;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(SpringExtension.class)
class SocketIOTest {

  @InjectMocks
  private SocketIO socketIO;

  @Mock
  private Socket socket;

  @Mock
  private InputStream input;

  @Mock
  private OutputStream output;

  @Test
  void createSocket() {
    SocketIO io = new SocketIO();
    assertNotNull(io);
  }

  @Test
  void connect_Ok() throws Exception {
    InputStream in = new InputStream() {
      @Override
      public int read() {
        return 0;
      }
    };
    OutputStream out = new OutputStream() {
      @Override
      public void write(int b) {
      }
    };
    int port = (new Random()).nextInt(9999 - 1000);
    ServerSocket mockServerSocket = spy(new ServerSocket(port, 0, InetAddress.getByName(null)));

    socketIO.connect("127.0.0.1", port);
    Assertions.assertNotNull(output);
    mockServerSocket.close();
  }

  @Test
  void connect_err() throws IOException {
    socketIO.connect("127.0.0.1", 6868);
    verify(socket, never()).getInputStream();
  }

  @Test
  void isConnected_socketNotNull() {
    when(socket.isConnected()).thenReturn(true);
    Assertions.assertTrue(socketIO.isConnected());
  }

  @Test
  void isConnected_socketNull() {
    ReflectionTestUtils.setField(socketIO, "socket", null);
    Assertions.assertFalse(socketIO.isConnected());
  }

  @Test
  void hasNetworkConnection_socketNull() {
    ReflectionTestUtils.setField(socketIO, "socket", null);
    Assertions.assertFalse(socketIO.hasNetworkConnection());
  }

  @Test
  void hasNetworkConnection_socketNotNull() {
    Assertions.assertTrue(socketIO.hasNetworkConnection());
  }

  @Test
  void hasNetworkConnection_exception() throws IOException {
    doThrow(new IOException()).when(output).write(any());
    Assertions.assertFalse(socketIO.hasNetworkConnection());
  }

  @Test
  void close_socketNotNull() throws IOException {
    socketIO.close();
    verify(socket, times(1)).close();
  }

  @Test
  void close_socketNull() throws IOException {
    ReflectionTestUtils.setField(socketIO, "socket", null);
    socketIO.close();
    verify(socket, never()).close();
  }

  @Test
  void close_exception() throws IOException {
    doThrow(new IOException()).when(socket).close();
    socketIO.close();
    verify(input, never()).close();
  }

  @Test
  void close_inputNotNull() throws IOException {
    socketIO.close();
    verify(input, times(1)).close();
  }

  @Test
  void close_inputNull() throws IOException {
    ReflectionTestUtils.setField(socketIO, "input", null);
    socketIO.close();
    verify(input, never()).close();
  }

  @Test
  void close_outputNotNull() throws IOException {
    socketIO.close();
    verify(output, times(1)).close();
  }

  @Test
  void close_outputNull() throws IOException {
    ReflectionTestUtils.setField(socketIO, "output", null);
    socketIO.close();
    verify(output, never()).close();
  }

  @Test
  void getInputStream_Ok() {
    Assertions.assertNotNull(socketIO.getInputStream());
  }

  @Test
  void getOutputStream_Ok() {
    Assertions.assertNotNull(socketIO.getOutputStream());
  }

  @Test
  void getReceiveBufferSize_Ok() throws SocketException {
    when(socket.isConnected()).thenReturn(true);
    when(socket.getReceiveBufferSize()).thenReturn(1);
    Assertions.assertEquals(1, socketIO.getReceiveBufferSize());
  }

  @Test
  void getReceiveBufferSize_SkNull() {
    ReflectionTestUtils.setField(socketIO, "socket", null);
    Assertions.assertEquals(0, socketIO.getReceiveBufferSize());
  }

  @Test
  void getReceiveBufferSize_SkNotConnect() {
    when(socket.isConnected()).thenReturn(false);
    Assertions.assertEquals(0, socketIO.getReceiveBufferSize());
  }

  @Test
  void getReceiveBufferSize_SkNotConnectSkNull() {
    ReflectionTestUtils.setField(socketIO, "socket", null);
    Assertions.assertEquals(0, socketIO.getReceiveBufferSize());
  }

  @Test
  void getReceiveBufferSize_SocketException() throws SocketException {
    when(socket.isConnected()).thenReturn(true);
    doThrow(new SocketException()).when(socket).getReceiveBufferSize();
    Assertions.assertEquals(0, socketIO.getReceiveBufferSize());
  }
}
