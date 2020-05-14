package rmhub.mod.notification.cache;

import java.util.List;
import rmhub.model.mivisu.ssil.AlertResponse;
import rmhub.model.mivisu.ssil.CentraleResponse;
import rmhub.model.mivisu.ssil.Mesure;

public interface WeatherMeasurementCacheHelper {

  static AlertResponse mergeAlertResponse(AlertResponse alertResponse, AlertResponse cacheAlertResponse) {
    List<CentraleResponse> srcCentraleResponses = alertResponse.getCentraleResponses();
    List<CentraleResponse> cacheCentraleResponses = cacheAlertResponse.getCentraleResponses();
    List<CentraleResponse> storeCentraleResponses = mergeCentraleResponse(srcCentraleResponses, cacheCentraleResponses);

    return AlertResponse.builder()
        .centraleResponses(storeCentraleResponses).size(storeCentraleResponses.size())
        .deploymentId(alertResponse.getDeploymentId()).build();
  }

  private static List<CentraleResponse> mergeCentraleResponse(List<CentraleResponse> centraleResponses,
      List<CentraleResponse> cacheCentraleResponses) {
    boolean flag = false;
    for (CentraleResponse cacheItem : cacheCentraleResponses) {

      for (CentraleResponse srcItem : centraleResponses) {

        if (srcItem.getExternalId().equals(cacheItem.getExternalId())) {
          List<Mesure> srcMesures = srcItem.getMesures();
          List<Mesure> cacheMesures = cacheItem.getMesures();
          srcItem.setMesures(megerMeasure(srcMesures, cacheMesures));
          srcItem.setEqt_Nb_Mesure(megerMeasure(srcMesures, cacheMesures).size());
          flag = true;
        }
      }
      if (flag) {
        flag = false;
      } else {
        centraleResponses.add(cacheItem);
      }
    }

    return centraleResponses;
  }

  private static List<Mesure> megerMeasure(List<Mesure> srcMesures, List<Mesure> cacheMesures) {
    boolean flag = false;
    for (Mesure cacheMesure : cacheMesures) {
      for (Mesure srcMesure : srcMesures) {
        if (cacheMesure.getEqt_Mes_Id().equals(srcMesure.getEqt_Mes_Id())) {
          flag = true;
        }
      }
      if (flag) {
        flag = false;
      } else {
        srcMesures.add(cacheMesure);
      }
    }
    return srcMesures;
  }
}
