package rmhub.mod.weatherstation.service;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static rmhub.mod.weatherstation.constant.AlertRuleCode.ALR_ES_LVL16_SNOWDRIFTS;
import static rmhub.mod.weatherstation.constant.AlertRuleCode.ALR_ES_LVL4_STREAMING_RAIN;
import static rmhub.mod.weatherstation.constant.AlertRuleCode.ALR_WIND_LEVEL1;
import static rmhub.mod.weatherstation.constant.AlertRuleCode.ALR_WIND_LEVEL2;
import static rmhub.mod.weatherstation.constant.AlertRuleCommon.AIR_OPERATION_NOT_ALLOW;
import static rmhub.mod.weatherstation.constant.AlertRuleCommon.AIR_VALUE_NOT_ALLOW;
import static rmhub.mod.weatherstation.constant.AlertRuleCommon.ALERT_OPERATION_NOT_ALLOW;
import static rmhub.mod.weatherstation.constant.AlertRuleCommon.ALERT_RULE_NOT_FOUND;
import static rmhub.mod.weatherstation.constant.AlertRuleCommon.DUPLICATE_VALUE_NOT_ALLOW;
import static rmhub.mod.weatherstation.constant.AlertRuleCommon.EXISTS_RULE_CODE;
import static rmhub.mod.weatherstation.constant.AlertRuleCommon.RULE_CODE_CHANGE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rmhub.common.common.ErrorCode;
import rmhub.common.exception.BusinessException;
import rmhub.mod.weatherstation.entity.WeatherAlertRule;
import rmhub.mod.weatherstation.helper.AlertRuleMapper;
import rmhub.mod.weatherstation.repository.AlertSettingsRepo;
import rmhub.model.weatherStation.AlertRule;

@ExtendWith(SpringExtension.class)
class AlertSettingsServiceImplTest {

  @InjectMocks
  private AlertSettingsServiceImpl alertSettingsServiceImpl;

  @Mock
  private AlertRuleMapper alertRuleMapper;

  private ModelMapper modelMapper = new ModelMapper();

  @Mock
  AlertSettingsRepo alertSettingsRepo;

  @Test
  void testNotNull() {
    Assertions.assertNotNull(alertSettingsServiceImpl);
  }

  @Test
  void createWeatherAlertRule_addNewOK() {
    AlertRule rule = new AlertRule(null, ALR_WIND_LEVEL2, "ALR_WIND_LEVEL2 alert rule to create", 1, 100, 2, "red", 1);
    WeatherAlertRule rule2 = modelMapper.map(rule, WeatherAlertRule.class);

    when(alertRuleMapper.convertToEntity(any())).thenReturn(rule2);
    when(alertRuleMapper.convertToDto(any())).thenReturn(rule);
    when(alertSettingsRepo.save(rule2)).thenReturn(rule2);
    AlertRule result = alertSettingsServiceImpl.create(rule);
    assertEquals(result, modelMapper.map(rule2, AlertRule.class));
  }

  @Test
  void createWeatherAlertRule_ExistsRuleCode() {
    AlertRule rule = new AlertRule(null, ALR_WIND_LEVEL1, "ALR_WIND_LEVEL1 alert rule to create ", 3, 80, 1, "red", 1);
    WeatherAlertRule rule2 = modelMapper.map(rule, WeatherAlertRule.class);

    when(alertRuleMapper.convertToEntity(any())).thenReturn(rule2);
    when(alertRuleMapper.convertToDto(any())).thenReturn(rule);
    when(alertSettingsRepo.find(rule.getAlertCode(), true, 1)).thenReturn(rule2);

    BusinessException exception = assertThrows(BusinessException.class, () -> alertSettingsServiceImpl.create(rule));
    assertEquals(EXISTS_RULE_CODE, exception.getMessage());
  }

  @Test
  void createWeatherAlertRule_notAlowOperationRule2() {
    AlertRule rule = new AlertRule(null, ALR_WIND_LEVEL2, "ALR_WIND_LEVEL2 alert rule to create", 2, 120, 1, "red", 1);
    WeatherAlertRule rule2 = modelMapper.map(rule, WeatherAlertRule.class);
    var ruleExist = WeatherAlertRule.builder().id(null).alertCode(ALR_WIND_LEVEL1).content("ALR_WIND_LEVEL1 alert rule exists in list")
        .level(1).condition(1).value(80)
        .status(true).color("red").deploymentId(1).build();
    List<WeatherAlertRule> lsWeatherAlertRule = new ArrayList<>();
    lsWeatherAlertRule.add(ruleExist);

    when(alertRuleMapper.convertToEntity(any())).thenReturn(rule2);
    when(alertRuleMapper.convertToDto(any())).thenReturn(rule);
    when(alertSettingsRepo.find(rule.getAlertCode(), true, 1)).thenReturn(null);
    when(alertSettingsRepo.find(true, 1)).thenReturn(lsWeatherAlertRule);
    BusinessException exception = assertThrows(BusinessException.class, () -> alertSettingsServiceImpl.create(rule));
    assertEquals(AIR_OPERATION_NOT_ALLOW, exception.getMessage());
  }

  @Test
  void createWeatherAlertRule_notAlowSmallerRule2() {
    AlertRule rule = new AlertRule(null, ALR_WIND_LEVEL2, "ALR_WIND_LEVEL2 alert rule to create", 3, 80, 1, "red", 1);
    WeatherAlertRule rule2 = modelMapper.map(rule, WeatherAlertRule.class);
    var ruleExist = WeatherAlertRule.builder().id(null).alertCode(ALR_WIND_LEVEL1).content("ALR_WIND_LEVEL1 alert rule exists in list")
        .level(1).condition(1).value(100)
        .status(true).color("red").deploymentId(1).build();
    List<WeatherAlertRule> lsWeatherAlertRule = new ArrayList<>();
    lsWeatherAlertRule.add(ruleExist);

    when(alertRuleMapper.convertToEntity(any())).thenReturn(rule2);
    when(alertRuleMapper.convertToDto(any())).thenReturn(rule);
    when(alertSettingsRepo.find(rule.getAlertCode(), true, 1)).thenReturn(null);
    when(alertSettingsRepo.find(true, 1)).thenReturn(lsWeatherAlertRule);
    BusinessException exception = assertThrows(BusinessException.class, () -> alertSettingsServiceImpl.create(rule));
    assertEquals(AIR_VALUE_NOT_ALLOW, exception.getMessage());
  }

  @Test
  void updateWeatherAlertRule_notAlowSmallerRule2() {
    AlertRule rule = new AlertRule(null, ALR_WIND_LEVEL2, "ALR_WIND_LEVEL2 alert rule to update", 3, 80, 1, "red", 1);
    WeatherAlertRule rule2 = modelMapper.map(rule, WeatherAlertRule.class);
    var ruleExist = WeatherAlertRule.builder().id(null).alertCode(ALR_WIND_LEVEL1).content("ALR_WIND_LEVEL1 alert rule exists in list")
        .level(1).condition(1).value(100)
        .status(true).color("red").deploymentId(1).build();
    List<WeatherAlertRule> lsWeatherAlertRule = new ArrayList<>();
    lsWeatherAlertRule.add(ruleExist);

    when(alertRuleMapper.convertToEntity(any())).thenReturn(rule2);
    when(alertRuleMapper.convertToDto(any())).thenReturn(rule);
    when(alertSettingsRepo.find(rule.getAlertCode(), true, 1)).thenReturn(null);
    when(alertSettingsRepo.find(true, 1)).thenReturn(lsWeatherAlertRule);
    BusinessException exception = assertThrows(BusinessException.class, () -> alertSettingsServiceImpl.update(rule));
    assertEquals(AIR_VALUE_NOT_ALLOW, exception.getMessage());
  }

  @Test
  void updateWeatherAlertRule_RuleIdNotFound() {
    AlertRule rule = new AlertRule(123L, ALR_WIND_LEVEL1, "ALR_WIND_LEVEL1 alert rule to update", 3, 80, 1, "red", 1);
    WeatherAlertRule rule2 = modelMapper.map(rule, WeatherAlertRule.class);

    when(alertRuleMapper.convertToEntity(any())).thenReturn(rule2);
    when(alertRuleMapper.convertToDto(any())).thenReturn(rule);
    when(alertSettingsRepo.findById(any(), any())).thenReturn(null);

    BusinessException exception = assertThrows(BusinessException.class, () -> alertSettingsServiceImpl.update(rule));
    assertEquals(ALERT_RULE_NOT_FOUND, exception.getMessage());
  }

  @Test
  void updateWeatherAlertRule_RuleCodeNotChange() {
    AlertRule rule = new AlertRule(1L, ALR_WIND_LEVEL2, "ALR_WIND_LEVEL1 alert rule to update", 3, 80, 1, "red", 1);
    WeatherAlertRule rule2 = modelMapper.map(rule, WeatherAlertRule.class);
    WeatherAlertRule ruleExist = modelMapper.map(rule, WeatherAlertRule.class);
    ruleExist.setAlertCode(ALR_WIND_LEVEL1);
    when(alertRuleMapper.convertToEntity(any())).thenReturn(rule2);
    when(alertRuleMapper.convertToDto(any())).thenReturn(rule);
    when(alertSettingsRepo.findById(any(), any())).thenReturn(ruleExist);

    BusinessException exception = assertThrows(BusinessException.class, () -> alertSettingsServiceImpl.update(rule));
    assertEquals(RULE_CODE_CHANGE, exception.getMessage());
  }

  @Test
  void updateWeatherAlertRule_updateOk() {
    AlertRule rule = new AlertRule(1L, ALR_WIND_LEVEL2, "ALR_WIND_LEVEL1 alert rule to update", 3, 80, 1, "red", 1);
    WeatherAlertRule rule2 = modelMapper.map(rule, WeatherAlertRule.class);
    WeatherAlertRule ruleExist = modelMapper.map(rule, WeatherAlertRule.class);

    when(alertRuleMapper.convertToEntity(any())).thenReturn(rule2);
    when(alertRuleMapper.convertToDto(any())).thenReturn(rule);
    when(alertSettingsRepo.findById(any(), any())).thenReturn(ruleExist);

    AlertRule result = alertSettingsServiceImpl.update(rule);
    Assertions.assertNotNull(result);
  }

  @Test
  void deleteWeatherAlertRule_RuleIdNotFound() {
    when(alertSettingsRepo.findById(any(), any())).thenReturn(null);

    Long id = 123L;
    BusinessException exception = assertThrows(BusinessException.class, () -> alertSettingsServiceImpl.delete(id));
    assertEquals(ALERT_RULE_NOT_FOUND, exception.getMessage());
  }

  @Test
  void deleteWeatherAlertRule_deleteOk() {
    AlertRule rule = new AlertRule(1L, ALR_WIND_LEVEL2, "ALR_WIND_LEVEL2 alert rule to delete", 3, 80, 1, "red", 1);
    WeatherAlertRule ruleExist = modelMapper.map(rule, WeatherAlertRule.class);
    Long id = 1L;
    when(alertRuleMapper.convertToDto(any())).thenReturn(rule);
    when(alertSettingsRepo.findById(any(), any())).thenReturn(ruleExist);
    alertSettingsServiceImpl.delete(id);
    // It's ok without exception
  }

  @Test
  void updateWeatherAlertRule_existsAlertRuleEqualValue() {
    AlertRule rule = new AlertRule(1L, ALR_WIND_LEVEL2, "ALR_WIND_LEVEL2 alert rule to update", 3, 80, 1, "red", 1);
    WeatherAlertRule rule2 = modelMapper.map(rule, WeatherAlertRule.class);
    WeatherAlertRule ruleExist = modelMapper.map(rule, WeatherAlertRule.class);

    when(alertRuleMapper.convertToEntity(any())).thenReturn(rule2);
    when(alertRuleMapper.convertToDto(any())).thenReturn(rule);
    when(alertSettingsRepo.findById(any(), any())).thenReturn(ruleExist);

    List<WeatherAlertRule> lsRuleExistWithValue = Collections
        .singletonList(modelMapper.map(rule, WeatherAlertRule.class));

    WeatherAlertRule ruleExistWithValue = modelMapper.map(rule, WeatherAlertRule.class);
    ruleExistWithValue.setAlertCode(ALR_ES_LVL4_STREAMING_RAIN);

    when(alertSettingsRepo.findExistsRuleByValue(any(), any(), any(), any())).thenReturn(lsRuleExistWithValue);

    BusinessException exception = assertThrows(BusinessException.class, () -> alertSettingsServiceImpl.update(rule));
    assertEquals(DUPLICATE_VALUE_NOT_ALLOW, exception.getMessage());
  }

  @Test
  void createWeatherAlertRule_existsAlertRuleEqualValue() {
    AlertRule rule = new AlertRule(null, ALR_WIND_LEVEL1, "ALR_WIND_LEVEL1 alert rule to update", 3, 80, 1, "red", 1);
    WeatherAlertRule rule2 = modelMapper.map(rule, WeatherAlertRule.class);

    when(alertRuleMapper.convertToEntity(any())).thenReturn(rule2);
    when(alertRuleMapper.convertToDto(any())).thenReturn(rule);
    when(alertSettingsRepo.find(rule.getAlertCode(), true, 1)).thenReturn(null);

    List<WeatherAlertRule> lsRuleExistWithValue = Collections
        .singletonList(modelMapper.map(rule, WeatherAlertRule.class));

    WeatherAlertRule ruleExistWithValue = modelMapper.map(rule, WeatherAlertRule.class);
    ruleExistWithValue.setAlertCode(ALR_ES_LVL4_STREAMING_RAIN);

    when(alertSettingsRepo.findExistsRuleByValue(any(), any(), any(), any())).thenReturn(lsRuleExistWithValue);

    BusinessException exception = assertThrows(BusinessException.class, () -> alertSettingsServiceImpl.create(rule));
    assertEquals(DUPLICATE_VALUE_NOT_ALLOW, exception.getMessage());
  }


  //===========================
  @Test
  void checkAlertRuleData_InvalidRuleObject() {

    WeatherAlertRule weatherAlertRule = null;
    String result = alertSettingsServiceImpl.checkAlertRuleData(weatherAlertRule);

    Assertions.assertEquals(" Alert Rule Object is invalid ", result);
  }

  @Test
  void checkAlertRuleData_InvalidRuleCode() {

    var weatherAlertRule = WeatherAlertRule.builder().id(1L)
        .alertCode("ALR_WIND_LEVEL12").content("check alert rule data with invalid code").level(1)
        .condition(1).value(80).status(true).color("red")
        .deploymentId(1).build();
    String result = alertSettingsServiceImpl.checkAlertRuleData(weatherAlertRule);

    Assertions.assertEquals(" Alert Rule Code is invalid ", result);
  }

  @Test
  void checkAlertRuleData_InvalidRuleValue() {

    var weatherAlertRule = WeatherAlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL1).content("check alert rule data with the invalid rule value").level(1)
        .condition(1).value(null).status(true).color("red")
        .deploymentId(1).build();
    String result = alertSettingsServiceImpl.checkAlertRuleData(weatherAlertRule);

    Assertions.assertEquals(" Alert Rule Value is invalid ", result);
  }

  @Test
  void checkAlertRuleData_InvalidRuleCondition() {

    var weatherAlertRule = WeatherAlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL1).content("check alert rule data with the invalid rule condition").level(1)
        .condition(4).value(80).status(true).color("red")
        .deploymentId(1).build();
    String result = alertSettingsServiceImpl.checkAlertRuleData(weatherAlertRule);

    Assertions.assertEquals(" Alert Rule Operation is invalid ", result);
  }

  @Test
  public void checkAlertRuleData_InvalidRuleLevel() {

    var weatherAlertRule = WeatherAlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL1).content("check alert rule data with the invalid rule level").level(6)
        .condition(1).value(80).status(true).color("red")
        .deploymentId(1).build();
    String result = alertSettingsServiceImpl.checkAlertRuleData(weatherAlertRule);
    Assertions.assertEquals(" Alert Rule Level is invalid ", result);
  }

  @Test
  void twoAirRuleLogic_Rule1ValueGreaterRule2Value() {

    var rule1 = WeatherAlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL1).content("check alert rule data with the value of rule 1 > the value of rule 2").level(1)
        .condition(1).value(100).status(true).color("red")
        .deploymentId(1).build();

    var rule2 = WeatherAlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL2).content("check alert rule data with the value of rule 1 > the value of rule 2").level(3)
        .condition(3).value(90).status(true).color("red")
        .deploymentId(1).build();

    String result = alertSettingsServiceImpl.checkTwoAirRuleLogic(rule1, rule2);

    Assertions.assertEquals(AIR_VALUE_NOT_ALLOW, result);
  }

  @Test
  void twoAirRuleLogic_Rule1ValueLessRule2Value() {

    var rule1 = WeatherAlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL2).content("check alert rule data with the value of rule 1 > the value of rule 2").level(1)
        .condition(1).value(90).status(true).color("red")
        .deploymentId(1).build();

    var rule2 = WeatherAlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL2).content("check alert rule data with the value of rule 1 > the value of rule 2").level(3)
        .condition(3).value(90).status(true).color("red")
        .deploymentId(1).build();

    String result = alertSettingsServiceImpl.checkTwoAirRuleLogic(rule1, rule2);

    Assertions.assertEquals(AIR_VALUE_NOT_ALLOW, result);
  }


  @Test
  void checkTwoAirRuleLogic_airOperationAlow() {
    var rule1 = WeatherAlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL1).content("ALR_WIND_LEVEL1 rule condition is <=").level(1)
        .condition(2).value(80).status(true).color("red")
        .deploymentId(1).build();

    var rule2 = WeatherAlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL2).content("ALR_WIND_LEVEL2 rule condition is invalid with <=").level(2)
        .condition(2).value(100).status(true).color("red")
        .deploymentId(1).build();

    String result = alertSettingsServiceImpl.checkTwoAirRuleLogic(rule2, rule1);

    Assertions.assertNull(result);
  }

  @Test
  void checkTwoAirRuleLogic_airOperationAlow2() {
    var rule1 = WeatherAlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL1).content("ALR_WIND_LEVEL1 rule condition is <=").level(1)
        .condition(2).value(80).status(true).color("red")
        .deploymentId(1).build();

    var rule2 = WeatherAlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL2).content("ALR_WIND_LEVEL2 rule condition is invalid with <=").level(2)
        .condition(2).value(100).status(true).color("red")
        .deploymentId(1).build();

    String result = alertSettingsServiceImpl.checkTwoAirRuleLogic(rule1, rule2);

    Assertions.assertNull(result);
  }

  @Test
  void twoAirRuleLogic_notAlowSmallerOperationRule2() {

    var rule1 = WeatherAlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL1).content("ALR_WIND_LEVEL1 rule condition is >=").level(1)
        .condition(1).value(80).status(true).color("red")
        .deploymentId(1).build();

    var rule2 = WeatherAlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL2).content("ALR_WIND_LEVEL2 rule condition is invalid with <=").level(2)
        .condition(2).value(100).status(true).color("red")
        .deploymentId(1).build();

    String result = alertSettingsServiceImpl.checkTwoAirRuleLogic(rule1, rule2);

    Assertions.assertEquals(AIR_OPERATION_NOT_ALLOW, result);
  }

  @Test
  void testGetAirRuleConverse() {

    String airCode2 = alertSettingsServiceImpl.getAirRuleConverse(ALR_WIND_LEVEL1);
    Assertions.assertEquals(airCode2, ALR_WIND_LEVEL2);

    String airCode1 = alertSettingsServiceImpl.getAirRuleConverse(ALR_WIND_LEVEL2);
    Assertions.assertEquals(airCode1, ALR_WIND_LEVEL1);
  }

  @Test
  void checkAirRuleLogic_addNewAirRuleWithNonExistOther() {

    var rule1 = WeatherAlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL1).content("check alert rule logic with add new Air rule with none exist other").level(1)
        .condition(1).value(80).status(true).color("red")
        .deploymentId(1).build();
    List<WeatherAlertRule> lsWeatherAlertRule = new ArrayList<>();

    String result = alertSettingsServiceImpl.checkAirRuleLogic(rule1, lsWeatherAlertRule);

    Assertions.assertNull(result);
  }

  @Test
  void checkAirRuleLogic_addNewAirRuleWithExistOther() {

    var rule1 = WeatherAlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL1).content("check alert rule logic with add new Air rule with exist other").level(1)
        .condition(1).value(80).status(true).color("red")
        .deploymentId(1).build();
    List<WeatherAlertRule> lsWeatherAlertRule = new ArrayList<>();

    var rule2 = WeatherAlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL1).content(" ALR_WIND_LEVEL1 exists in alert rule list").level(1)
        .condition(1).value(80).status(true).color("red")
        .deploymentId(1).build();

    lsWeatherAlertRule.add(rule2);
    String result = alertSettingsServiceImpl.checkAirRuleLogic(rule1, lsWeatherAlertRule);
    Assertions.assertNull(result);

    lsWeatherAlertRule.clear();
    lsWeatherAlertRule.add(rule1);
    String result2 = alertSettingsServiceImpl.checkAirRuleLogic(rule2, lsWeatherAlertRule);
    Assertions.assertNull(result);
  }

  @Test
  void checkAirRuleLogic_addNewAirRuleWithExistLv2() {

    var rule1 = WeatherAlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL2).content("check alert rule logic with add new Air rule with exist other").level(2)
        .condition(1).value(80).status(true).color("red")
        .deploymentId(1).build();
    List<WeatherAlertRule> lsWeatherAlertRule = new ArrayList<>();

    var rule2 = WeatherAlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL2).content(" ALR_WIND_LEVEL2 exists in alert rule list").level(2)
        .condition(1).value(80).status(true).color("red")
        .deploymentId(1).build();

    lsWeatherAlertRule.add(rule2);
    String result = alertSettingsServiceImpl.checkAirRuleLogic(rule1, lsWeatherAlertRule);
    Assertions.assertNull(result);

    lsWeatherAlertRule.clear();
    lsWeatherAlertRule.add(rule1);
    String result2 = alertSettingsServiceImpl.checkAirRuleLogic(rule2, lsWeatherAlertRule);
    Assertions.assertNull(result);
  }

  @Test
  void checkAirRuleLogic_addNewAirRuleWithExistOtherInvalid() {

    var rule1 = WeatherAlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL1).content("ALR_WIND_LEVEL1 alert rule to add").level(1)
        .condition(1).value(80).status(true).color("red")
        .deploymentId(1).build();
    List<WeatherAlertRule> lsWeatherAlertRule = new ArrayList<>();

    var rule2 = WeatherAlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL2).content("ALR_WIND_LEVEL2 alert rule in list").level(2)
        .condition(2).value(100).status(true).color("red")
        .deploymentId(1).build();
    lsWeatherAlertRule.add(rule2);

    String result = alertSettingsServiceImpl.checkAirRuleLogic(rule1, lsWeatherAlertRule);

    Assertions.assertEquals(AIR_OPERATION_NOT_ALLOW, result);

  }

  @Test
  void checkAirRuleLogic_addNewAirRuleWithExistOtherInvalid2() {

    var rule1 = WeatherAlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL1).content("ALR_WIND_LEVEL1 alert rule in list").level(1)
        .condition(1).value(80).status(true).color("red")
        .deploymentId(1).build();
    List<WeatherAlertRule> lsWeatherAlertRule = new ArrayList<>();

    var rule2 = WeatherAlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL2).content("ALR_WIND_LEVEL2 alert rule to add").level(2)
        .condition(2).value(100).status(true).color("red")
        .deploymentId(1).build();
    lsWeatherAlertRule.add(rule1);

    String result = alertSettingsServiceImpl.checkAirRuleLogic(rule2, lsWeatherAlertRule);

    Assertions.assertEquals(AIR_OPERATION_NOT_ALLOW, result);

  }

  @Test
  void checkAlertRuleLogic_NotAirRuleInvalidOperation() {

    var rule = WeatherAlertRule.builder().id(null)
        .alertCode("ALR_ES_LVL1_WET_TRANSITIONAL").content("check alert rule logics").level(1)
        .condition(1).value(80).status(true).color("red")
        .deploymentId(1).build();

    String result = alertSettingsServiceImpl.checkAlertRuleLogic(rule);

    Assertions.assertEquals(ALERT_OPERATION_NOT_ALLOW, result);
  }

  @Test
  void checkAlertRuleLogic_CheckAirRuleLogic() {
    var rule = WeatherAlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL1).content("check alert rule logics").level(1)
        .condition(1).value(80).status(true).color("red")
        .deploymentId(1).build();
    String result = alertSettingsServiceImpl.checkAlertRuleLogic(rule);
    var rule1 = WeatherAlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL1).content("check alert rule logic with add new Air rule with none exist other").level(1)
        .condition(1).value(80).status(true).color("red")
        .deploymentId(1).build();
    List<WeatherAlertRule> lsWeatherAlertRule = new ArrayList<>();
    String result1 = alertSettingsServiceImpl.checkAirRuleLogic(rule1, lsWeatherAlertRule);
    Assertions.assertNull(result1);
  }


  @Test
  void createWeatherAlertRule_InvalidRuleCode() {

    var alertRule = AlertRule.builder().id(1L)
        .alertCode("ALR_WIND_LEVEL12").content("check alert rule data with invalid code").level(1)
        .condition(1).value(80).color("red")
        .deploymentId(1).build();
    WeatherAlertRule rule2 = modelMapper.map(alertRule, WeatherAlertRule.class);

    when(alertRuleMapper.convertToEntity(any())).thenReturn(rule2);
    when(alertRuleMapper.convertToDto(any())).thenReturn(alertRule);
    when(alertSettingsRepo.find(alertRule.getAlertCode(), true, 1)).thenReturn(rule2);

    BusinessException exception = assertThrows(BusinessException.class, () -> alertSettingsServiceImpl.create(alertRule));
    assertEquals(" Alert Rule Code is invalid ", exception.getMessage());
  }

  @Test
  void createWeatherAlertRule_NotWind_addOk() {

    var rule = AlertRule.builder()
        .id(null).alertCode(ALR_ES_LVL16_SNOWDRIFTS)
        .content("ALR_WIND_LEVEL2 alert rule to add new")
        .level(2).value(120)
        .condition(3)
        .color("red")
        .deploymentId(1)
        .build();
    WeatherAlertRule rule2 = modelMapper.map(rule, WeatherAlertRule.class);

    when(alertRuleMapper.convertToEntity(any())).thenReturn(rule2);
    when(alertRuleMapper.convertToDto(any())).thenReturn(rule);
    when(alertSettingsRepo.save(rule2)).thenReturn(rule2);
    AlertRule result = alertSettingsServiceImpl.create(rule);
    assertEquals(result, modelMapper.map(rule2, AlertRule.class));
  }


  @Test
  void updateWeatherAlertRule_BusinessException() {

    AlertRule rule = new AlertRule();
    WeatherAlertRule rule2 = modelMapper.map(rule, WeatherAlertRule.class);

    when(alertRuleMapper.convertToEntity(any())).thenReturn(rule2);
    when(alertRuleMapper.convertToDto(any())).thenReturn(rule);
    when(alertSettingsRepo.findById(any(), any())).thenReturn(null);

    BusinessException exception = assertThrows(BusinessException.class, () -> alertSettingsServiceImpl.update(rule));
    assertEquals(ErrorCode.BAD_REQUEST, exception.getErrorCode());
    Assertions.assertEquals(
        " Alert Rule Code is invalid  Alert Rule Operation is invalid  Alert Rule Level is invalid  Alert Rule Value is invalid ",
        exception.getMessage());
  }

//  @Test
//  void updateWeatherAlertRule_AlertRuleLogicNotNull() {
//    var ruleExists = WeatherAlertRule.builder()
//        .id(null).alertCode(ALR_ES_LVL3_WET)
//        .content("ALR_ES_LVL3_WET exists alert rule ")
//        .level(1)
//        .value(90)
//        .condition(3)
//        .color("red")
//        .deploymentId(1)
//        .build();
//    alertSettingsRepo.save(ruleExists);
//
//    var ruleUpdate = AlertRule.builder()
//        .id(null).alertCode(ALR_ES_LVL3_WET)
//        .content("ALR_ES_LVL3_WET alert rule to update")
//        .level(1)
//        .value(90)
//        .condition(1)
//        .color("red")
//        .deploymentId(1)
//        .build();
//    BusinessException exExpect  = Assertions.assertThrows(BusinessException.class ,() -> alertSettingsServiceImpl.update(any()));
//   BusinessException exExpect = Assertions.assertThrows(BusinessException.class, () -> alertSettingsServiceImpl.update(ruleUpdate));
//    Assertions.assertNotNull(exExpect);
//  }

  @Test
  void deleteWeatherAlertRule_IdNull() {
    // Id is checked  by control layer but write test for coverage
    Long id = null;
    NullPointerException exExpect = assertThrows(NullPointerException.class, () -> alertSettingsServiceImpl.delete(id));
    assertNotNull(exExpect);
  }

  @Test
  void findAll_findOK() {

    var alertRule = AlertRule.builder().id(1L)
        .alertCode("ALR_WIND_LEVEL2").content("find exists value").level(1)
        .condition(1).value(80).color("red")
        .deploymentId(1).build();

    var weatherAlertRule = WeatherAlertRule.builder().id(1L)
        .alertCode(ALR_WIND_LEVEL2).content("find exists value").level(1)
        .condition(1).value(81).status(true).color("red")
        .deploymentId(1).build();

    var weatherAlertRule1 = WeatherAlertRule.builder().id(2L)
        .alertCode(ALR_WIND_LEVEL2).content("find exists value").level(1)
        .condition(1).value(82).status(true).color("red")
        .deploymentId(1).build();

    var weatherAlertRule2 = WeatherAlertRule.builder().id(3L)
        .alertCode(ALR_WIND_LEVEL2).content("find exists value").level(1)
        .condition(1).value(83).status(true).color("red")
        .deploymentId(1).build();

    List<WeatherAlertRule> list = new ArrayList<>();

    list.add(weatherAlertRule);
    list.add(weatherAlertRule1);
    list.add(weatherAlertRule2);

    when(alertSettingsRepo.find(true, 1)).thenReturn(list);
    when(alertRuleMapper.convertToDto(any())).thenReturn(alertRule);
    List<AlertRule> actual = alertSettingsServiceImpl.findAll(1);
    Assertions.assertEquals(3, actual.size());
    assertEquals(weatherAlertRule, list.get(0));
    assertEquals(weatherAlertRule1, list.get(1));
    assertEquals(weatherAlertRule2, list.get(2));
  }

  @Test
  void findAll_findWithDeloymentNull() {

    var alertRule = AlertRule.builder().id(1L)
        .alertCode("ALR_WIND_LEVEL2").content("find exists value").level(1)
        .condition(1).value(80).color("red")
        .deploymentId(1).build();

    var weatherAlertRule = WeatherAlertRule.builder().id(1L)
        .alertCode(ALR_WIND_LEVEL2).content("find exists value").level(1)
        .condition(1).value(81).status(true).color("red")
        .deploymentId(1).build();

    var weatherAlertRule1 = WeatherAlertRule.builder().id(2L)
        .alertCode(ALR_WIND_LEVEL2).content("find exists value").level(1)
        .condition(1).value(82).status(true).color("red")
        .deploymentId(1).build();

    var weatherAlertRule2 = WeatherAlertRule.builder().id(3L)
        .alertCode(ALR_WIND_LEVEL2).content("find exists value").level(1)
        .condition(1).value(83).status(true).color("red")
        .deploymentId(1).build();

    List<WeatherAlertRule> list = new ArrayList<>();

    list.add(weatherAlertRule);
    list.add(weatherAlertRule1);
    list.add(weatherAlertRule2);

    when(alertSettingsRepo.find(Mockito.anyBoolean(), Mockito.anyInt())).thenReturn(list);
    when(alertRuleMapper.convertToDto(any())).thenReturn(alertRule);
    List<AlertRule> actual = alertSettingsServiceImpl.findAll(null);

    Mockito.verify(alertSettingsRepo, Mockito.times(1)).find(Mockito.anyBoolean(), Mockito.anyInt());

    Assertions.assertEquals(3, actual.size());
    assertEquals(weatherAlertRule, list.get(0));
    assertEquals(weatherAlertRule1, list.get(1));
    assertEquals(weatherAlertRule2, list.get(2));
  }

  @Test
  void findById_findOK() {
    var weatherAlertRule = WeatherAlertRule.builder().id(1L)
        .alertCode(ALR_WIND_LEVEL2).content("find exists value").level(1)
        .condition(1).value(80).status(true).color("red")
        .deploymentId(1).build();
    when(alertSettingsRepo.findById(Mockito.anyLong())).thenReturn(Optional.of(weatherAlertRule));
    WeatherAlertRule actual = alertSettingsServiceImpl.findById(1L);
    Assertions.assertEquals(weatherAlertRule, actual);
  }

  @Test
  void findById_findEmpty() {

    Long id = 2L;
    WeatherAlertRule result = alertSettingsServiceImpl.findById(id);
    Assertions.assertNull(result);
  }

  @Test
  void findExistsValue_findWhenUpdate() {

    var rule = WeatherAlertRule.builder().id(1L)
        .alertCode(ALR_WIND_LEVEL1).content("find exists value").level(1)
        .condition(1).value(100).status(true).color("red")
        .deploymentId(1).build();
    // notthing is Ok
    alertSettingsServiceImpl.checkExistsRuleByValue(rule);
  }

  @Test
  void findExistsValue_findWhenCreate() {

    var weatherAlertRule = WeatherAlertRule.builder().id(1L)
        .alertCode(ALR_WIND_LEVEL2).content("find exists value").level(1)
        .condition(1).value(80).status(true).color("red")
        .deploymentId(1).build();
    // notthing is Ok
    alertSettingsServiceImpl.checkExistsRuleByValue(weatherAlertRule);
  }

  @Test
  void findByAlertCode_OK() {

    var weatherAlertRule = WeatherAlertRule.builder()
        .id(1L)
        .alertCode(ALR_WIND_LEVEL1)
        .content("exists  ALR_WIND_LEVEL1 rule")
        .condition(1)
        .value(100)
        .level(1)
        .status(true)
        .color("red")
        .deploymentId(1)
        .build();

    var weatherAlertRule1 = WeatherAlertRule.builder()
        .id(2L)
        .alertCode(ALR_WIND_LEVEL1)
        .content("exists  ALR_WIND_LEVEL1 rule")
        .condition(1)
        .value(100)
        .level(1)
        .status(true)
        .color("red")
        .deploymentId(1)
        .build();

    var weatherAlertRule2 = WeatherAlertRule.builder()
        .id(3L)
        .alertCode(ALR_WIND_LEVEL1)
        .content("exists  ALR_WIND_LEVEL1 rule")
        .condition(1)
        .value(100)
        .level(1)
        .status(true)
        .color("red")
        .deploymentId(1)
        .build();

    List<WeatherAlertRule> list = new ArrayList<>();

    list.add(weatherAlertRule);
    list.add(weatherAlertRule1);
    list.add(weatherAlertRule2);

    when(alertSettingsRepo.findByAlertCode(Mockito.anyString(), Mockito.anyInt())).thenReturn(list);
    List<WeatherAlertRule> actual = alertSettingsServiceImpl.findByAlertCode(ALR_WIND_LEVEL1, 1);
    Assertions.assertEquals(list, actual);
    assertEquals(weatherAlertRule, list.get(0));
    assertEquals(weatherAlertRule1, list.get(1));
    assertEquals(weatherAlertRule2, list.get(2));
  }

  @Test
  void checkExistsRuleByValue_NotNull() {
    var weatherAlertRule = WeatherAlertRule.builder()
        .id(2L)
        .alertCode(ALR_WIND_LEVEL2)
        .content("ALR_WIND_LEVEL2 rule to add")
        .condition(1)
        .value(100)
        .level(1)
        .status(true)
        .color("red")
        .deploymentId(1)
        .build();

    List<WeatherAlertRule> list = new ArrayList<>();

    list.add(weatherAlertRule);

    when(alertSettingsRepo.findExistsRuleByValue(Mockito.anyLong(), Mockito.anyInt(), Mockito.anyBoolean(), Mockito.anyInt()))
        .thenReturn(list);
    BusinessException exExpect = Assertions
        .assertThrows(BusinessException.class, () -> alertSettingsServiceImpl.checkExistsRuleByValue(weatherAlertRule));
    Assertions.assertNotNull(exExpect);
  }

}
