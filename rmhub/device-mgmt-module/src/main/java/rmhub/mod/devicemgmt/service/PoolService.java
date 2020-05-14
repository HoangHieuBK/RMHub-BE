package rmhub.mod.devicemgmt.service;

import java.util.List;
import rmhub.mod.devicemgmt.entity.Pool;

public interface PoolService {

  Pool create(Pool pool);

  Pool update(Pool pool);

  void delete(Long id);

  void deleteAll();

  Pool findByValue(String value);

  List<Pool> findAll();

  Pool findById(Long id);
}
