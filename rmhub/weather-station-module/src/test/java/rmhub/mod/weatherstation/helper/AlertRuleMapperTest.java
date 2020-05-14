package rmhub.mod.weatherstation.helper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rmhub.mod.weatherstation.entity.WeatherAlertRule;
import rmhub.model.weatherStation.AlertRule;

@ExtendWith(SpringExtension.class)
class AlertRuleMapperTest {

  @InjectMocks
  private AlertRuleMapper alertRuleMapper;

  @Mock
  private ModelMapper mockModelMapper;

  @Test
  void convertToDto() {
    //given
    var entity = WeatherAlertRule.builder()
        .id(1L)
        .alertCode("132")
        .content("132")
        .condition(1)
        .value(100)
        .level(1)
        .status(true)
        .color("#d5d5d5")
        .deploymentId(1)
        .build();

    var dto = AlertRule.builder()
        .id(1L)
        .alertCode("132")
        .content("132")
        .condition(1)
        .value(100)
        .level(1)
        .color("#d5d5d5")
        .deploymentId(1)
        .build();

    //when
    Mockito.when(mockModelMapper.map(Mockito.any(WeatherAlertRule.class), Mockito.any())).thenReturn(dto);
    AlertRule actual = alertRuleMapper.convertToDto(entity);

    //then
    assertEquals(dto, actual);

    // verify
    verify(mockModelMapper, times(1)).map(entity, AlertRule.class);
  }

  @Test
  void convertToEntity() {
    //given
    var entity = WeatherAlertRule.builder()
        .id(1L)
        .alertCode("132")
        .content("132")
        .condition(1)
        .value(100)
        .level(1)
        .status(true)
        .color("#d5d5d5")
        .deploymentId(1)
        .build();

    var dto = AlertRule.builder()
        .id(1L)
        .alertCode("132")
        .content("132")
        .condition(1)
        .value(100)
        .level(1)
        .color("#d5d5d5")
        .deploymentId(1)
        .build();

    //when
    Mockito.when(mockModelMapper.map(Mockito.any(AlertRule.class), Mockito.any())).thenReturn(entity);
    WeatherAlertRule actual = alertRuleMapper.convertToEntity(dto);

    //then
    assertEquals(entity, actual);

    // verify
    verify(mockModelMapper, times(1)).map(dto, WeatherAlertRule.class);
  }
}
