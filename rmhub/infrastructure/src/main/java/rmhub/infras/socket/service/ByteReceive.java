package rmhub.infras.socket.service;

import java.util.Queue;
import rmhub.infras.socket.SocketIO;

public interface ByteReceive {
  void read(Queue<byte[]> outgoing, SocketIO io);
}
