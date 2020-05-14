package rmhub.mod.devicemgmt;

import java.util.TimeZone;
import javax.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class DeviceMgmtModuleApplication {

  @Value("${rmhub.time-zone:UTC}")
  private String timeZone;

  public static void main(String[] args) {
    SpringApplication.run(DeviceMgmtModuleApplication.class, args);
  }

  @PostConstruct
  void setTimeZone() {
    TimeZone.setDefault(TimeZone.getTimeZone(timeZone));
    log.info("Current time-zone: {}", TimeZone.getDefault().getID());
  }
}
