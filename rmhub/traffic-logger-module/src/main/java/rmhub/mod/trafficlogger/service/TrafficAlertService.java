package rmhub.mod.trafficlogger.service;

import java.util.List;
import rmhub.mod.trafficlogger.dto.request.UpdateTrafficAlertDto;
import rmhub.mod.trafficlogger.dto.response.TrafficAlertDto;
import rmhub.mod.trafficlogger.entity.TrafficAlert;

public interface TrafficAlertService {

  // =======================================================================================================================================
  // For consumer and producer

  TrafficAlert create(TrafficAlert trafficAlert);

  TrafficAlert update(TrafficAlert trafficAlert);

  void delete(Long id);

  List<TrafficAlert> findAll();

  /**
   * Retrieve an entity by id if it exists, otherwise throw an predefined exception.
   */
  TrafficAlert findById(Long id);

  // =======================================================================================================================================
  // For controller

  List<TrafficAlertDto> list();

  TrafficAlertDto get(Long id);

  TrafficAlertDto update(Long id, UpdateTrafficAlertDto dto);
}
