package rmhub.mod.weatherstation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static rmhub.mod.weatherstation.constant.AlertRuleCode.ALR_WIND_LEVEL1;
import static rmhub.mod.weatherstation.constant.AlertRuleCode.ALR_WIND_LEVEL2;
import static rmhub.mod.weatherstation.constant.AlertRuleCommon.API_PATH;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import rmhub.mod.weatherstation.service.AlertSettingsService;
import rmhub.model.weatherStation.AlertRule;

@WebMvcTest(AlertSettingController.class)
class AlertSettingControllerTest {

  @Autowired
  protected MockMvc mockMvc;

  @MockBean
  private AlertSettingsService alertSettingsService;

  @Test
  void createAlert_addNewOk() throws Exception {
    AlertRule rule = AlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL2)
        .content("ALR_WIND_LEVEL2 alert rule to add").condition(1)
        .value(100).level(1)
        .color("red").deploymentId(1).build();

    when(alertSettingsService.create((any()))).thenReturn(rule);

    mockMvc.perform(post(API_PATH).content(mapToJson(rule))
        .accept("application/json")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void createAlert_addNewExistsRuleCode() throws Exception {
    AlertRule rule = AlertRule.builder().id(null)
        .alertCode(ALR_WIND_LEVEL1)
        .content("ALR_WIND_LEVEL1 alert rule to add").condition(1)
        .value(100)
        .color("red").deploymentId(1).build();

    mockMvc.perform(post(API_PATH).content(mapToJson(rule))
        .accept("application/json")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  void editAlert_updateOK() throws Exception {
    var rule = AlertRule.builder().id(1L)
        .alertCode(ALR_WIND_LEVEL1)
        .content("ALR_WIND_LEVEL1 alert rule to update").condition(1).value(90)
        .level(1)
        .color("red").deploymentId(1).build();

    when(alertSettingsService.update((any()))).thenReturn(rule);

    mockMvc.perform(put(API_PATH + "/{id}", 1L)
        .content(mapToJson(rule))
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void deleteAlert_deleteOk() throws Exception {
    mockMvc.perform(delete(API_PATH + "/{id}", 1)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void getAlerts_Ok() throws Exception {
    List<AlertRule> lsRule = Collections.singletonList(
        AlertRule.builder().id(null)
            .alertCode(ALR_WIND_LEVEL2)
            .content("ALR_WIND_LEVEL2 alert rule to update").condition(1).value(90)
            .color("red").deploymentId(1).build());

    when(alertSettingsService.findAll(1)).thenReturn(lsRule);

    this.mockMvc.perform(get(API_PATH).accept("application/json")
        .param("deploymentId", "1")
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk());
  }

  @Test
  void getAlerts_Ok_NullDeploymentId() throws Exception {
    List<AlertRule> lsRule = Collections.singletonList(
        AlertRule.builder().id(null)
            .alertCode(ALR_WIND_LEVEL2)
            .content("ALR_WIND_LEVEL2 alert rule to update").condition(1).value(90)
            .color("red").deploymentId(1).build());

    when(alertSettingsService.findAll(1)).thenReturn(lsRule);

    this.mockMvc.perform(get(API_PATH).accept("application/json")
        .param("deploymentId", "")
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk());
  }

  private String mapToJson(Object obj) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(obj);
  }
}
