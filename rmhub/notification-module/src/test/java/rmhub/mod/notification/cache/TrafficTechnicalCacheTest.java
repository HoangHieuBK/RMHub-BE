package rmhub.mod.notification.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rmhub.model.mivisu.ssil.Et;
import rmhub.model.mivisu.ssil.TechnicalStatus;

@ExtendWith(SpringExtension.class)
class TrafficTechnicalCacheTest {

  @InjectMocks
  private TrafficTechnicalCache trafficTechnicalCache;

  @Test
  void getTrafficTechnicalStatus() {
    trafficTechnicalCache.getTrafficTechnicalStatus(1);
  }

  @Test
  void updateTrafficTechnicalStatus_etsNull() {

    TechnicalStatus cacheTechnicalStatus = TechnicalStatus.builder().nb_Et(1).deploymentId(1).build();
    TechnicalStatus technicalStatus = TechnicalStatus.builder().nb_Et(1).deploymentId(1).build();

    // if Ets is not cached, return the original instance itself
    assertEquals(technicalStatus, trafficTechnicalCache.updateTrafficTechnicalStatus(technicalStatus, cacheTechnicalStatus));
  }

  @Test
  void updateTrafficTechnicalStatus_cacheIsOutdated() {

    Et et = Et.builder().id_ext("XYZ").eqt_Conf_Version(1).etat_Alim(1).build();
    List<Et> listEt = Collections.singletonList(et);

    TechnicalStatus technicalStatus = TechnicalStatus.builder().nb_Et(1).deploymentId(1).ets(listEt).build();
    TechnicalStatus cacheTechnicalStatus = TechnicalStatus.builder().nb_Et(1).deploymentId(1).ets(listEt).build();

    var mergedTechnicalStatus = trafficTechnicalCache.updateTrafficTechnicalStatus(technicalStatus, cacheTechnicalStatus);

    assertEquals(technicalStatus.getEts().get(0).getId_ext(), mergedTechnicalStatus.getEts().get(0).getId_ext());
  }

  @Test
  void updateTrafficTechnicalStatus_shouldMergeFromCache() {

    Et et = Et.builder().id_ext("ABC").eqt_Conf_Version(1).etat_Alim(1).build();
    Et caheEt = Et.builder().id_ext("DEF").eqt_Conf_Version(2).etat_Alim(2).isInCache(true).build();

    List<Et> listEt = new ArrayList<>();
    listEt.add(et);
    List<Et> listCacheEt = Collections.singletonList(caheEt);

    TechnicalStatus technicalStatus = TechnicalStatus.builder().nb_Et(1).deploymentId(1).ets(listEt).build();
    TechnicalStatus cacheTechnicalStatus = TechnicalStatus.builder().nb_Et(1).deploymentId(1).ets(listCacheEt).build();

    var mergedTechnicalStatus = trafficTechnicalCache.updateTrafficTechnicalStatus(technicalStatus, cacheTechnicalStatus);

    assertEquals(2, mergedTechnicalStatus.getEts().size());
    assertEquals("ABC", mergedTechnicalStatus.getEts().get(0).getId_ext());
    assertEquals("DEF", mergedTechnicalStatus.getEts().get(1).getId_ext());
  }

}
