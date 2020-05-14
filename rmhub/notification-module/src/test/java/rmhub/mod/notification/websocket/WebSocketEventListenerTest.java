package rmhub.mod.notification.websocket;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.messaging.simp.SimpMessageHeaderAccessor.DESTINATION_HEADER;
import static rmhub.mod.notification.TestConst.RMHUB_LOGGER_NAME;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import rmhub.mod.notification.cache.TrafficTechnicalCache;
import rmhub.mod.notification.cache.WeatherMeasurementCache;
import rmhub.mod.notification.constant.Destinations;
import rmhub.model.mivisu.ssil.Et;
import rmhub.model.mivisu.ssil.TechnicalStatus;

@ExtendWith(SpringExtension.class)
class WebSocketEventListenerTest {

  @InjectMocks
  private WebSocketEventListener webSocketEventListener;

  /**
   * not used but still need for mocking
   */
  @Mock
  private SimpMessageSendingOperations messagingTemplate;

  /**
   * not used but still need for mocking
   */
  @Mock
  private WeatherMeasurementCache weatherMeasurementCache;

  @Mock
  private TrafficTechnicalCache trafficTechnicalCache;

  @Mock
  private Message<byte[]> message;

  @BeforeEach
  void setUp() {
    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel(RMHUB_LOGGER_NAME, LogLevel.INFO);
  }

  @Test
  void handleWebSocketConnectListener() {

    webSocketEventListener.handleWebSocketConnectListener(new SessionConnectedEvent(new Object(), new GenericMessage<>(new byte[]{1})));
  }

  @Test
  void handleWebSocketDisconnectListener() {

    Map<String, Object> sessionId = new HashMap<>();
    sessionId.put("sessionId", "EFCEE59FE1FF16CA655FB33824A158E5");

    Map<String, Object> attributes = new HashMap<>();

    attributes.put("simpMessageType", "DISCONNECT");
    attributes.put("stompCommand", "DISCONNECT");
    attributes.put("simpSessionAttributes", sessionId);
    attributes.put("simpSessionId", "469de394-8a08-42f3-aa00-53d9931e5e32");

    when(message.getHeaders()).thenReturn(new MessageHeaders(attributes));

    SessionDisconnectEvent sessionDisconnectEvent = new SessionDisconnectEvent(new Object(),
        message, "sessionId", CloseStatus.NORMAL);

    webSocketEventListener.handleWebSocketDisconnectListener(sessionDisconnectEvent);
  }

  @Test
  void eventClientSubscribe_weather() {

    Map<String, Object> headers = new HashMap<>();
    headers.put(DESTINATION_HEADER, Destinations.WEATHER_DATA);

    Message<byte[]> message = new GenericMessage<>(new byte[]{1}, headers);

    SessionSubscribeEvent sessionSubscribeEvent = new SessionSubscribeEvent(new Object(), message);

    webSocketEventListener.eventClientSubscribe(sessionSubscribeEvent);
  }

  @Test
  void eventClientSubscribe_weather_debugTrick() {

    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel(RMHUB_LOGGER_NAME, LogLevel.DEBUG);

    Map<String, Object> headers = new HashMap<>();
    headers.put(DESTINATION_HEADER, Destinations.WEATHER_DATA);

    Message<byte[]> message = new GenericMessage<>(new byte[]{1}, headers);

    SessionSubscribeEvent sessionSubscribeEvent = new SessionSubscribeEvent(new Object(), message);

    webSocketEventListener.eventClientSubscribe(sessionSubscribeEvent);
  }

  @Test
  void eventClientSubscribe_technical_withEtsIsNull() {

    Map<String, Object> headers = new HashMap<>();
    headers.put(DESTINATION_HEADER, Destinations.TECHNICAL_DATA);

    Message<byte[]> message = new GenericMessage<>(new byte[]{1}, headers);

    SessionSubscribeEvent sessionSubscribeEvent = new SessionSubscribeEvent(new Object(), message);

    when(trafficTechnicalCache.getTrafficTechnicalStatus(anyInt())).thenReturn(new TechnicalStatus());

    webSocketEventListener.eventClientSubscribe(sessionSubscribeEvent);
  }

  @Test
  void eventClientSubscribe_technical_withEts() {

    Map<String, Object> headers = new HashMap<>();
    headers.put(DESTINATION_HEADER, Destinations.TECHNICAL_DATA);

    Message<byte[]> message = new GenericMessage<>(new byte[]{1}, headers);

    SessionSubscribeEvent sessionSubscribeEvent = new SessionSubscribeEvent(new Object(), message);

    when(trafficTechnicalCache.getTrafficTechnicalStatus(anyInt()))
        .thenReturn(TechnicalStatus.builder().ets(Collections.singletonList(new Et())).build());

    webSocketEventListener.eventClientSubscribe(sessionSubscribeEvent);
  }

  @Test
  void eventClientSubscribe_technical_withEts_debugTrick() {

    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel(RMHUB_LOGGER_NAME, LogLevel.DEBUG);

    Map<String, Object> headers = new HashMap<>();
    headers.put(DESTINATION_HEADER, Destinations.TECHNICAL_DATA);

    Message<byte[]> message = new GenericMessage<>(new byte[]{1}, headers);

    SessionSubscribeEvent sessionSubscribeEvent = new SessionSubscribeEvent(new Object(), message);

    when(trafficTechnicalCache.getTrafficTechnicalStatus(anyInt()))
        .thenReturn(TechnicalStatus.builder().ets(Collections.singletonList(new Et())).build());

    webSocketEventListener.eventClientSubscribe(sessionSubscribeEvent);
  }

  @Test
  void eventClientSubscribe_otherwise() {

    Map<String, Object> headers = new HashMap<>();
    headers.put(DESTINATION_HEADER, "dummy");

    Message<byte[]> message = new GenericMessage<>(new byte[]{1}, headers);

    SessionSubscribeEvent sessionSubscribeEvent = new SessionSubscribeEvent(new Object(), message);

    webSocketEventListener.eventClientSubscribe(sessionSubscribeEvent);
  }
}
