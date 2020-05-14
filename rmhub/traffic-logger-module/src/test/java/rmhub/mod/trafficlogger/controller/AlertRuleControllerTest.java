package rmhub.mod.trafficlogger.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static rmhub.mod.trafficlogger.common.TrafficAlertConst.API_ROOT_PATH;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import rmhub.mod.trafficlogger.dto.request.CreateTrafficAlertSettingDto;
import rmhub.mod.trafficlogger.dto.request.UpdateTrafficAlertSettingDto;
import rmhub.mod.trafficlogger.dto.response.TrafficAlertSettingDto;
import rmhub.mod.trafficlogger.service.TrafficAlertSettingService;

@WebMvcTest(AlertRuleController.class)
class AlertRuleControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TrafficAlertSettingService service;

  private ObjectMapper objectMapper = new ObjectMapper();

  private final TrafficAlertSettingDto dummyTrafficAlertSettingDto = TrafficAlertSettingDto.builder()
      .id(1L)
      .level(1)
      .color("#FFF")
      .description("dummy")
      .min(1)
      .max(10)
      .createdDate(new Date())
      .lastModifiedDate(new Date())
      .build();

  @Test
  void list_alertRule() throws Exception {

    // when
    Mockito.when(service.findAll()).thenReturn(Collections.singletonList(dummyTrafficAlertSettingDto));

    // then
    mockMvc.perform(get(API_ROOT_PATH)
        .characterEncoding(StandardCharsets.UTF_8.name())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status.message").value("Query successfully!"))
        .andExpect(jsonPath("$.path").value(API_ROOT_PATH))
        .andExpect(jsonPath("$.data[0].color").value("#FFF"));

    // verify
    verify(service, Mockito.times(1)).findAll();
  }

  @Test
  void list_alertRule_empty() throws Exception {

    // when
    Mockito.when(service.findAll()).thenReturn(new ArrayList<>());

    // then
    mockMvc.perform(get(API_ROOT_PATH)
        .characterEncoding(StandardCharsets.UTF_8.name())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status.message").value("Query successfully!"))
        .andExpect(jsonPath("$.path").value(API_ROOT_PATH))
        .andExpect(jsonPath("$.data").isEmpty());

    // verify
    verify(service, Mockito.times(1)).findAll();
  }

  @Test
  void update_alertRule() throws Exception {

    var updateTrafficAlertSettingDto = UpdateTrafficAlertSettingDto.builder()
        .level(1)
        .color("#656565")
        .description("123")
        .min(1)
        .max(10)
        .build();

    // when
    when(service.update(1L, updateTrafficAlertSettingDto)).thenReturn(dummyTrafficAlertSettingDto);

    // then
    mockMvc.perform((put(API_ROOT_PATH + "/1")
        .characterEncoding(StandardCharsets.UTF_8.name())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateTrafficAlertSettingDto))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status.message").value("Update successfully!"))
        .andExpect(jsonPath("$.data[0].id").value(dummyTrafficAlertSettingDto.getId()))
        .andExpect(jsonPath("$.data[0].level").value(dummyTrafficAlertSettingDto.getLevel()))
        .andExpect(jsonPath("$.data[0].color").value(dummyTrafficAlertSettingDto.getColor()));

    // verify
    verify(service, Mockito.times(1)).update(Mockito.anyLong(), Mockito.any(UpdateTrafficAlertSettingDto.class));

  }

  @Test
  void create_alertRule() throws Exception {

    var createTrafficAlertSettingDto = CreateTrafficAlertSettingDto.builder()
        .level(1)
        .color("#656565")
        .description("123")
        .min(1)
        .max(10)
        .build();

    // when
    when(service.create(createTrafficAlertSettingDto)).thenReturn(dummyTrafficAlertSettingDto);

    // then
    mockMvc.perform((post(API_ROOT_PATH)
        .characterEncoding(StandardCharsets.UTF_8.name())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createTrafficAlertSettingDto))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status.message").value("Create successfully!"))
        .andExpect(jsonPath("$.data[0].id").value(dummyTrafficAlertSettingDto.getId()))
        .andExpect(jsonPath("$.data[0].level").value(dummyTrafficAlertSettingDto.getLevel()))
        .andExpect(jsonPath("$.data[0].color").value(dummyTrafficAlertSettingDto.getColor()));

    // verify
    verify(service, Mockito.times(1)).create(Mockito.any(CreateTrafficAlertSettingDto.class));
  }

  /**
   * min and max are not null, and min is less than max
   */
  @Test
  void create_alertRule_minMaxCorrelationValidator_minIsLessThanMax() throws Exception {

    var createTrafficAlertSettingDto = CreateTrafficAlertSettingDto.builder()
        .level(1)
        .color("#656565")
        .description("123")
        .min(6)
        .max(9)
        .build();

    // when
    when(service.create(createTrafficAlertSettingDto)).thenReturn(dummyTrafficAlertSettingDto);

    // then
    mockMvc.perform((post(API_ROOT_PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(StandardCharsets.UTF_8.name())
        .content(objectMapper.writeValueAsString(createTrafficAlertSettingDto))))
        .andExpect(status().isOk());

    // verify
    verify(service, Mockito.times(1)).create(Mockito.any(CreateTrafficAlertSettingDto.class));
  }

  /**
   * min and max are not null, and min is greater than max
   */
  @Test
  void create_alertRule_minMaxCorrelationValidator_minIsGreaterThanMax() throws Exception {

    var createTrafficAlertSettingDto = CreateTrafficAlertSettingDto.builder()
        .level(1)
        .color("#656565")
        .description("123")
        .min(9)
        .max(6)
        .build();

    // when
    when(service.create(createTrafficAlertSettingDto)).thenReturn(dummyTrafficAlertSettingDto);

    // then
    mockMvc.perform((post(API_ROOT_PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(StandardCharsets.UTF_8.name())
        .content(objectMapper.writeValueAsString(createTrafficAlertSettingDto))))
        .andExpect(status().isBadRequest());

    // verify
    verify(service, Mockito.never()).create(Mockito.any(CreateTrafficAlertSettingDto.class));
  }

  @Test
  void delete_Alert_rule() throws Exception {
    mockMvc.perform(delete(API_ROOT_PATH + "/1")
        .characterEncoding(StandardCharsets.UTF_8.name())
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status.message").value("Delete successfully!"));

    // verify
    verify(service, Mockito.times(1)).delete(Mockito.anyLong());
  }
}
