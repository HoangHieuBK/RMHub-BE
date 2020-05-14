package rmhub.mod.weatherstation.repository;

import static rmhub.mod.weatherstation.constant.AlertRuleCode.ALR_WIND_LEVEL1;
import static rmhub.mod.weatherstation.constant.AlertRuleCode.ALR_WIND_LEVEL2;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import rmhub.mod.weatherstation.entity.EntityPackageMarker;
import rmhub.mod.weatherstation.entity.WeatherAlertRule;

@Disabled("In this phase we need UT only")
@DataJpaTest
@EntityScan(basePackageClasses = EntityPackageMarker.class)
@EnableJpaRepositories(basePackageClasses = RepoPackageMarker.class)
public class AlertSettingsRepoIT {

  @Autowired
  private AlertSettingsRepo alertSettingsRepo;

  // I remove @DirtiesContext so that the entity Id is not "1" anymore
  private Long idThatExists;

  @BeforeEach
  public void beforeEach() {
    var rule = WeatherAlertRule.builder().alertCode(ALR_WIND_LEVEL1).content("ALR_WIND_LEVEL1 alert rule to find").level(1)
        .condition(1).value(100)
        .status(true).color("red").deploymentId(1).build();
    idThatExists = alertSettingsRepo.save(rule).getId();
  }

  @Test
  public void findByStatus_findOk() {
    List<WeatherAlertRule> lsResult = alertSettingsRepo.find(true, 1);

    Assertions.assertEquals(ALR_WIND_LEVEL1, lsResult.get(0).getAlertCode());
    Assertions.assertEquals("ALR_WIND_LEVEL1 alert rule to find", lsResult.get(0).getContent());
    Assertions.assertEquals(100, lsResult.get(0).getValue().intValue());

    List<WeatherAlertRule> lsResult2 = alertSettingsRepo.find(false, 1);
    Assertions.assertEquals(0, lsResult2.size());
  }

  @Test
  public void findByAlertCode_findOk() {

    WeatherAlertRule result = alertSettingsRepo.find(ALR_WIND_LEVEL1, true, 1);
    Assertions.assertEquals(ALR_WIND_LEVEL1, result.getAlertCode());
    Assertions.assertEquals("ALR_WIND_LEVEL1 alert rule to find", result.getContent());
    Assertions.assertEquals(100, result.getValue().intValue());

    WeatherAlertRule result2 = alertSettingsRepo.find(ALR_WIND_LEVEL2, true, 1);
    Assertions.assertNull(result2);
  }

  @Test
  public void findById_findOk() {
    WeatherAlertRule result = alertSettingsRepo.findById(idThatExists).orElseThrow();

    Assertions.assertEquals(ALR_WIND_LEVEL1, result.getAlertCode());
    Assertions.assertEquals("ALR_WIND_LEVEL1 alert rule to find", result.getContent());
    Assertions.assertEquals(100, result.getValue().intValue());
  }

  @Test
  public void findExistsValue_findWhenUpdate() {
    List<WeatherAlertRule> result = alertSettingsRepo.findExistsRuleByValue(idThatExists, 100, true, 1);
    Assertions.assertEquals(0, result.size());
  }

  @Test
  public void findExistsValue_findWhenCreate() {
    List<WeatherAlertRule> result = alertSettingsRepo.findExistsRuleByValue(null, 100, true, 1);
    Assertions.assertNotNull(result);
  }

  @Test
  public void findByAlertCodeNotStatus_findOk() {
    WeatherAlertRule result = alertSettingsRepo.findByAlertCode(ALR_WIND_LEVEL1, 1)
        .stream().filter(WeatherAlertRule::getStatus).findFirst().orElseThrow();

    Assertions.assertEquals(ALR_WIND_LEVEL1, result.getAlertCode());
    Assertions.assertEquals("ALR_WIND_LEVEL1 alert rule to find", result.getContent());
    Assertions.assertEquals(100, result.getValue().intValue());
  }

  @Test
  public void findExistsRuleByValue_findOk() {
    WeatherAlertRule result = alertSettingsRepo.findExistsRuleByValue(null, 100, true, 1)
        .stream().filter(WeatherAlertRule::getStatus).findFirst().orElseThrow();

    Assertions.assertEquals(ALR_WIND_LEVEL1, result.getAlertCode());
    Assertions.assertEquals("ALR_WIND_LEVEL1 alert rule to find", result.getContent());
    Assertions.assertEquals(100, result.getValue().intValue());
  }
}
