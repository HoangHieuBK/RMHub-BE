package rmhub.mod.trafficlogger.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmhub.common.common.ErrorCode;
import rmhub.common.exception.BusinessException;
import rmhub.mod.trafficlogger.entity.TechnicalData;
import rmhub.mod.trafficlogger.repository.TechnicalDataRepo;

@Service
class TechnicalDataServiceImpl implements TechnicalDataService {

  @Autowired
  private TechnicalDataRepo technicalDataRepo;

  @Override
  public TechnicalData create(TechnicalData technicalData) {
    return technicalDataRepo.save(technicalData);
  }

  @Override
  public TechnicalData update(TechnicalData technicalData) {
    return technicalDataRepo.save(technicalData);
  }

  @Override
  public void delete(Long id) {
    technicalDataRepo.deleteById(id);
  }

  @Override
  public List<TechnicalData> findAll() {
    return technicalDataRepo.findAll();
  }

  @Override
  public TechnicalData findById(Long id) {
    return technicalDataRepo.findById(id).orElseThrow(
        () -> new BusinessException(ErrorCode.NOT_FOUND, "TechnicalData doesn't exist."));
  }
}
