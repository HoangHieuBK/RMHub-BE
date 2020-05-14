package rmhub.mod.notification.websocket;

import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import rmhub.common.utility.JsonHelper;
import rmhub.mod.notification.cache.TrafficTechnicalCache;
import rmhub.mod.notification.cache.WeatherMeasurementCache;
import rmhub.mod.notification.constant.Destinations;
import rmhub.model.mivisu.ssil.AlertResponse;
import rmhub.model.mivisu.ssil.CacheWeatherMeasure;
import rmhub.model.mivisu.ssil.Et;
import rmhub.model.mivisu.ssil.TechnicalStatus;

@Slf4j
@Component
public class WebSocketEventListener {

  @Autowired
  private SimpMessageSendingOperations messagingTemplate;

  @Autowired
  private WeatherMeasurementCache weatherMeasurementCache;

  @Autowired
  private TrafficTechnicalCache trafficTechnicalCache;

  private static final int DEPLOYMENT_ID = 1;

  @EventListener
  public void handleWebSocketConnectListener(SessionConnectedEvent event) {

    log.info("simpSessionId = {}", event.getMessage().getHeaders().get("simpSessionId"));
    log.info("event = {}", event);
    log.info("Received a new web socket connection.");
  }

  @EventListener
  public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

    StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    String sessionId = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("sessionId");

    log.info("Session closed: {}", sessionId);
  }

  @EventListener
  public void eventClientSubscribe(SessionSubscribeEvent sessionSubscribeEvent) {

    SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(sessionSubscribeEvent.getMessage());
    log.info("Server receive a subscribe event: {}", headers.getDestination());

    if (Destinations.WEATHER_DATA.equals(headers.getDestination())) {

      AlertResponse alertResponse = weatherMeasurementCache.getWeatherMeasure(DEPLOYMENT_ID);
      CacheWeatherMeasure weatherMeasureData = new CacheWeatherMeasure();

      weatherMeasureData.setAlertResponse(alertResponse);
      String data = JsonHelper.convertObject2Json(alertResponse);

      if (log.isDebugEnabled()) {
        log.debug("weather measure data from subscribe event: {}", data);
      }

      messagingTemplate.convertAndSend(Destinations.WEATHER_DATA, data);

      log.info("Message sent to: {}!", Destinations.WEATHER_DATA);

    } else if (Destinations.TECHNICAL_DATA.equals(headers.getDestination())) {

      log.info("technical data from subscribe event");

      TechnicalStatus technicalStatus = trafficTechnicalCache.getTrafficTechnicalStatus(DEPLOYMENT_ID);

      List<Et> ets = technicalStatus.getEts();

      if (ets == null) {
        return;
      }

      for (Et et : ets) {
        et.setInCache(true);
      }
      String data = JsonHelper.convertObject2Json(technicalStatus);

      if (log.isDebugEnabled()) {
        log.debug("technical data from subscribe event: {}", data);
      }

      messagingTemplate.convertAndSend(Destinations.TECHNICAL_DATA, data);

      log.info("Message sent to: {}!", Destinations.TECHNICAL_DATA);
    }
  }
}
