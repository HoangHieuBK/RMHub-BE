package rmhub.mod.trafficlogger.controller;

import static rmhub.mod.trafficlogger.common.TrafficAlertConst.API_ROOT_PATH;

import java.util.Collections;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import rmhub.common.helper.RmhubResponseEntity;
import rmhub.common.util.PathUtil;
import rmhub.mod.trafficlogger.dto.request.CreateTrafficAlertSettingDto;
import rmhub.mod.trafficlogger.dto.request.UpdateTrafficAlertSettingDto;
import rmhub.mod.trafficlogger.dto.response.TrafficAlertSettingDto;
import rmhub.mod.trafficlogger.service.TrafficAlertSettingService;

/**
 * Controller for Alert Rule resource.
 *
 * @author Hino &lt;ntquan@cmc.com.vn&gt;
 */
@RestController
@RequestMapping(path = API_ROOT_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class AlertRuleController {

  private final TrafficAlertSettingService service;

  @Autowired
  public AlertRuleController(TrafficAlertSettingService service) {
    this.service = service;
  }

  /**
   * List all Alert Rules.
   */
  @GetMapping
  RmhubResponseEntity<List<TrafficAlertSettingDto>> list(WebRequest request) {
    return RmhubResponseEntity.with(
        "Query successfully!",
        service.findAll(),
        PathUtil.getPath(request));
  }

  /**
   * Create an Alert Rule.
   */
  @PostMapping
  RmhubResponseEntity<List<TrafficAlertSettingDto>> create(@Valid @RequestBody CreateTrafficAlertSettingDto dto, WebRequest request) {
    return RmhubResponseEntity.with(
        "Create successfully!",
        Collections.singletonList(service.create(dto)),
        PathUtil.getPath(request));
  }

  /**
   * Update an Alert Rule.
   */
  @PutMapping("/{id}")
  RmhubResponseEntity<List<TrafficAlertSettingDto>> update(@PathVariable("id") Long id,
      @Valid @RequestBody UpdateTrafficAlertSettingDto dto, WebRequest request) {
    return RmhubResponseEntity.with(
        "Update successfully!",
        Collections.singletonList(service.update(id, dto)),
        PathUtil.getPath(request));
  }

  /**
   * Delete an Alert Rule.
   */
  @DeleteMapping("/{id}")
  RmhubResponseEntity<?> delete(@PathVariable("id") Long id, WebRequest request) {
    service.delete(id);
    return RmhubResponseEntity.with(
        "Delete successfully!",
        PathUtil.getPath(request));
  }
}
