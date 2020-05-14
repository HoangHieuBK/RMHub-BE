package rmhub.mod.trafficlogger.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import rmhub.mod.trafficlogger.common.StatusEnum;
import rmhub.mod.trafficlogger.entity.EntityPackageMarker;
import rmhub.mod.trafficlogger.entity.TrafficAlertSetting;

@Disabled("In this phase we need UT only")
@DataJpaTest
@EntityScan(basePackageClasses = EntityPackageMarker.class)
@EnableJpaRepositories(basePackageClasses = RepoPackageMarker.class)
class TrafficAlertSettingRepoIT {

  private static final TrafficAlertSetting DUMMY =
      TrafficAlertSetting.builder().id(1L).status(StatusEnum.ACTIVE).level(5).build();

  @Autowired
  private TrafficAlertSettingRepo repository;

  @BeforeEach
  void setup() {
    repository.save(DUMMY);
  }

  @Test
  void findById_shouldReturnExactly() {
    Assertions.assertEquals(DUMMY, repository.findById(1L).get());
  }
}
