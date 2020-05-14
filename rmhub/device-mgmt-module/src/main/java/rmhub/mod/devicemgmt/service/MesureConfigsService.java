package rmhub.mod.devicemgmt.service;

import java.util.List;
import rmhub.mod.devicemgmt.entity.MesureConfig;

public interface MesureConfigsService {

  MesureConfig create(MesureConfig mesureConfig);

  MesureConfig update(MesureConfig mesureConfig);

  void delete(Long id);

  void deleteAll();

  MesureConfig find(String mesureId);

  List<MesureConfig> findAll();

  MesureConfig findById(Long id);
}
