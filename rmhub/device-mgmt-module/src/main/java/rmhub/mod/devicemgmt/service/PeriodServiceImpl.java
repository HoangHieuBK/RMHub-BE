package rmhub.mod.devicemgmt.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmhub.common.common.ErrorCode;
import rmhub.common.exception.BusinessException;
import rmhub.mod.devicemgmt.common.StatusEnum;
import rmhub.mod.devicemgmt.entity.Period;
import rmhub.mod.devicemgmt.repository.PeriodRepo;

@Service
class PeriodServiceImpl implements PeriodService {

  @Autowired
  private PeriodRepo periodRepo;

  @Override
  public Period create(Period period) {
    return periodRepo.save(period);
  }

  @Override
  public Period update(Period period) {
    return periodRepo.save(period);
  }

  @Override
  public void delete(Long id) {
    periodRepo.deleteById(id);
  }

  @Override
  public void deleteAll() {
    List<Period> periods = periodRepo.findAll();

    for (Period period : periods) {
      period.setStatus(StatusEnum.DELETE.getValue());
    }

    periodRepo.saveAll(periods);
  }

  @Override
  public Period find(String value) {
    return periodRepo.findByValue(value);
  }

  @Override
  public List<Period> findAll() {
    return periodRepo.findAll();
  }

  @Override
  public Period findById(Long id) {
    return periodRepo.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Period doesn't exist."));
  }
}
