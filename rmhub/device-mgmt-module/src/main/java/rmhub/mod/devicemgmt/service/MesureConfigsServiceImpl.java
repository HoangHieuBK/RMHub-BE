package rmhub.mod.devicemgmt.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmhub.common.common.ErrorCode;
import rmhub.common.exception.BusinessException;
import rmhub.mod.devicemgmt.common.StatusEnum;
import rmhub.mod.devicemgmt.entity.MesureConfig;
import rmhub.mod.devicemgmt.repository.MesureConfigsRepo;

@Service
class MesureConfigsServiceImpl implements MesureConfigsService {

  @Autowired
  private MesureConfigsRepo mesureConfigsRepo;

  @Override
  public MesureConfig create(MesureConfig mesureConfig) {
    return mesureConfigsRepo.save(mesureConfig);
  }

  @Override
  public MesureConfig update(MesureConfig mesureConfig) {
    return mesureConfigsRepo.save(mesureConfig);
  }

  @Override
  public void delete(Long id) {
    mesureConfigsRepo.deleteById(id);
  }

  @Override
  public void deleteAll() {
    List<MesureConfig> mesureConfigs = mesureConfigsRepo.findAll();

    for (MesureConfig mesureConfig : mesureConfigs) {
      mesureConfig.setStatus(StatusEnum.DELETE.getValue());
    }

    mesureConfigsRepo.saveAll(mesureConfigs);
  }

  @Override
  public MesureConfig find(String mesureId) {
    return mesureConfigsRepo.findByMesureId(mesureId);
  }

  @Override
  public List<MesureConfig> findAll() {
    return mesureConfigsRepo.findAll();
  }

  @Override
  public MesureConfig findById(Long id) {
    return mesureConfigsRepo.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "MesureConfig doesn't exist."));
  }
}
