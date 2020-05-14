package rmhub.mod.notification;

import java.util.TimeZone;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@Slf4j
@SpringBootApplication
@CrossOrigin("*")
public class NotificationModuleApplication {

  @Value("${rmhub.time-zone:UTC}")
  private String timeZone;

  public static void main(String[] args) {
    SpringApplication.run(NotificationModuleApplication.class, args);
  }

  @PostConstruct
  void setTimeZone() {
    TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    log.info("Current time-zone: {}", TimeZone.getDefault().getID());
  }
}
