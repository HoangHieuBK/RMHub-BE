package rmhub.mod.devicemgmt.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import rmhub.common.helper.RmhubResponseEntity;
import rmhub.common.util.PathUtil;
import rmhub.mod.devicemgmt.common.DeviceTypeEnum;
import rmhub.mod.devicemgmt.dto.DuplicatedLocationDto;
import rmhub.mod.devicemgmt.dto.LocationInfo;
import rmhub.mod.devicemgmt.dto.PhysicalDeviceResponse;
import rmhub.mod.devicemgmt.dto.ResponseDeviceInfo;
import rmhub.mod.devicemgmt.service.PhysicalDeviceService;

@RestController
@Slf4j
@RequestMapping(path = "/devices", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
public class DeviceController {

  private final PhysicalDeviceService physicalDeviceService;

  @Autowired
  public DeviceController(PhysicalDeviceService physicalDeviceService) {
    this.physicalDeviceService = physicalDeviceService;
  }

  @GetMapping
  public RmhubResponseEntity<List<PhysicalDeviceResponse>> getDevicesByType(
      @NotNull @RequestParam("deviceType") Long deviceTypeId,
      @NotNull @RequestParam("deploymentId") Long deploymentId,
      @RequestParam(value = "deviceName", required = false) String deviceName,
      WebRequest request) {

    List<PhysicalDeviceResponse> physicalDeviceResponses;

    if (StringUtils.isEmpty(deviceName)) {

      physicalDeviceResponses = physicalDeviceService.find(deviceTypeId, deploymentId);

    } else {

      physicalDeviceResponses = physicalDeviceService.findByName(deviceTypeId, deploymentId, deviceName);
    }

    return RmhubResponseEntity.with("Device found!", physicalDeviceResponses, PathUtil.getPath(request));
  }

  @DeleteMapping("/{id}")
  public RmhubResponseEntity<?> removeDevice(@PathVariable Long id, WebRequest request) {

    PhysicalDeviceResponse physicalDevice = physicalDeviceService.removeLocation(id);

    return RmhubResponseEntity.with("Device is removed!", Collections.singletonList(physicalDevice), PathUtil.getPath(request));
  }

  @GetMapping("/{id}")
  public RmhubResponseEntity<List<PhysicalDeviceResponse>> findById(@PathVariable Long id, WebRequest request) {

    PhysicalDeviceResponse result = physicalDeviceService.findById(id);

    return RmhubResponseEntity.with("Device found!", Collections.singletonList(result), PathUtil.getPath(request));
  }

  @GetMapping("/search")
  public RmhubResponseEntity<List<PhysicalDeviceResponse>> search(@RequestParam Long deploymentId,
      @RequestParam(required = false) String name,
      @RequestParam(required = false) Integer status,
      @RequestParam(name = "deviceType", required = false) Long deviceTypeId,
      WebRequest request) {

    // FIXME deviceType (1 for WS, 2 for TC) is the same as field deviceType.id in DB, but may cause bug
    List<PhysicalDeviceResponse> result = physicalDeviceService.find(deploymentId, name, status, deviceTypeId);

    return RmhubResponseEntity.with("Device found!", result, PathUtil.getPath(request));
  }

  @GetMapping("/map")
  public RmhubResponseEntity<List<ResponseDeviceInfo>> getDevices(
      @NotNull @RequestParam Long deploymentId,
      @NotNull @RequestParam Long deviceType,
      WebRequest request) {

    List<ResponseDeviceInfo> result = new ArrayList<>();

    if (Objects.equals(DeviceTypeEnum.WEATHER_STATION.getValue(), deviceType)
        || DeviceTypeEnum.TRAFFIC_COUNTING.getValue().equals(deviceType)) {

      result = physicalDeviceService.findByDeviceType(deploymentId, deviceType);

    } else if (Objects.equals(DeviceTypeEnum.ALL.getValue(), deviceType)) {

      result = physicalDeviceService.findByDeploymentId(deploymentId);
    }

    return RmhubResponseEntity.with(result.size() > 0 ? "Device found!" : "No device found!", result, PathUtil.getPath(request));
  }

  @GetMapping("/sync")
  public RmhubResponseEntity<?> syncDevice(
      @NotNull @RequestParam("deviceType") Long deviceType,
      @NotBlank @RequestParam("requestId") String requestId,
      @NotNull @RequestParam("deploymentId") Long deploymentId,
      WebRequest request) {

    physicalDeviceService.sync(deviceType, requestId, deploymentId);

    return RmhubResponseEntity.with(HttpStatus.ACCEPTED, "Request accepted!", Collections.emptyList(), PathUtil.getPath(request));
  }

  @PutMapping("/{id}/set_location")
  public ResponseEntity<?> setLocation(
      @PathVariable Long id,
      @Valid @RequestBody LocationInfo locationDto,
      WebRequest request) {

    var duplicatedDeviceName = physicalDeviceService.checkDuplicatedLocation(id, locationDto);

    if (duplicatedDeviceName.isEmpty()) {
      // in case location is not duplicated, it's set
      PhysicalDeviceResponse result = physicalDeviceService.setLocation(id, locationDto);

      return RmhubResponseEntity.with("Device location is set!", Collections.singletonList(result), PathUtil.getPath(request));

    } else {
      // in case location is duplicated, return CONFLICT status along with name of the duplicated device
      return RmhubResponseEntity.with(null, HttpStatus.CONFLICT,
          "Location is duplicated with device: " + duplicatedDeviceName.get(),
          new DuplicatedLocationDto(duplicatedDeviceName.get()),
          PathUtil.getPath(request));
    }
  }
}
