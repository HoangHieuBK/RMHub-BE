package rmhub.mod.trafficlogger.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.Collections;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import rmhub.mod.trafficlogger.dto.request.UpdateTrafficAlertDto;
import rmhub.mod.trafficlogger.dto.response.TrafficAlertDto;
import rmhub.mod.trafficlogger.service.TrafficAlertService;

@WebMvcTest(AlertController.class)
class AlertControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private TrafficAlertService service;

//  @Mock
//  TrafficAlertRepo alertRepo;

  private ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void list() throws Exception {

    // when
    Mockito.when(service.list()).thenReturn(Collections.singletonList(new TrafficAlertDto()));

    // then
    mockMvc.perform(get("/alerts")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status.message").value("Query succesfully!"))
        .andExpect(jsonPath("$.path").value("/alerts"));
    verify(service, Mockito.times(1)).list();
  }

  @Test
  void list_Empty() throws Exception {

    // when
    Mockito.when(service.list()).thenReturn(new ArrayList<>());

    // then
    mockMvc.perform(get("/alerts")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.status.message").value("Query succesfully!"))
        .andExpect(jsonPath("$.path").value("/alerts"))
        .andExpect(jsonPath("$.data").isEmpty());
    verify(service, Mockito.times(1)).list();
  }

  @Test
  void update() throws Exception {

    var updateTrafficAlertDto = UpdateTrafficAlertDto.builder()
        .description("abc")
        .isHandled(false)
        .build();

    var trafficAlertDto = TrafficAlertDto.builder()
        .id(1L)
        .physicalDeviceId(1L)
        .trafficMeasurementId(1L)
        .deploymentId(1L)
        .description("abc")
        .externalId(123L)
        .cause("abc")
        .isHandled(false)
        .handledBy("132")
        .build();

    // when
    when(service.update(1L, updateTrafficAlertDto)).thenReturn(trafficAlertDto);

    // then
    mockMvc.perform((put("/alerts/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(trafficAlertDto))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status.message").value("Update succesfully!"))
        .andExpect(jsonPath("$.data[0].id").value(trafficAlertDto.getId()))
        .andExpect(jsonPath("$.data[0].physicalDeviceId").value(trafficAlertDto.getPhysicalDeviceId()))
        .andExpect(jsonPath("$.data[0].trafficMeasurementId").value(trafficAlertDto.getTrafficMeasurementId()));
    verify(service, Mockito.times(1)).update(Mockito.anyLong(),Mockito.any(UpdateTrafficAlertDto.class));
  }
}
