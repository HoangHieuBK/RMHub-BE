package rmhub.mod.devicemgmt.controller;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import rmhub.mod.devicemgmt.common.DeviceConst;
import rmhub.mod.devicemgmt.common.StatusEnum;
import rmhub.mod.devicemgmt.dto.LocationInfo;
import rmhub.mod.devicemgmt.dto.PhysicalDeviceResponse;
import rmhub.mod.devicemgmt.dto.ResponseDeviceInfo;
import rmhub.mod.devicemgmt.service.PhysicalDeviceService;

@Slf4j
@ExtendWith(SpringExtension.class)
@WebMvcTest(DeviceController.class)
class PhysicalDeviceControllerTest {

  @MockBean
  private PhysicalDeviceService physicalDeviceService;

  @Autowired
  private MockMvc mvc;

  private ObjectMapper objectMapper = new ObjectMapper();

  @Test
  void testGetDevices() throws Exception {

    List<PhysicalDeviceResponse> physicalDeviceResponses = new ArrayList<>();

    PhysicalDeviceResponse data = new PhysicalDeviceResponse();
    data.setId(1L);
    data.setExternal_id("TC_96_050");
    data.setName("Traffic counting PR 96+050 (Calcul) (TC_96_050)");
    data.setStatus(StatusEnum.ACTIVE.getValue());

    physicalDeviceResponses.add(data);

    when(physicalDeviceService.find(Mockito.anyLong(), Mockito.anyLong())).thenReturn(physicalDeviceResponses);

    mvc.perform((get("/devices")
        .param("deviceType", "1")
        .param("deploymentId", "1")
        .contentType(MediaType.APPLICATION_JSON))).andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(physicalDeviceResponses.get(0).getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value(physicalDeviceResponses.get(0).getName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].external_id").value(physicalDeviceResponses.get(0).getExternal_id()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].status").value(physicalDeviceResponses.get(0).getStatus()));
  }

  @Test
  void testGetByName() throws Exception {

    List<PhysicalDeviceResponse> physicalDeviceResponses = new ArrayList<>();

    PhysicalDeviceResponse data = new PhysicalDeviceResponse();
    data.setId(1L);
    data.setExternal_id("TC_96_050");
    data.setName("Traffic counting PR 96+050 (Calcul) (TC_96_050)");
    data.setStatus(StatusEnum.ACTIVE.getValue());

    physicalDeviceResponses.add(data);

    when(physicalDeviceService.findByName(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyString())).thenReturn(physicalDeviceResponses);

    mvc.perform((get("/devices/")
        .param("deviceType", "1")
        .param("deploymentId", "1")
        .param("deviceName", "1")
        .contentType(MediaType.APPLICATION_JSON))).andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(physicalDeviceResponses.get(0).getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value(physicalDeviceResponses.get(0).getName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].external_id").value(physicalDeviceResponses.get(0).getExternal_id()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].status").value(physicalDeviceResponses.get(0).getStatus()));
  }

  @Test
  void testDelete() throws Exception {

    PhysicalDeviceResponse physicalDevice = new PhysicalDeviceResponse();
    physicalDevice.setRegistration(DeviceConst.UNREGISTERED);
    physicalDevice.setLatitude(null);
    physicalDevice.setLongitude(null);

    when(physicalDeviceService.removeLocation(Mockito.anyLong())).thenReturn(physicalDevice);

    mvc.perform((delete("/devices/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content("")))
        .andExpect(status().isOk());
  }

  @Test
  void testSync() throws Exception {

    doNothing().when(physicalDeviceService).sync(Mockito.anyLong(), Mockito.anyString(), Mockito.anyLong());

    mvc.perform((get("/devices/sync")
        .contentType(MediaType.APPLICATION_JSON)
        .param("deviceType", "1")
        .param("requestId", "requestId")
        .param("deploymentId", "1")
        .content("")))
        .andExpect(status().isAccepted());
  }

  @Test
  void testSearch() throws Exception {
    List<PhysicalDeviceResponse> physicalDeviceResponses = new ArrayList<>();

    PhysicalDeviceResponse data = new PhysicalDeviceResponse();
    data.setId(1L);
    data.setExternal_id("TC_96_050");
    data.setName("Traffic counting PR 96+050 (Calcul) (TC_96_050)");
    data.setStatus(StatusEnum.ACTIVE.getValue());

    physicalDeviceResponses.add(data);

    when(physicalDeviceService.find(Mockito.anyLong(), Mockito.anyString(), Mockito.anyInt(), Mockito.anyLong()))
        .thenReturn(physicalDeviceResponses);

    mvc.perform((get("/devices/search")
        .param("deploymentId", "1")
        .param("name", "device name")
        .param("status", "1")
        .param("deviceType", "1")
        .contentType(MediaType.APPLICATION_JSON))).andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(physicalDeviceResponses.get(0).getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name").value(physicalDeviceResponses.get(0).getName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].external_id").value(physicalDeviceResponses.get(0).getExternal_id()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].status").value(physicalDeviceResponses.get(0).getStatus()));
  }

  @Test
  void testMapGetDevice_byType_weather() throws Exception {
    List<ResponseDeviceInfo> responseDeviceInfos = new ArrayList<>();

    ResponseDeviceInfo data = new ResponseDeviceInfo();
    data.setId(1L);
    data.setDeviceTypeId(1L);
    data.setExternalId("TC_96_050");
    data.setDeviceName("Traffic counting PR 96+050 (Calcul) (TC_96_050)");

    responseDeviceInfos.add(data);

    when(physicalDeviceService.findByDeviceType(Mockito.anyLong(), Mockito.anyLong())).thenReturn(responseDeviceInfos);

    mvc.perform((get("/devices/map")
        .param("deploymentId", "1")
        .param("deviceType", "1")
        .contentType(MediaType.APPLICATION_JSON))).andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(responseDeviceInfos.get(0).getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].deviceName").value(responseDeviceInfos.get(0).getDeviceName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].externalId").value(responseDeviceInfos.get(0).getExternalId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].deviceTypeId").value(responseDeviceInfos.get(0).getDeviceTypeId()));
  }

  @Test
  void testMapGetDevice_byType_traffic() throws Exception {
    List<ResponseDeviceInfo> responseDeviceInfos = new ArrayList<>();

    ResponseDeviceInfo data = new ResponseDeviceInfo();
    data.setId(1L);
    data.setDeviceTypeId(1L);
    data.setExternalId("TC_96_050");
    data.setDeviceName("Traffic counting PR 96+050 (Calcul) (TC_96_050)");

    responseDeviceInfos.add(data);

    when(physicalDeviceService.findByDeviceType(Mockito.anyLong(), Mockito.anyLong())).thenReturn(responseDeviceInfos);

    mvc.perform((get("/devices/map")
        .param("deploymentId", "1")
        .param("deviceType", "2")
        .contentType(MediaType.APPLICATION_JSON))).andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(responseDeviceInfos.get(0).getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].deviceName").value(responseDeviceInfos.get(0).getDeviceName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].externalId").value(responseDeviceInfos.get(0).getExternalId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].deviceTypeId").value(responseDeviceInfos.get(0).getDeviceTypeId()));
  }

  @Test
  void testMapGetDevice_byDeploymentId() throws Exception {
    List<ResponseDeviceInfo> responseDeviceInfos = new ArrayList<>();

    ResponseDeviceInfo data = new ResponseDeviceInfo();
    data.setId(1L);
    data.setDeviceTypeId(1L);
    data.setExternalId("TC_96_050");
    data.setDeviceName("Traffic counting PR 96+050 (Calcul) (TC_96_050)");

    responseDeviceInfos.add(data);

    when(physicalDeviceService.findByDeploymentId(Mockito.anyLong())).thenReturn(responseDeviceInfos);

    mvc.perform((get("/devices/map")
        .param("deploymentId", "1")
        .param("deviceType", "0")
        .contentType(MediaType.APPLICATION_JSON))).andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id").value(responseDeviceInfos.get(0).getId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].deviceName").value(responseDeviceInfos.get(0).getDeviceName()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].externalId").value(responseDeviceInfos.get(0).getExternalId()))
        .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].deviceTypeId").value(responseDeviceInfos.get(0).getDeviceTypeId()));
  }

  @Test
  void testMapGetDevice_byDeploymentId_empty() throws Exception {
    List<ResponseDeviceInfo> responseDeviceInfos = new ArrayList<>();

    ResponseDeviceInfo data = new ResponseDeviceInfo();
    data.setId(1L);
    data.setDeviceTypeId(1L);
    data.setExternalId("TC_96_050");
    data.setDeviceName("Traffic counting PR 96+050 (Calcul) (TC_96_050)");

    responseDeviceInfos.add(data);

    when(physicalDeviceService.findByDeploymentId(Mockito.anyLong())).thenReturn(responseDeviceInfos);

    mvc.perform((get("/devices/map")
        .param("deploymentId", "1")
        .param("deviceType", "3")
        .contentType(MediaType.APPLICATION_JSON))).andExpect(status().isOk());
  }

  /**
   * Please follow this test case as an example
   */
  @Test
  void setLocation_ok() throws Exception {
    var locationDTO = LocationInfo.builder().latitude(80D).longitude(123D).build();

    PhysicalDeviceResponse responseDto = new PhysicalDeviceResponse();
    responseDto.setName("Traffic counting PR 96+050 (Calcul) (TC_96_050)");
    responseDto.setId(123L);
    responseDto.setStatus(StatusEnum.ACTIVE.getValue());
    responseDto.setLatitude(80D);
    responseDto.setLongitude(123D);

    when(physicalDeviceService.setLocation(123L, locationDTO)).thenReturn(responseDto);

    mvc.perform((put("/devices/123/set_location")
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(StandardCharsets.UTF_8.name())
        .content(objectMapper.writeValueAsString(locationDTO))))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data[0].id").value(responseDto.getId()))
        .andExpect(jsonPath("$.data[0].latitude").value(responseDto.getLatitude()))
        .andExpect(jsonPath("$.data[0].longitude").value(responseDto.getLongitude()));
        // use this for debugging your test case
        // .andDo(print());
  }

  /**
   * Please follow this test case as an example
   */
  @Test
  void setLocation_error_duplicated() throws Exception {
    var locationDTO = LocationInfo.builder().latitude(80D).longitude(123D).build();

    when(physicalDeviceService.checkDuplicatedLocation(123L, locationDTO)).thenReturn(Optional.of("dummy"));

    mvc.perform((put("/devices/123/set_location")
        .contentType(MediaType.APPLICATION_JSON)
        .characterEncoding(StandardCharsets.UTF_8.name())
        .content(objectMapper.writeValueAsString(locationDTO))))
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.data.deviceName").value("dummy"));
        // use this for debugging your test case
        // .andDo(print());
  }

  @Test
  void findDeviceByIdOk() throws Exception {

    PhysicalDeviceResponse physicalDeviceDto = new PhysicalDeviceResponse();
    physicalDeviceDto.setName("Traffic counting PR 96+050 (Calcul) (TC_96_050)");
    physicalDeviceDto.setId(123L);
    physicalDeviceDto.setStatus(StatusEnum.ACTIVE.getValue());

    when(physicalDeviceService.findById(123L)).thenReturn(physicalDeviceDto);

    assertEquals(123L, physicalDeviceDto.getId().byteValue());
    assertEquals(1, physicalDeviceDto.getStatus().byteValue());
    assertEquals("Traffic counting PR 96+050 (Calcul) (TC_96_050)", physicalDeviceDto.getName());

    mvc.perform((get("/devices/123")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(physicalDeviceDto)))).andExpect(status().isOk());
  }
}
