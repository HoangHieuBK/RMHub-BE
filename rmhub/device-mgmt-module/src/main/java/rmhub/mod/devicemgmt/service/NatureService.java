package rmhub.mod.devicemgmt.service;

import java.util.List;
import rmhub.mod.devicemgmt.entity.Nature;

public interface NatureService {

  Nature create(Nature nature);

  Nature update(Nature nature);

  void delete(Long id);

  void deleteAll();

  Nature find(String value);

  List<Nature> findAll();

  Nature findById(Long id);
}
