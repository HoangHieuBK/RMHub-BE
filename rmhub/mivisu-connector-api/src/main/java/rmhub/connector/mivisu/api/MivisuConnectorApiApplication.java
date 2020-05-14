package rmhub.connector.mivisu.api;

import java.util.TimeZone;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

@Slf4j
@SpringBootApplication
@ComponentScans(value = {@ComponentScan("rmhub.connector.mivisu.api.config"), @ComponentScan("rmhub.common.kafkaconnector.configs"),
    @ComponentScan("rmhub.common.kafkaconnector.messagebased")})
public class MivisuConnectorApiApplication {

  @Value("${rmhub.time-zone:UTC}")
  private String timeZone;

  @Value("${rmhub.mivisu.api.url}")
  private String mivisuApiUrl;

  public static void main(String[] args) {
    SpringApplication.run(MivisuConnectorApiApplication.class, args);
  }

  @Bean
  CommandLineRunner commandLineRunner() {
    return args -> log.info("Mivisu API url: {}", mivisuApiUrl);
  }

  @PostConstruct
  void setTimeZone() {
    TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    log.info("Current time-zone: {}", TimeZone.getDefault().getID());
  }
}
