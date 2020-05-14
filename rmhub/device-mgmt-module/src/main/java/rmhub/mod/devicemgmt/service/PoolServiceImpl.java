package rmhub.mod.devicemgmt.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmhub.common.common.ErrorCode;
import rmhub.common.exception.BusinessException;
import rmhub.mod.devicemgmt.common.StatusEnum;
import rmhub.mod.devicemgmt.entity.Pool;
import rmhub.mod.devicemgmt.repository.PoolRepo;

@Service
class PoolServiceImpl implements PoolService {

  @Autowired
  private PoolRepo poolRepo;

  @Override
  public Pool create(Pool pool) {
    return poolRepo.save(pool);
  }

  @Override
  public Pool update(Pool pool) {
    return poolRepo.save(pool);
  }

  @Override
  public void delete(Long id) {
    poolRepo.deleteById(id);
  }

  @Override
  public void deleteAll() {
    List<Pool> pools = poolRepo.findAll();

    for (Pool pool : pools) {
      pool.setStatus(StatusEnum.DELETE.getValue());
    }

    poolRepo.saveAll(pools);
  }

  @Override
  public Pool findByValue(String value) {
    return poolRepo.findByValue(value);
  }

  @Override
  public List<Pool> findAll() {
    return poolRepo.findAll();
  }

  @Override
  public Pool findById(Long id) {
    return poolRepo.findById(id).orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND, "Pool doesn't exist."));
  }
}

