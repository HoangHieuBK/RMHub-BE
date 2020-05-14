package rmhub.mod.weatherstation.service;

import static rmhub.mod.weatherstation.constant.AlertRuleCommon.AIR_OPERATION_NOT_ALLOW;
import static rmhub.mod.weatherstation.constant.AlertRuleCommon.AIR_VALUE_NOT_ALLOW;
import static rmhub.mod.weatherstation.constant.AlertRuleCommon.ALERT_OPERATION_NOT_ALLOW;
import static rmhub.mod.weatherstation.constant.AlertRuleCommon.ALERT_RULE_NOT_FOUND;
import static rmhub.mod.weatherstation.constant.AlertRuleCommon.DUPLICATE_VALUE_NOT_ALLOW;
import static rmhub.mod.weatherstation.constant.AlertRuleCommon.EXISTS_RULE_CODE;
import static rmhub.mod.weatherstation.constant.AlertRuleCommon.RULE_CODE_CHANGE;
import static rmhub.mod.weatherstation.constant.AlertRuleOperation.EQUALS;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import rmhub.common.common.ErrorCode;
import rmhub.common.exception.BusinessException;
import rmhub.mod.weatherstation.constant.AlertRuleCode;
import rmhub.mod.weatherstation.constant.AlertRuleLevel;
import rmhub.mod.weatherstation.constant.AlertRuleOperation;
import rmhub.mod.weatherstation.entity.WeatherAlertRule;
import rmhub.mod.weatherstation.helper.AlertRuleMapper;
import rmhub.mod.weatherstation.repository.AlertSettingsRepo;
import rmhub.model.weatherStation.AlertRule;

@Service
class AlertSettingsServiceImpl implements AlertSettingsService {

  @Autowired
  private AlertSettingsRepo alertSettingsRepo;

  @Autowired
  private AlertRuleMapper alertRuleMapper;

  @Override
  @CacheEvict(value = {"alertSettingFindCache", "alertSettingFindByIdCache", "alertSettingFindByCodeCache"}, allEntries = true)
  public AlertRule create(AlertRule alertData) {
    String result;
    WeatherAlertRule weatherAlertData = alertRuleMapper.convertToEntity(alertData);
    String alertCode = weatherAlertData.getAlertCode();

    //create alert
    if (weatherAlertData.getId() == null) {
      WeatherAlertRule oldAlertRule = alertSettingsRepo
          .find(alertCode, true, weatherAlertData.getDeploymentId());

      if (oldAlertRule != null) {
        throw new BusinessException(ErrorCode.BAD_REQUEST, EXISTS_RULE_CODE);
      }
    }
    checkExistsRuleByValue(weatherAlertData);
    result = checkAlertRuleData(weatherAlertData);
    if (result == null) {
      result = checkAlertRuleLogic(weatherAlertData);
      if (result == null) {
        // save the data
        WeatherAlertRule alertRule = alertSettingsRepo.save(weatherAlertData);
        // build the response
        return alertRuleMapper.convertToDto(alertRule);
      }
    }
    throw new BusinessException(ErrorCode.BAD_REQUEST, result);
  }

  @Deprecated
  public String checkAlertRuleData(WeatherAlertRule weatherAlertData) {
    String error = "";
    //if (weatherAlertData == null || weatherAlertData.getAlertCode() == null) {
    if (weatherAlertData == null) {
      error = " Alert Rule Object is invalid ";
      return error;
    }
    String alertCode = weatherAlertData.getAlertCode();
    if (alertCode == null || !AlertRuleCode.lsAlertCode.contains(alertCode)) {
      error = error + " Alert Rule Code is invalid ";
    }
    Integer alertOperation = weatherAlertData.getCondition();
    if (alertOperation == null || !AlertRuleOperation.lsOperation.contains(alertOperation)) {
      error = error + " Alert Rule Operation is invalid ";
    }
    Integer alertLevel = weatherAlertData.getLevel();
    if (alertLevel == null || !AlertRuleLevel.lsLevel.contains(alertLevel)) {
      error = error + " Alert Rule Level is invalid ";
    }
    Integer alertValue = weatherAlertData.getValue();
    if (alertValue == null) {
      error = error + " Alert Rule Value is invalid ";
    }
    if (!error.equals("")) {
      return error;
    } else {
      return null;
    }
  }

  @Deprecated
  protected String checkAlertRuleLogic(WeatherAlertRule weatherAlertData) {
    String error = "";
    List<WeatherAlertRule> lsWeatherAlertRule = alertSettingsRepo.find(true, weatherAlertData.getDeploymentId());
    String alertCode = weatherAlertData.getAlertCode();
    if (alertCode.equals(AlertRuleCode.ALR_WIND_LEVEL1.toString()) || alertCode
        .equals(AlertRuleCode.ALR_WIND_LEVEL2.toString())) {
      return checkAirRuleLogic(weatherAlertData, lsWeatherAlertRule);
    }
    Integer alertOperation = weatherAlertData.getCondition();
    if (alertOperation.intValue() != EQUALS) {
      error = ALERT_OPERATION_NOT_ALLOW;
    }
    if (!error.equals("")) {
      return error;
    } else {
      return null;
    }
  }

  @Deprecated
  protected String checkAirRuleLogic(WeatherAlertRule airRule,
      List<WeatherAlertRule> lsWeatherAlertRule) {
    String alertCode = airRule.getAlertCode();
    String converseRule = getAirRuleConverse(alertCode);

    WeatherAlertRule airRule2 = lsWeatherAlertRule.stream().filter(
        WeatherAlertRule -> converseRule.equals(WeatherAlertRule.getAlertCode())).findFirst().orElse(null);
    if (airRule2 != null) {
      return checkTwoAirRuleLogic(airRule, airRule2);
    }
    return null;
  }

  protected String getAirRuleConverse(String aiRule) {
    return aiRule.equals(AlertRuleCode.ALR_WIND_LEVEL1) ? AlertRuleCode.ALR_WIND_LEVEL2 : AlertRuleCode.ALR_WIND_LEVEL1;
  }

  @Deprecated
  protected String checkTwoAirRuleLogic(WeatherAlertRule airRule,
      WeatherAlertRule airRule2) {
    String error = "";
    if ((airRule.getAlertCode().equals(AlertRuleCode.ALR_WIND_LEVEL1)
        && airRule.getValue() >= airRule2.getValue())
        || (airRule.getAlertCode().equals(AlertRuleCode.ALR_WIND_LEVEL2)
        && airRule.getValue() <= airRule2.getValue())) {
      error =
          AIR_VALUE_NOT_ALLOW;
    }
    if ((airRule.getAlertCode().equals(AlertRuleCode.ALR_WIND_LEVEL1) && airRule.getCondition()
        .equals(AlertRuleOperation.GREATER) && airRule2.getCondition()
        .equals(AlertRuleOperation.SMALLER))
        || airRule2.getAlertCode().equals(AlertRuleCode.ALR_WIND_LEVEL1) && airRule2.getCondition()
        .equals(AlertRuleOperation.GREATER) && airRule.getCondition()
        .equals(AlertRuleOperation.SMALLER)) {
      error = error
          + AIR_OPERATION_NOT_ALLOW
      ;
    }
    if (!error.equals("")) {
      return error;
    } else {
      return null;
    }
  }

  @Override
  @CacheEvict(value = {"alertSettingFindCache", "alertSettingFindByIdCache", "alertSettingFindByCodeCache"}, allEntries = true)
  public AlertRule update(AlertRule alertData) {
    WeatherAlertRule weatherAlertData = alertRuleMapper.convertToEntity(alertData);
    WeatherAlertRule oldAlertRule = null;
    //update
    if (weatherAlertData.getId() != null) {
      oldAlertRule = alertSettingsRepo.findById(weatherAlertData.getId(), true);
      if (oldAlertRule == null) {
        throw new BusinessException(ErrorCode.NOT_FOUND, ALERT_RULE_NOT_FOUND);
      } else {
        if (!oldAlertRule.getAlertCode().equals(weatherAlertData.getAlertCode())) {
          throw new BusinessException(ErrorCode.BAD_REQUEST, RULE_CODE_CHANGE);
        }
      }
    }
    checkExistsRuleByValue(weatherAlertData);
    String result = checkAlertRuleData(weatherAlertData);
    if (result == null) {
      result = checkAlertRuleLogic(weatherAlertData);
      if (result == null) {
        // save the data
        oldAlertRule.setValue(weatherAlertData.getValue());
        oldAlertRule.setLevel(weatherAlertData.getLevel());
        oldAlertRule.setContent(weatherAlertData.getContent());
        oldAlertRule.setCondition(weatherAlertData.getCondition());
        oldAlertRule.setColor(weatherAlertData.getColor());
        oldAlertRule.setDeploymentId(weatherAlertData.getDeploymentId());
        WeatherAlertRule alertRule = alertSettingsRepo.save(oldAlertRule);
        // build the response
        return alertRuleMapper.convertToDto(alertRule);
      }
    }
    throw new BusinessException(ErrorCode.BAD_REQUEST, result);
  }

  protected void checkExistsRuleByValue(WeatherAlertRule rule) {
    List<WeatherAlertRule> existsRule = alertSettingsRepo
        .findExistsRuleByValue(rule.getId(), rule.getValue(), true, rule.getDeploymentId());
    if (existsRule.size() > 0) {
      throw new BusinessException(ErrorCode.BAD_REQUEST, DUPLICATE_VALUE_NOT_ALLOW);
    }
  }

  @Override
  @CacheEvict(value = {"alertSettingFindCache", "alertSettingFindByIdCache", "alertSettingFindByCodeCache"}, allEntries = true)
  public void delete(Long id) {
    WeatherAlertRule oldAlertRule = null;
    if (id != null) {
      oldAlertRule = alertSettingsRepo.findById(id, true);
      if (oldAlertRule == null) {
        throw new BusinessException(ErrorCode.NOT_FOUND, ALERT_RULE_NOT_FOUND);
      }
    }
    // save the data
    oldAlertRule.setStatus(false);
    alertSettingsRepo.save(oldAlertRule);
  }

  @Override
  @Cacheable(value = "alertSettingFindCache")
  public List<AlertRule> findAll(Integer deploymentId) {
    List<WeatherAlertRule> lsWeatherAlertRule = alertSettingsRepo.find(true, deploymentId == null ? 1 : deploymentId);
    return lsWeatherAlertRule.stream().map(p -> alertRuleMapper.convertToDto(p))
        .collect(Collectors.toList());
  }

  @Override
  @Cacheable(value = "alertSettingFindByIdCache")
  public WeatherAlertRule findById(Long id) {
    Optional<WeatherAlertRule> result = alertSettingsRepo.findById(id);
    return result.isEmpty() ? null : result.get();
  }

  @Override
  @Cacheable(value = "alertSettingFindByCodeCache")
  public List<WeatherAlertRule> findByAlertCode(String alertCode, Integer deploymentId) {
    return alertSettingsRepo.findByAlertCode(alertCode, deploymentId);
  }
}
