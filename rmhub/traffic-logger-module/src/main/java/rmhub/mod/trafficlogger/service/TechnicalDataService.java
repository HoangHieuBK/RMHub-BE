package rmhub.mod.trafficlogger.service;

import java.util.List;
import rmhub.mod.trafficlogger.entity.TechnicalData;


public interface TechnicalDataService {

  TechnicalData create(TechnicalData technicalData);

  TechnicalData update(TechnicalData technicalData);

  void delete(Long id);

  List<TechnicalData> findAll();

  TechnicalData findById(Long id);

}
