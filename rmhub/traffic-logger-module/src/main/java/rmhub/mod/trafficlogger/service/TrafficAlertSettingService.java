package rmhub.mod.trafficlogger.service;

import java.util.List;
import rmhub.mod.trafficlogger.dto.request.CreateTrafficAlertSettingDto;
import rmhub.mod.trafficlogger.dto.request.UpdateTrafficAlertSettingDto;
import rmhub.mod.trafficlogger.dto.response.TrafficAlertSettingDto;

public interface TrafficAlertSettingService {

  TrafficAlertSettingDto create(CreateTrafficAlertSettingDto dto);

  TrafficAlertSettingDto update(Long id, UpdateTrafficAlertSettingDto dto);

  /**
   * Logically delete the setting.
   */
  void delete(Long id);

  /**
   * Find all resources that not logically deleted, sorted by created date in ascending order.
   */
  List<TrafficAlertSettingDto> findAll();

  TrafficAlertSettingDto findById(Long id);
}
