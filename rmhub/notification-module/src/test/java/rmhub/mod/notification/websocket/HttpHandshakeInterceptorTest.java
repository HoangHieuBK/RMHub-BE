package rmhub.mod.notification.websocket;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.socket.WebSocketHandler;

class HttpHandshakeInterceptorTest {

  /**
   * the instance to be tested
   */
  private HttpHandshakeInterceptor interceptor = new HttpHandshakeInterceptor();

  /**
   * we instantiate a {@link MockHttpServletRequest} instead of using {@link Mock}
   */
  private HttpServletRequest httpServletRequest = new MockHttpServletRequest();

  @Mock
  private ServerHttpRequest serverHttpRequest;

  @Mock
  private ServerHttpResponse serverHttpResponse;

  @Mock
  private WebSocketHandler webSocketHandler;

  @Test
  void beforeHandshake_notServletServerHttpRequest() {

    assertTrue(interceptor.beforeHandshake(serverHttpRequest, serverHttpResponse, webSocketHandler, new HashMap<>()));
  }

  @Test
  void beforeHandshake() {

    assertTrue(interceptor.beforeHandshake(
        new ServletServerHttpRequest(httpServletRequest), serverHttpResponse, webSocketHandler, new HashMap<>()));
  }

  @Test
  void afterHandshake() {

    interceptor.afterHandshake(
        new ServletServerHttpRequest(httpServletRequest), serverHttpResponse, webSocketHandler, new Exception("fake exception"));
  }
}
