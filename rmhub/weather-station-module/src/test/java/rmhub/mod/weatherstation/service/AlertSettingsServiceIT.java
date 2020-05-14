package rmhub.mod.weatherstation.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static rmhub.mod.weatherstation.constant.AlertRuleCode.ALR_ES_LVL5_WHITE_FROZEN;
import static rmhub.mod.weatherstation.constant.AlertRuleCode.ALR_WIND_LEVEL1;
import static rmhub.mod.weatherstation.constant.AlertRuleCode.ALR_WIND_LEVEL2;
import static rmhub.mod.weatherstation.constant.AlertRuleCommon.ALERT_RULE_NOT_FOUND;
import static rmhub.mod.weatherstation.constant.AlertRuleCommon.DUPLICATE_VALUE_NOT_ALLOW;
import static rmhub.mod.weatherstation.constant.AlertRuleCommon.EXISTS_RULE_CODE;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.HttpStatus;
import rmhub.common.exception.BusinessException;
import rmhub.mod.weatherstation.constant.AlertRuleCommon;
import rmhub.mod.weatherstation.entity.EntityPackageMarker;
import rmhub.mod.weatherstation.entity.WeatherAlertRule;
import rmhub.mod.weatherstation.helper.HelperPackageMarker;
import rmhub.mod.weatherstation.repository.AlertSettingsRepo;
import rmhub.mod.weatherstation.repository.RepoPackageMarker;
import rmhub.model.weatherStation.AlertRule;

@Disabled("In this phase we need UT only")
@DataJpaTest
@EntityScan(basePackageClasses = EntityPackageMarker.class)
@EnableJpaRepositories(basePackageClasses = RepoPackageMarker.class)
@ComponentScan(basePackageClasses = {ServicePackageMarker.class, HelperPackageMarker.class})
public class AlertSettingsServiceIT {

  @Autowired
  private AlertSettingsService alertSettingsService;

  @Autowired
  private AlertSettingsRepo alertSettingsRepo;

  @BeforeEach
  public void beforeAll() {
    WeatherAlertRule rule = WeatherAlertRule.builder().id(1L).alertCode(ALR_WIND_LEVEL1).content("ALR_WIND_LEVEL1 alert rule exists")
        .condition(1).value(80).status(true).level(1)
        .color("red").deploymentId(1).build();
    alertSettingsRepo.save(rule);
  }

  @Test
  public void createWeatherAlertRule_addNewOK() {
    AlertRule rule = new AlertRule(null, ALR_WIND_LEVEL2, "ALR_WIND_LEVEL1 alert rule to add", 1, 100, 2, "red", 1);
    AlertRule result = alertSettingsService.create(rule);
    Assertions.assertNotNull(result);
  }

  @Test
  public void createWeatherAlertRule_ExistsRuleCode() {
    AlertRule rule = new AlertRule(null, ALR_WIND_LEVEL1, "ALR_WIND_LEVEL1 alert rule to add", 3, 80, 1, "red", 1);
    BusinessException exception = assertThrows(BusinessException.class, () -> alertSettingsService.create(rule));
    assertEquals(EXISTS_RULE_CODE, exception.getMessage());
  }

  @Test
  public void createWeatherAlertRule_notAlowOperationRule2() {
    AlertRule rule2 = new AlertRule(null, ALR_WIND_LEVEL2, "ALR_WIND_LEVEL2 alert rule to add", 2, 120, 1, "red", 1);
    BusinessException exception = assertThrows(BusinessException.class, () -> alertSettingsService.create(rule2));
    assertEquals(AlertRuleCommon.AIR_OPERATION_NOT_ALLOW, exception.getMessage());
  }

  @Test
  public void createWeatherAlertRule_notAlowSmallerRule2() {
    AlertRule rule2 = new AlertRule(null, ALR_WIND_LEVEL2, "ALR_WIND_LEVEL2 alert rule to add", 3, 70, 1, "red", 1);
    BusinessException exception = assertThrows(BusinessException.class, () -> alertSettingsService.create(rule2));
    assertEquals(AlertRuleCommon.AIR_VALUE_NOT_ALLOW, exception.getMessage());
  }

  @Test
  public void updateWeatherAlertRule_RuleIdNotFound() {
    AlertRule rule = new AlertRule(123L, ALR_WIND_LEVEL1, "ALR_WIND_LEVEL1 alert rule to update", 3, 80, 1, "red", 1);

    BusinessException exception = assertThrows(BusinessException.class, () -> alertSettingsService.update(rule));
    assertEquals(ALERT_RULE_NOT_FOUND, exception.getMessage());
  }

  @Test
  public void updateWeatherAlertRule_RuleCodeNotChange() {
    AlertRule rule = new AlertRule(1L, ALR_WIND_LEVEL2, "ALR_WIND_LEVEL1 alert rule to update", 3, 80, 1, "red", 1);

    BusinessException exception = assertThrows(BusinessException.class, () -> alertSettingsService.update(rule));
    assertEquals(AlertRuleCommon.RULE_CODE_CHANGE, exception.getMessage());
  }

  @Test
  public void updateWeatherAlertRule_updateOk() {
    AlertRule rule = new AlertRule(1L, ALR_WIND_LEVEL1, "ALR_WIND_LEVEL1 alert rule to update", 1, 90, 1, "red", 1);
    AlertRule result = alertSettingsService.update(rule);
    Assertions.assertNotNull(result);
  }

  @Test
  public void deleteWeatherAlertRule_RuleIdNotFound() {
    Long id = 123L;
    BusinessException exception = assertThrows(BusinessException.class, () -> alertSettingsService.delete(id));
    Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getErrorCode().getHttpStatus());
    Assertions.assertEquals(
        ALERT_RULE_NOT_FOUND, exception.getMessage());
  }

  @Test
  public void deleteWeatherAlertRule_deleteOk() {
    Long id = 1L;
    alertSettingsService.delete(id);
  }

  @Test
  public void updateWeatherAlertRule_existsAlertRuleEqualValue() {
    AlertRule rule = new AlertRule(1L, ALR_WIND_LEVEL1, "ALR_WIND_LEVEL1 alert rule to update", 3, 90, 1, "red", 1);

    WeatherAlertRule ruleExists = WeatherAlertRule.builder().id(2L).alertCode(ALR_ES_LVL5_WHITE_FROZEN)
        .content("ALR_ES_LVL5_WHITE_FROZEN alert rule exists").condition(1).value(90).status(true).level(1)
        .color("red").deploymentId(1).build();
    alertSettingsRepo.save(ruleExists);

    BusinessException exception = assertThrows(BusinessException.class, () -> alertSettingsService.update(rule));
    assertEquals(DUPLICATE_VALUE_NOT_ALLOW, exception.getMessage());
  }

  @Test
  public void createWeatherAlertRule_existsAlertRuleEqualValue() {
    AlertRule rule = new AlertRule(2L, ALR_ES_LVL5_WHITE_FROZEN, "ALR_ES_LVL5_WHITE_FROZEN alert rule to add", 3, 80, 1, "red", 1);
    BusinessException exception = assertThrows(BusinessException.class, () -> alertSettingsService.create(rule));
    assertEquals(DUPLICATE_VALUE_NOT_ALLOW, exception.getMessage());
  }
}
