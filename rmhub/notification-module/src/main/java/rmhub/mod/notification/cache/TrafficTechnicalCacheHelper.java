package rmhub.mod.notification.cache;

import java.util.List;
import rmhub.model.mivisu.ssil.Et;
import rmhub.model.mivisu.ssil.TechnicalStatus;

public interface TrafficTechnicalCacheHelper {

  static TechnicalStatus mergeTrafficTechnicalResponse(
      TechnicalStatus technicalStatus, TechnicalStatus cacheTechnicalStatus) {

    List<Et> ets = technicalStatus.getEts();
    List<Et> cacheEts = cacheTechnicalStatus.getEts();
    boolean flag = false;
    for (Et cacheEt : cacheEts) {
      for (Et et : ets) {
        if (cacheEt.getId_ext().equals(et.getId_ext())) {
          flag = true;
          break;
        }
      }
      if (flag) {
        flag = false;
      } else {
        cacheEt.setInCache(true);
        ets.add(cacheEt);
      }
    }
    return technicalStatus;
  }
}
