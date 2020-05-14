package rmhub.infras.socket;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SocketIO {

  private Socket socket;
  private InputStream input;
  private OutputStream output;

  public SocketIO() {
    socket = null;
    input = null;
    output = null;
  }

  public void connect(final String ip, final int port) {
    log.info("Socket info ip address {}, port {} ", ip, port);
    close();
    try {
      socket = new Socket(ip, port);
      input = new BufferedInputStream(socket.getInputStream());
      output = new BufferedOutputStream(socket.getOutputStream());
    } catch (IOException e) {
      log.warn("Error, unable to connect to socket {} ", e.getMessage());
    }
  }

  public boolean isConnected() {
    if (socket == null) {
      return false;
    }
    return socket.isConnected();
  }

  public boolean hasNetworkConnection() {
    if (socket != null) {
      try {
        String s = "ping";
        output.write(s.getBytes());
        output.flush();
        return true;
      } catch (IOException e) {
        log.warn("Error lost connection {} ", e.getMessage());
        close();
        return false;
      }
    }
    return false;
  }

  public void close() {
    try {
      if (socket != null) {
        socket.close();
        socket = null;
      }
      if (input != null) {
        input.close();
        input = null;
      }
      if (output != null) {
        output.close();
        output = null;
      }
    } catch (IOException e) {
      log.warn("Error, unable to close the socket: {} ", e.getMessage());
    }
  }

  public InputStream getInputStream() {
    return input;
  }

  public OutputStream getOutputStream() {
    return output;
  }

  public int getReceiveBufferSize() {
    try {
      if (socket != null && socket.isConnected()) {
        return socket.getReceiveBufferSize();
      }
    } catch (SocketException e) {
      log.warn("Error lost connection: {}", e.getMessage());
    }
    return 0;
  }
}
