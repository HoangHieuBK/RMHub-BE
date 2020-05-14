package rmhub.mod.trafficlogger.controller;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import rmhub.common.helper.RmhubResponseEntity;
import rmhub.common.util.PathUtil;
import rmhub.mod.trafficlogger.dto.request.UpdateTrafficAlertDto;
import rmhub.mod.trafficlogger.dto.response.TrafficAlertDto;
import rmhub.mod.trafficlogger.service.TrafficAlertService;

/**
 * Controller for Alert resource.
 *
 * @author Hino &lt;ntquan@cmc.com.vn&gt;
 */
@RestController
@RequestMapping("/alerts")
@Deprecated
public class AlertController {

  private final TrafficAlertService service;

  @Autowired
  public AlertController(TrafficAlertService service) {
    this.service = service;
  }

  /**
   * List all Alert.
   */
  @GetMapping
  RmhubResponseEntity<List<TrafficAlertDto>> list(WebRequest request) {
    return RmhubResponseEntity.with("Query succesfully!", service.list(), PathUtil.getPath(request));
  }

  /**
   * Update an Alert.
   */
  @PutMapping("/{id}")
  RmhubResponseEntity<List<TrafficAlertDto>> update(@PathVariable("id") Long id, @RequestBody UpdateTrafficAlertDto dto, WebRequest request) {
    return RmhubResponseEntity.with("Update succesfully!", Collections.singletonList(service.update(id, dto)), PathUtil.getPath(request));
  }
}
