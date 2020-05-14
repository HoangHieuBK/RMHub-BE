package rmhub.mod.notification.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import rmhub.model.mivisu.ssil.TechnicalStatus;

@Component
@Slf4j
public class TrafficTechnicalCache {

  @Cacheable(value = "trafficTechnicalStatus", key = "#p0")
  public TechnicalStatus getTrafficTechnicalStatus(int deploymentId) {
    return new TechnicalStatus();
  }

  @CachePut(value = "trafficTechnicalStatus", key = "#technicalStatus.deploymentId")
  public TechnicalStatus updateTrafficTechnicalStatus(TechnicalStatus technicalStatus, TechnicalStatus cacheTechnicalStatus) {

    log.info("Traffic technical updating cache data...");

    if (cacheTechnicalStatus.getEts() == null) {
      return technicalStatus;
    }
    return TrafficTechnicalCacheHelper.mergeTrafficTechnicalResponse(technicalStatus, cacheTechnicalStatus);
  }
}
