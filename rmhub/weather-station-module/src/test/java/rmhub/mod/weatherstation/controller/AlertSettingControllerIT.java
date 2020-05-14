package rmhub.mod.weatherstation.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static rmhub.mod.weatherstation.constant.AlertRuleCode.ALR_WIND_LEVEL1;
import static rmhub.mod.weatherstation.constant.AlertRuleCode.ALR_WIND_LEVEL2;
import static rmhub.mod.weatherstation.constant.AlertRuleCommon.ALERT_RULE_NOT_FOUND;
import static rmhub.mod.weatherstation.constant.AlertRuleCommon.API_PATH;
import static rmhub.mod.weatherstation.constant.AlertRuleCommon.EXISTS_RULE_CODE;
import static rmhub.mod.weatherstation.constant.AlertRuleCommon.RULE_CODE_CHANGE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import rmhub.mod.weatherstation.entity.EntityPackageMarker;
import rmhub.mod.weatherstation.entity.WeatherAlertRule;
import rmhub.mod.weatherstation.helper.HelperPackageMarker;
import rmhub.mod.weatherstation.repository.AlertSettingsRepo;
import rmhub.mod.weatherstation.repository.RepoPackageMarker;
import rmhub.mod.weatherstation.service.AlertSettingsService;
import rmhub.mod.weatherstation.service.ServicePackageMarker;
import rmhub.model.weatherStation.AlertRule;

@Disabled("In this phase we need UT only")
@DataJpaTest
@EntityScan(basePackageClasses = EntityPackageMarker.class)
@EnableJpaRepositories(basePackageClasses = RepoPackageMarker.class)
@ComponentScan(basePackageClasses = {ServicePackageMarker.class, HelperPackageMarker.class, ControllerPackageMarker.class})
public class AlertSettingControllerIT {

  @Configuration
  static class TestConfig {

    @Bean
    public ModelMapper modelMapper() {
      return new ModelMapper();
    }
  }

  private MockMvc mockMvc;

  @Autowired
  AlertSettingController alertSettingController;

  @Autowired
  private AlertSettingsRepo alertSettingsRepo;

  @Autowired
  AlertSettingsService alertSettingsService;

  // I remove @DirtiesContext so that the entity Id is not "1" anymore
  private Long idThatExists;

  @BeforeEach
  public void setUp() {
    this.mockMvc = MockMvcBuilders.standaloneSetup(alertSettingController).build();
    var rule = WeatherAlertRule.builder().alertCode(ALR_WIND_LEVEL1).content("ALR_WIND_LEVEL1 alert rule exists").condition(1)
        .value(80).status(true).level(1)
        .color("red").deploymentId(1).build();
    idThatExists = alertSettingsRepo.save(rule).getId();
  }

  @Test
  public void createAlert_addNewOk() throws Exception {

    AlertRule rule = AlertRule.builder().id(null).alertCode(ALR_WIND_LEVEL2).content("ALR_WIND_LEVEL2 alert rule to add").condition(1)
        .value(120).level(1)
        .color("red").deploymentId(1).build();

    mockMvc.perform(post(API_PATH).content(mapToJson(rule)).accept("application/json").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    WeatherAlertRule rule2 = alertSettingsRepo.find(ALR_WIND_LEVEL2, true, 1);

    assertNotNull(rule2);
    Assertions.assertEquals(rule2.getValue().intValue(), 120);
    Assertions.assertEquals(rule2.getContent(), "ALR_WIND_LEVEL2 alert rule to add");
  }

  @Test
  public void createAlert_addNewExistsRuleCode() {

    try {
      var rule = WeatherAlertRule.builder().id(null).alertCode(ALR_WIND_LEVEL1).content("ALR_WIND_LEVEL1 alert rule to add").condition(1)
          .value(100).status(true)
          .color("red").deploymentId(1).build();

      mockMvc.perform(post(API_PATH).content(mapToJson(rule)).contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isBadRequest());
    } catch (Exception e) {
      Assertions.assertEquals(EXISTS_RULE_CODE, e.getCause().getMessage());
    }
  }

  @Test
  public void editAlert_updateOK() throws Exception {
    var rule = AlertRule.builder().id(1L).alertCode(ALR_WIND_LEVEL1).content("ALR_WIND_LEVEL1 alert rule to update").condition(1).value(150)
        .level(1)
        .color("red").deploymentId(1).build();

    mockMvc.perform(put(API_PATH + "/{id}", 1L).content(mapToJson(rule)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    WeatherAlertRule rule2 = alertSettingsRepo.find(ALR_WIND_LEVEL1, true, 1);
    assertNotNull(rule2);
    Assertions.assertEquals(rule2.getValue().intValue(), 150);
    Assertions.assertEquals(rule2.getContent(), "ALR_WIND_LEVEL1 alert rule to update");
  }

  @Test
  public void editAlert_RuleIdNotFound() throws Exception {
    var rule = AlertRule.builder().id(null).alertCode(ALR_WIND_LEVEL1).content("ALR_WIND_LEVEL1 alert rule to update").condition(1)
        .value(90).level(1)
        .color("red").deploymentId(1).build();

    try {
      mockMvc.perform(put(API_PATH + "/{id}", 123L).content(mapToJson(rule)).contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound());
    } catch (Exception e) {
      Assertions.assertEquals(ALERT_RULE_NOT_FOUND, e.getCause().getMessage());
    }
  }

  @Test
  public void editAlert_RuleCodeNotChange() {
    var rule = AlertRule.builder().id(null).alertCode(ALR_WIND_LEVEL2).content("ALR_WIND_LEVEL1 alert rule to update").condition(1)
        .value(90)
        .color("red").deploymentId(1).build();
    try {
      mockMvc.perform(put(API_PATH + "/{id}", 1).content(mapToJson(rule)).contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isBadRequest());
    } catch (Exception e) {
      Assertions.assertEquals(RULE_CODE_CHANGE, e.getCause().getMessage());
    }
  }

  @Test
  public void deleteAlert_deleteOk() throws Exception {

    mockMvc.perform(delete(API_PATH + "/{id}", idThatExists)
        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    WeatherAlertRule rule2 = alertSettingsRepo.find(ALR_WIND_LEVEL1, false, 1);
    assertNotNull(rule2);
    Assertions.assertEquals(rule2.getValue().intValue(), 80);
    Assertions.assertEquals(rule2.getContent(), "ALR_WIND_LEVEL1 alert rule exists");
    Assertions.assertEquals(rule2.getStatus(), false);
  }

  @Test
  public void deleteAlert_RuleIdNotFound() {
    try {
      mockMvc.perform(delete(API_PATH + "/{id}", 999).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNotFound());

    } catch (Exception e) {
      Assertions.assertEquals(ALERT_RULE_NOT_FOUND, e.getCause().getMessage());
    }
    WeatherAlertRule rule2 = alertSettingsRepo.find(ALR_WIND_LEVEL1, true, 1);
    assertNotNull(rule2);
    Assertions.assertEquals(rule2.getValue().intValue(), 80);
    Assertions.assertEquals(rule2.getContent(), "ALR_WIND_LEVEL1 alert rule exists");
    Assertions.assertEquals(rule2.getStatus(), true);
  }

  @Test
  public void getAlerts_Ok() throws Exception {

    MvcResult mvcRes;
    ResultActions mvcResult;
    mvcResult = this.mockMvc
        .perform(get(API_PATH).accept("application/json").param("deploymentId", "1").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
    mvcRes = mvcResult.andReturn();

    int status = mvcRes.getResponse().getStatus();
    assertEquals(200, status);
    String content = mvcRes.getResponse().getContentAsString();
    assertNotNull(content);
    List<AlertRule> rulelist;
    // RmhubResponseBody<List<AlertRule>> res = mapFromJson(content, RmhubResponseBody.class);
    rulelist = JsonPath.read(content, "$.data");
    assertEquals(1, rulelist.size());
  }

  @Test
  public void getAlerts_notOk() throws Exception {
    String uri = API_PATH + "s";
    mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON_VALUE)).andDo(print()).andExpect(status().isNotFound());
  }

  protected String mapToJson(Object obj) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(obj);
  }
}
