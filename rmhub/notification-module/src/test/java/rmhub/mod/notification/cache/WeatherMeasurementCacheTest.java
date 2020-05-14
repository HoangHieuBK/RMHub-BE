package rmhub.mod.notification.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import rmhub.model.mivisu.ssil.AlertResponse;
import rmhub.model.mivisu.ssil.CentraleResponse;
import rmhub.model.mivisu.ssil.Mesure;

@ExtendWith(SpringExtension.class)
class WeatherMeasurementCacheTest {

  @InjectMocks
  private WeatherMeasurementCache weatherMeasurementCache;

  @Test
  void getWeatherMeasure() {
    weatherMeasurementCache.getWeatherMeasure(1);
  }

  @Test
  void updateAlertResponse_centraleResponseNull() {

    AlertResponse alertResponse = AlertResponse.builder().size(1).deploymentId(1).build();
    AlertResponse cacheAlertResponse = AlertResponse.builder().size(1).deploymentId(1).build();

    weatherMeasurementCache.updateAlertResponse(alertResponse, cacheAlertResponse);

    // if CentraleResponse is not cached, return the original instance itself
    assertEquals(alertResponse, weatherMeasurementCache.updateAlertResponse(alertResponse, cacheAlertResponse));
  }

  @Test
  void updateAlertResponse() {

    Mesure mesure = Mesure.builder()
        .eqt_Dt_Mes("123")
        .eqt_Mes_Lg_Id(1)
        .eqt_Mes_Lg_Id(1)
        .eqt_Mes_Val_1(10)
        .eqt_Mes_Type("WS")
        .eqt_Mes_Id("123")
        .build();

    List<Mesure> mesureList = Collections.singletonList(mesure);

    CentraleResponse centralRest = CentraleResponse.builder().eqt_Nb_Mesure(1).externalId("123").mesures(mesureList).build();
    List<CentraleResponse> listCr = Collections.singletonList(centralRest);

    AlertResponse alertResponse = AlertResponse.builder().size(1).deploymentId(1).centraleResponses(listCr).build();
    AlertResponse cacheAlertResponse = AlertResponse.builder().size(1).deploymentId(1).centraleResponses(listCr).build();

    var mergedAlertResponse = weatherMeasurementCache.updateAlertResponse(alertResponse, cacheAlertResponse);

    assertEquals(alertResponse.getCentraleResponses().get(0).getExternalId(),
        mergedAlertResponse.getCentraleResponses().get(0).getExternalId());
  }

  @Test
  void updateAlertResponse_shouldMergeFromCache() {

    Mesure srcMesure = Mesure.builder()
        .eqt_Dt_Mes("123")
        .eqt_Mes_Lg_Id(1)
        .eqt_Mes_Val_1(10)
        .eqt_Mes_Type("WS")
        .eqt_Mes_Id("123")
        .build();
    List<Mesure> mesureSrcList = new ArrayList<>();
    mesureSrcList.add(srcMesure);

    Mesure cacheMesure = Mesure.builder()
        .eqt_Dt_Mes("123")
        .eqt_Mes_Lg_Id(1)
        .eqt_Mes_Val_1(10)
        .eqt_Mes_Type("WS")
        .eqt_Mes_Id("1234")
        .build();
    List<Mesure> mesureCacheList = new ArrayList<>();
    mesureCacheList.add(cacheMesure);

    CentraleResponse srccentraleResponse = CentraleResponse.builder().eqt_Nb_Mesure(1).externalId("123").mesures(mesureSrcList).build();
    CentraleResponse cacheCentraleResponse =
        CentraleResponse.builder().eqt_Nb_Mesure(1).externalId("1234").mesures(mesureCacheList).build();

    List<CentraleResponse> srcCenList = new ArrayList<>();
    srcCenList.add(srccentraleResponse);

    List<CentraleResponse> cacheCenList = new ArrayList<>();
    cacheCenList.add(cacheCentraleResponse);

    AlertResponse alertResponse = AlertResponse.builder().size(1).deploymentId(1).centraleResponses(srcCenList).build();
    AlertResponse cacheAlertResponse = AlertResponse.builder().size(1).deploymentId(1).centraleResponses(cacheCenList).build();

    var mergedAlertResponse = weatherMeasurementCache.updateAlertResponse(alertResponse, cacheAlertResponse);

    assertEquals(alertResponse.getCentraleResponses().get(0).getExternalId(),
        mergedAlertResponse.getCentraleResponses().get(0).getExternalId());
    assertEquals(2, mergedAlertResponse.getCentraleResponses().size());
  }

  @Test
  void updateAlertResponse_cacheIsOutdated() {
    Mesure srcMesure = Mesure.builder()
        .eqt_Dt_Mes("123")
        .eqt_Mes_Lg_Id(1)
        .eqt_Mes_Val_1(10)
        .eqt_Mes_Type("WS")
        .eqt_Mes_Id("123")
        .build();
    List<Mesure> mesureSrcList = new ArrayList<>();
    mesureSrcList.add(srcMesure);

    Mesure cacheMesure = Mesure.builder()
        .eqt_Dt_Mes("123")
        .eqt_Mes_Lg_Id(1)
        .eqt_Mes_Val_1(10)
        .eqt_Mes_Type("WS")
        .eqt_Mes_Id("1234")
        .build();
    List<Mesure> mesureCacheList = new ArrayList<>();
    mesureCacheList.add(cacheMesure);

    CentraleResponse srccentraleResponse = CentraleResponse.builder().eqt_Nb_Mesure(1).externalId("123").mesures(mesureSrcList).build();
    CentraleResponse cacheCentraleResponse =
        CentraleResponse.builder().eqt_Nb_Mesure(1).externalId("123").mesures(mesureCacheList).build();

    List<CentraleResponse> srcCenList = new ArrayList<>();
    srcCenList.add(srccentraleResponse);

    List<CentraleResponse> cacheCenList = new ArrayList<>();
    cacheCenList.add(cacheCentraleResponse);

    AlertResponse alertResponse = AlertResponse.builder().size(1).deploymentId(1).centraleResponses(srcCenList).build();
    AlertResponse cacheAlertResponse = AlertResponse.builder().size(1).deploymentId(1).centraleResponses(cacheCenList).build();

    var mergedAlertResponse = weatherMeasurementCache.updateAlertResponse(alertResponse, cacheAlertResponse);

    assertEquals(alertResponse.getCentraleResponses().get(0).getExternalId(),
        mergedAlertResponse.getCentraleResponses().get(0).getExternalId());
    assertEquals(1, mergedAlertResponse.getCentraleResponses().size());
  }
}
