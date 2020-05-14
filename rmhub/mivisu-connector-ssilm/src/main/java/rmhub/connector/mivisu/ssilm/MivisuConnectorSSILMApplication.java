package rmhub.connector.mivisu.ssilm;

import java.util.TimeZone;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@Slf4j
@SpringBootApplication
@ComponentScans(value = {@ComponentScan("rmhub.infras.socket"), @ComponentScan("rmhub.common.kafkaconnector.configs"),
    @ComponentScan("rmhub.common.kafkaconnector.messagebased")})
public class MivisuConnectorSSILMApplication {

  @Value("${rmhub.time-zone:UTC}")
  private String timeZone;

  public static void main(String[] args) {
    SpringApplication.run(MivisuConnectorSSILMApplication.class, args);
  }

  @PostConstruct
  void setTimeZone() {
    TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    log.info("Current time-zone: {}", TimeZone.getDefault().getID());
  }
}
