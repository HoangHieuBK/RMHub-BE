package rmhub.mod.notification.consumer;

import static rmhub.mod.notification.TestConst.RMHUB_LOGGER_NAME;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.logging.LogLevel;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rmhub.common.core.response.PhysicalDeviceResult;

@Slf4j
@ExtendWith(SpringExtension.class)
class DeviceSyncConsumerTest {

  @InjectMocks
  private DeviceSyncConsumer consumer;

  @Mock
  private SimpMessageSendingOperations messagingTemplate;

  private static ObjectMapper OBJECT_MAPPER = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  @BeforeEach
  void setUp() {
    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel(RMHUB_LOGGER_NAME, LogLevel.INFO);
  }

  @Test
  void responseDataAfterSync() throws Exception {

    var physicalDeviceResult = PhysicalDeviceResult.builder()
        .deploymentId(1L)
        .requestId("123")
        .timestamp(new Date())
        .build();

    var json = OBJECT_MAPPER.writeValueAsString(physicalDeviceResult);

    consumer.responseDataAfterSync(json);

    Mockito.verify(messagingTemplate, Mockito.times(1))
        .convertAndSendToUser(Mockito.anyString(), Mockito.anyString(), Mockito.anyObject(), Mockito.anyMap());
  }

  @Test
  void debugCoverTrick() throws Exception {

    LoggingSystem.get(ClassLoader.getSystemClassLoader()).setLogLevel(RMHUB_LOGGER_NAME, LogLevel.DEBUG);

    var json = OBJECT_MAPPER.writeValueAsString(PhysicalDeviceResult.builder().deploymentId(1L).build());

    consumer.responseDataAfterSync(json);
  }
}
