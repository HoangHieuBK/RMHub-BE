package rmhub.mod.devicemgmt.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmhub.common.common.ErrorCode;
import rmhub.common.exception.BusinessException;
import rmhub.mod.devicemgmt.common.StatusEnum;
import rmhub.mod.devicemgmt.entity.Nature;
import rmhub.mod.devicemgmt.repository.NatureRepo;

@Service
class NatureServiceImpl implements NatureService {

  @Autowired
  private NatureRepo natureRepo;

  @Override
  public Nature create(Nature nature) {
    return natureRepo.save(nature);
  }

  @Override
  public Nature update(Nature nature) {
    return natureRepo.save(nature);
  }

  @Override
  public void delete(Long id) {
    natureRepo.deleteById(id);
  }

  @Override
  public void deleteAll() {
    List<Nature> natures = natureRepo.findAll();

    for (Nature nature : natures) {
      nature.setStatus(StatusEnum.DELETE.getValue());
    }

    natureRepo.saveAll(natures);
  }

  @Override
  public Nature find(String value) {
    return natureRepo.findByValue(value);
  }

  @Override
  public List<Nature> findAll() {
    return natureRepo.findAll();
  }

  @Override
  public Nature findById(Long id) {
    return natureRepo.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Nature doesn't exist."));
  }
}
