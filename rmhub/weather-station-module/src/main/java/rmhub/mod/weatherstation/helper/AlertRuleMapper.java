package rmhub.mod.weatherstation.helper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rmhub.mod.weatherstation.entity.WeatherAlertRule;
import rmhub.model.weatherStation.AlertRule;

@Component
public class AlertRuleMapper {

  @Autowired
  private ModelMapper modelMapper;

  public AlertRule convertToDto(WeatherAlertRule alertRule) {
    return modelMapper.map(alertRule, AlertRule.class);
  }

  public WeatherAlertRule convertToEntity(AlertRule alertRuleDto) {
    return modelMapper.map(alertRuleDto, WeatherAlertRule.class);
  }
}

