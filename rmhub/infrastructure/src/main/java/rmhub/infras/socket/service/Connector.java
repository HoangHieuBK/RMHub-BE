package rmhub.infras.socket.service;

import rmhub.infras.socket.SocketIO;

public interface Connector {
  void connect(SocketIO io);
}
