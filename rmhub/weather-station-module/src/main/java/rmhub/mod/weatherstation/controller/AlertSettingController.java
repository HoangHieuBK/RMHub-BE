package rmhub.mod.weatherstation.controller;

import static rmhub.mod.weatherstation.constant.AlertRuleCommon.API_PATH;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import rmhub.common.helper.RmhubResponseEntity;
import rmhub.common.util.PathUtil;
import rmhub.mod.weatherstation.service.AlertSettingsService;
import rmhub.model.weatherStation.AlertRule;

@RestController
@RequestMapping(path = API_PATH, produces = MediaType.APPLICATION_JSON_VALUE)
public class AlertSettingController {

  private final AlertSettingsService alertSettingsService;

  @Autowired
  public AlertSettingController(AlertSettingsService alertSettingsService) {
    this.alertSettingsService = alertSettingsService;
  }

  @GetMapping
  public RmhubResponseEntity<List<AlertRule>> list(@RequestParam("deploymentId") Integer deploymentId, WebRequest request) {

    var result = alertSettingsService.findAll(deploymentId == null ? 1 : deploymentId);
    return RmhubResponseEntity.with("Success", result, PathUtil.getPath(request));
  }

  @PostMapping
  public RmhubResponseEntity<List<AlertRule>> create(@Valid @RequestBody AlertRule alertRule, WebRequest request) {
    var alert = alertSettingsService.create(alertRule);
    return RmhubResponseEntity.with("Success", Collections.singletonList(alert), PathUtil.getPath(request));
  }

  @PutMapping("/{id}")
  public RmhubResponseEntity<List<AlertRule>> update(@PathVariable("id") Long id, @Valid @RequestBody AlertRule alertRule,
      WebRequest request) {
    alertRule.setId(id);
    var alert = alertSettingsService.update(alertRule);
    return RmhubResponseEntity.with("Success", Collections.singletonList(alert), PathUtil.getPath(request));
  }

  @DeleteMapping("/{id}")
  public RmhubResponseEntity<?> delete(@PathVariable("id") Long id, WebRequest request) {
    alertSettingsService.delete(id);
    return RmhubResponseEntity.with("Success", PathUtil.getPath(request));
  }
}
