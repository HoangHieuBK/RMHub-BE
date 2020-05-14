package rmhub.mod.trafficlogger.behaviour;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import rmhub.common.constant.MivisuMessageConstant;
import rmhub.common.utility.ParserHelper;
import rmhub.mod.trafficlogger.dto.response.TrafficAlertSettingDto;
import rmhub.mod.trafficlogger.entity.TrafficAlert;
import rmhub.mod.trafficlogger.entity.TrafficMeasurement;
import rmhub.mod.trafficlogger.entity.TrafficMeasurementDetail;
import rmhub.mod.trafficlogger.producer.TrafficProducer;
import rmhub.mod.trafficlogger.service.TrafficAlertService;
import rmhub.mod.trafficlogger.service.TrafficAlertSettingService;
import rmhub.mod.trafficlogger.service.TrafficMeasurementService;
import rmhub.model.mivisu.ssil.AlertResponse;
import rmhub.model.mivisu.ssil.AlertTraffic;
import rmhub.model.mivisu.ssil.Body;
import rmhub.model.mivisu.ssil.Centrale;
import rmhub.model.mivisu.ssil.CentraleResponse;
import rmhub.model.mivisu.ssil.Mesure;
import rmhub.model.mivisu.ssil.MivisuXml;

@Profile("!test")
@Service
@Slf4j
public class GenerateTrafficAlertImpl implements GenerateTrafficAlert {

  @Autowired
  private TrafficProducer trafficProducer;

  @Autowired
  private TrafficMeasurementService trafficMeasurementService;

  @Autowired
  private TrafficAlertService trafficAlertService;

  @Autowired
  private TrafficAlertSettingService trafficLoggerAlertService;

  @Value("${eqt.mesure.period}")
  private String period;

  @Value("${eqt.mes.star}")
  private String star;

  @Value("${eqt.mesure.alr}")
  private String alr;

  @Value("${eqt.mesure.vt.type}")
  private String vtType;

  @Override
  public void processData(MivisuXml mivisuData) {

    log.info("process traffic counting");
    Body body = mivisuData.getBody();
    int deploymentId = mivisuData.getDeploymentId();
    Body resBody = SerializationUtils.clone(body);
    boolean flag = false;
    if (body != null && body.getRep_Nb_Eqt() > 0) {
      List<Centrale> centrals = body.getCentrale();

      for (int i = 0; i < centrals.size(); i++) {
        Centrale centrale = body.getCentrale().get(i);
        String extenalId = centrale.getId_ext();
        for (int j = 0; j < centrale.getMesure().size(); j++) {
          Map<String, Object> mesure = centrale.getMesure().get(j);
          TrafficMeasurement trafficMeasure = saveMeasureData(mesure, extenalId, deploymentId); // store measurement data
          resBody.getCentrale().get(i).getMesure().get(j).put(MivisuMessageConstant.TRAFFIC_MES_ID, trafficMeasure.getId());
          if (!flag) {
            flag = isSelectedDataMesure(mesure);
          }
        }
      }
    }
    if (flag) {
      generateAlert(resBody, deploymentId);
    }
  }

  private void generateAlert(Body body, int deploymentId) {

    List<CentraleResponse> centraleListResponse = new ArrayList<>();
    List<Mesure> mesureListResponse;

    List<TrafficAlertSettingDto> listTrafficAlert = trafficLoggerAlertService.findAll();
    listTrafficAlert.sort((Comparator.comparing(TrafficAlertSettingDto::getMin)));
    List<Centrale> centrale = body.getCentrale();

    for (Centrale cen : centrale) {
      String idExt = cen.getId_ext();
      int eqtNbMesure = cen.getMesure().size();
      mesureListResponse = new ArrayList<>();
      for (int j = 0; j < eqtNbMesure; j++) {
        Map<String, Object> mesureItem = cen.getMesure().get(j);
        Mesure mesureResponse = getMesureValue(mesureItem);
        if (!mesureResponse.getEqt_Mes_Id().contains(vtType)) {
          mesureListResponse.add(mesureResponse);
          continue;
        }
        int value = mesureResponse.getEqt_Mes_Val_1();
        for (TrafficAlertSettingDto item : listTrafficAlert) {
          if (value >= item.getMin() && value <= item.getMax()) {
            AlertTraffic alertTraffic = new AlertTraffic(item.getColor(), item.getDescription(), item.getLevel());
            mesureResponse.setAlertBase(alertTraffic);
            saveTrafficAlert(deploymentId, idExt, mesureItem);
          }
        }
        mesureListResponse.add(mesureResponse);
      }
      CentraleResponse centrale1Response = new CentraleResponse(mesureListResponse, mesureListResponse.size(), idExt);
      centraleListResponse.add(centrale1Response);
    }

    AlertResponse alertResponse = AlertResponse.builder()
        .deploymentId(deploymentId)
        .size(centraleListResponse.size())
        .centraleResponses(centraleListResponse).build();

    // notification to client
    trafficProducer.sendAlertToKafka(alertResponse);
  }

  private void saveTrafficAlert(int deploymentId, String idExt, Map<String, Object> mesureItem) {
    int trafficMeasurementId = ParserHelper.convertString2Int(
        mesureItem.getOrDefault(MivisuMessageConstant.TRAFFIC_MES_ID, MivisuMessageConstant.DEFAULT_INT_VALUE).toString());

    TrafficAlert trafficAlert = TrafficAlert.builder()
        .trafficMeasurementId((long) trafficMeasurementId)
        .deploymentId(deploymentId)
        .externalId(idExt)
        .isHandled(false)
        .build();

    trafficAlertService.create(trafficAlert);
  }

  private Mesure getMesureValue(Map<String, Object> mesureItem) {

    int num = ParserHelper
        .convertString2Int(mesureItem.getOrDefault(MivisuMessageConstant.NUM, MivisuMessageConstant.DEFAULT_INT_VALUE).toString());
    String eqtDtMes = mesureItem.getOrDefault(MivisuMessageConstant.EQT_DT_MES, MivisuMessageConstant.DEFAULT_STRING_VALUE).toString();
    int eqtMesPer = ParserHelper
        .convertString2Int(mesureItem.getOrDefault(MivisuMessageConstant.EQT_MES_PER, MivisuMessageConstant.DEFAULT_INT_VALUE).toString());
    int eqtMesLgId = ParserHelper
        .convertString2Int(
            mesureItem.getOrDefault(MivisuMessageConstant.EQT_MES_LG_ID, MivisuMessageConstant.DEFAULT_INT_VALUE).toString());
    String eqtMesId = mesureItem.getOrDefault(MivisuMessageConstant.EQT_MES_ID, MivisuMessageConstant.DEFAULT_STRING_VALUE).toString();
    int eqtMesLgType = ParserHelper
        .convertString2Int(
            mesureItem.getOrDefault(MivisuMessageConstant.EQT_MES_LG_TYPE, MivisuMessageConstant.DEFAULT_INT_VALUE).toString());
    String eqtMesType = mesureItem.getOrDefault(MivisuMessageConstant.EQT_MES_TYPE, MivisuMessageConstant.DEFAULT_STRING_VALUE).toString();
    int eqtMesNbVal = ParserHelper
        .convertString2Int(
            mesureItem.getOrDefault(MivisuMessageConstant.EQT_MES_NB_VAL, MivisuMessageConstant.DEFAULT_INT_VALUE).toString());
    int eqtMesVal = ParserHelper
        .convertString2Int(
            mesureItem.getOrDefault(MivisuMessageConstant.EQT_MES_VAL_1, MivisuMessageConstant.DEFAULT_INT_VALUE).toString());
    String eqtMesKlif = mesureItem.getOrDefault(MivisuMessageConstant.EQT_MES_KLIF_1, MivisuMessageConstant.DEFAULT_STRING_VALUE)
        .toString();

    return new Mesure(num, eqtDtMes, eqtMesPer, eqtMesLgId, eqtMesId, eqtMesLgType, eqtMesType, eqtMesNbVal, eqtMesVal,
        eqtMesKlif, new AlertTraffic());
  }

  private TrafficMeasurement saveMeasureData(Map<String, Object> mesure, String externalId, Integer deploymentId) {
    String eqtDtMes = mesure.getOrDefault(MivisuMessageConstant.EQT_DT_MES, MivisuMessageConstant.DEFAULT_STRING_VALUE).toString();
    int eqtMesPer = ParserHelper
        .convertString2Int(mesure.getOrDefault(MivisuMessageConstant.EQT_MES_PER, MivisuMessageConstant.DEFAULT_INT_VALUE).toString());
    int eqtMesLgId = ParserHelper
        .convertString2Int(mesure.getOrDefault(MivisuMessageConstant.EQT_MES_LG_ID, MivisuMessageConstant.DEFAULT_INT_VALUE).toString());
    String eqtMesId = mesure.getOrDefault(MivisuMessageConstant.EQT_MES_ID, MivisuMessageConstant.DEFAULT_STRING_VALUE).toString();
    int eqtMesLgType = ParserHelper
        .convertString2Int(mesure.getOrDefault(MivisuMessageConstant.EQT_MES_LG_TYPE, MivisuMessageConstant.DEFAULT_INT_VALUE).toString());
    String eqtMesType = mesure.getOrDefault(MivisuMessageConstant.EQT_MES_TYPE, MivisuMessageConstant.DEFAULT_STRING_VALUE).toString();
    int eqtMesNbVal = ParserHelper
        .convertString2Int(mesure.getOrDefault(MivisuMessageConstant.EQT_MES_NB_VAL, MivisuMessageConstant.DEFAULT_INT_VALUE).toString());

    TrafficMeasurement trafficMeasure = TrafficMeasurement.builder()
        .eqtDtMes(eqtDtMes)
        .eqtMesPer(eqtMesPer)
        .eqtMesLgId(eqtMesLgId)
        .eqtMesId(eqtMesId)
        .eqtMesLgType(eqtMesLgType)
        .eqtMesType(eqtMesType)
        .eqtMesNbVal(eqtMesNbVal)
        .externalId(externalId)
        .deploymentId(deploymentId)
        .build();

    for (int idx = 1; idx <= eqtMesNbVal; idx++) {
      String eqtMesVal = mesure.getOrDefault(MivisuMessageConstant.EQT_MES_VAL_ + idx, MivisuMessageConstant.DEFAULT_STRING_VALUE)
          .toString();
      String eqtMesKlif = mesure.getOrDefault(MivisuMessageConstant.EQT_MES_KLIF_ + idx, MivisuMessageConstant.DEFAULT_STRING_VALUE)
          .toString();

      TrafficMeasurementDetail trafficMeasureDetail = TrafficMeasurementDetail.builder()
          .eqtMesVal(eqtMesVal)
          .eqtMesKlif(eqtMesKlif)
          .index(idx)
          .build();

      trafficMeasureDetail.setTrafficMeasurement(trafficMeasure);
      trafficMeasure.getTrafficMeasurementDetail().add(trafficMeasureDetail);
    }

    return trafficMeasurementService.create(trafficMeasure);
  }

  private boolean isSelectedDataMesure(Map<String, Object> mesure) {

    String eqtMesPer = mesure.getOrDefault(MivisuMessageConstant.EQT_MES_PER, MivisuMessageConstant.DEFAULT_STRING_VALUE).toString();
    String eqtMesId = mesure.getOrDefault(MivisuMessageConstant.EQT_MES_ID, MivisuMessageConstant.DEFAULT_STRING_VALUE).toString();

    return period.equals(eqtMesPer) && eqtMesId.contains(star) && !eqtMesId.contains(alr);
  }
}
