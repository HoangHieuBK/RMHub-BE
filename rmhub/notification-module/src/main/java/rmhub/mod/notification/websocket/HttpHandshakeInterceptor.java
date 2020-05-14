package rmhub.mod.notification.websocket;

import java.util.Map;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Slf4j
@Component
public class HttpHandshakeInterceptor implements HandshakeInterceptor {

  @Override
  public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
      WebSocketHandler webSocketHandler, Map<String, Object> attributes) {

    if (serverHttpRequest instanceof ServletServerHttpRequest) {

      ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) serverHttpRequest;
      HttpSession session = servletRequest.getServletRequest().getSession();
      attributes.put("sessionId", session.getId());
    }
    return true;
  }

  @Override
  public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
      WebSocketHandler webSocketHandler, Exception e) {
    log.info("Call afterHandshake");
  }
}
