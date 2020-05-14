package rmhub.connector.mivisu.ssilm.service;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Queue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import rmhub.common.kafkaconnector.messagebased.KafkaProducible;
import rmhub.connector.mivisu.ssilm.behaviour.SsilmBehaviour;
import rmhub.connector.mivisu.ssilm.queue.ResponseQueue;

@ExtendWith(SpringExtension.class)
class ProducerServiceTest {

  @InjectMocks
  private ProducerService producerService;

  @Mock
  private KafkaProducible<String, String> responseToKafka;

  @Spy
  private SsilmBehaviour ssilmBehaviour;

  @Test
  void testSendToKafka() throws Exception {
    File file = new ClassPathResource("dataTestSendMivisuResponseTypeA.xml").getFile();

    BufferedReader br = new BufferedReader(new FileReader(file));

    String st;
    StringBuilder tmp = new StringBuilder();
    while ((st = br.readLine()) != null) {
      tmp.append(st);
    }

    Queue<byte[]> outgoing = ResponseQueue.getResponseQueue();

    outgoing.add(tmp.toString().getBytes());

    ReflectionTestUtils.setField(ssilmBehaviour, "topicWs", "weather.station");
    ReflectionTestUtils.setField(ssilmBehaviour, "topicTc", "traffic.counting");

    producerService.sendToKafka();

    verify(responseToKafka, atLeastOnce()).send(Mockito.anyString(), Mockito.anyString());

    ResponseQueue.getResponseQueue().clear();
  }

  @Test
  void testSendToKafka_mapNull() throws Exception {
    File file = new ClassPathResource("dataTestSendMivisuResponseTypeACentralNull.xml").getFile();

    BufferedReader br = new BufferedReader(new FileReader(file));

    String st;
    StringBuilder tmp = new StringBuilder();
    while ((st = br.readLine()) != null) {
      tmp.append(st);
    }

    Queue<byte[]> outgoing = ResponseQueue.getResponseQueue();

    outgoing.add(tmp.toString().getBytes());

    ReflectionTestUtils.setField(ssilmBehaviour, "topicWs", "weather.station");
    ReflectionTestUtils.setField(ssilmBehaviour, "topicTc", "traffic.counting");

    producerService.sendToKafka();

    ResponseQueue.getResponseQueue().clear();
  }
}
