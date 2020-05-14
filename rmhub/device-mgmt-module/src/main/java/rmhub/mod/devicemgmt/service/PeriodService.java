package rmhub.mod.devicemgmt.service;

import java.util.List;
import rmhub.mod.devicemgmt.entity.Period;

public interface PeriodService {

  Period create(Period period);

  Period update(Period period);

  void delete(Long id);

  void deleteAll();

  Period find(String value);

  List<Period> findAll();

  Period findById(Long id);
}
