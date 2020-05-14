package rmhub.mod.trafficlogger.consumer;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rmhub.mod.trafficlogger.entity.TechnicalData;
import rmhub.mod.trafficlogger.producer.TechnicalProducer;
import rmhub.mod.trafficlogger.service.TechnicalDataService;

@ExtendWith(SpringExtension.class)
class TechnicalConsumerTest {

  @InjectMocks
  private TechnicalConsumer technicalConsumerService;

  @Mock
  private TechnicalProducer technicalProducer;

  @Mock
  private TechnicalDataService technicalDataService;

  @Test
  void testListenRequestFromKafka() throws Exception {

    File file = new ClassPathResource("dataTechnicalDataTest.json").getFile();

    BufferedReader br = new BufferedReader(new FileReader(file));

    String st;
    StringBuilder tmp = new StringBuilder();
    while ((st = br.readLine()) != null) {
      tmp.append(st);
    }

    technicalConsumerService.listenRequestFromKafka(tmp.toString());

    verify(technicalDataService, atLeastOnce()).create(Mockito.any(TechnicalData.class));
  }

  @Test
  void testListenRequestFromKafka_false() throws Exception {

    File file = new ClassPathResource("dataTechnicalDataTest_false.json").getFile();

    BufferedReader br = new BufferedReader(new FileReader(file));

    String st;
    StringBuilder tmp = new StringBuilder();
    while ((st = br.readLine()) != null) {
      tmp.append(st);
    }

    technicalConsumerService.listenRequestFromKafka(tmp.toString());

    verify(technicalDataService, Mockito.never()).create(Mockito.any(TechnicalData.class));
  }
}
