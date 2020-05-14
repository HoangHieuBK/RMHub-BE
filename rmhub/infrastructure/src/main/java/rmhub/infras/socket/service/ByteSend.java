package rmhub.infras.socket.service;

import java.util.Queue;
import rmhub.infras.socket.SocketIO;

public interface ByteSend {
    void write(Queue<byte[]> queue, SocketIO io);
}
