package rmhub.mod.weatherstation.behaviour;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rmhub.common.constant.MivisuMessageConstant;
import rmhub.common.utility.ParserHelper;
import rmhub.mod.weatherstation.constant.AlertRuleCode;
import rmhub.mod.weatherstation.constant.AlertRuleOperation;
import rmhub.mod.weatherstation.constant.Identification;
import rmhub.mod.weatherstation.entity.WeatherAlert;
import rmhub.mod.weatherstation.entity.WeatherAlertRule;
import rmhub.mod.weatherstation.entity.WeatherMeasurement;
import rmhub.mod.weatherstation.entity.WeatherMeasurementDetail;
import rmhub.mod.weatherstation.producer.WeatherProducer;
import rmhub.mod.weatherstation.service.AlertSettingsService;
import rmhub.mod.weatherstation.service.WSMeasurementService;
import rmhub.mod.weatherstation.service.WeatherAlertService;
import rmhub.model.mivisu.ssil.AlertResponse;
import rmhub.model.mivisu.ssil.AlertWeather;
import rmhub.model.mivisu.ssil.Body;
import rmhub.model.mivisu.ssil.Centrale;
import rmhub.model.mivisu.ssil.CentraleResponse;
import rmhub.model.mivisu.ssil.Mesure;
import rmhub.model.mivisu.ssil.MivisuXml;

@Service
@Slf4j
class GenerateWeatherAlertImpl implements GenerateWeatherAlert {

  @Autowired
  private WSMeasurementService wsMeasurementService;

  @Autowired
  private WeatherAlertService weatherAlertService;

  @Autowired
  private AlertSettingsService alertSettingsService;

  @Autowired
  WeatherProducer weatherProducer;

  /********for consumer******************/
  public void process(MivisuXml data) {

    log.info("process weather station");

    Body body = data.getBody();
    int deploymentId = data.getDeploymentId();
    Body resBody = new Body();

    if (body != null && body.getRep_Nb_Eqt() > 0) {

      List<Centrale> centrals = body.getCentrale();
      List<Centrale> centralsSelected = new ArrayList<>();

      for (int i = 0; i < centrals.size(); i++) {

        Centrale centrale = body.getCentrale().get(i);
        String externalId = centrale.getId_ext();

        Centrale itemCentraleSelected = new Centrale();
        List<Map<String, Object>> listMeasureSelected = new ArrayList<>();

        for (int j = 0; j < centrale.getMesure().size(); j++) {
          Map<String, Object> mesure = centrale.getMesure().get(j);
          WeatherMeasurement weatherMeasurement = saveMeasureData(mesure, externalId, deploymentId); // store measurement data

          // only use data of mesure have Identification for FE show on detail pop-up
          if (checkAllId(mesure)) {
            mesure.put(MivisuMessageConstant.WEATHER_MES_ID, weatherMeasurement.getId());
            listMeasureSelected.add(mesure);
            itemCentraleSelected.setMesure(listMeasureSelected);
            itemCentraleSelected.setId_ext(externalId);
          }
        }
        if (listMeasureSelected.size() > 0) {
          centralsSelected.add(itemCentraleSelected);
        }
      }
      if (centralsSelected.size() > 0) {
        resBody.setCentrale(centralsSelected);
        generateAlert(resBody, deploymentId);
      }
    }
  }

  private WeatherMeasurement saveMeasureData(Map<String, Object> mesure, String externalId, Integer deploymentId) {

    String eqtDtMes = mesure.getOrDefault(MivisuMessageConstant.EQT_DT_MES, MivisuMessageConstant.DEFAULT_STRING_VALUE).toString();
    int eqtMesPer = ParserHelper
        .convertString2Int(mesure.getOrDefault(MivisuMessageConstant.EQT_MES_PER, MivisuMessageConstant.DEFAULT_INT_VALUE).toString());
    int eqtMesLgId = ParserHelper
        .convertString2Int(
            mesure.getOrDefault(MivisuMessageConstant.EQT_MES_LG_ID, MivisuMessageConstant.DEFAULT_INT_VALUE).toString());
    String eqtMesId = mesure.getOrDefault(MivisuMessageConstant.EQT_MES_ID, MivisuMessageConstant.DEFAULT_STRING_VALUE).toString();
    int eqtMesLgType = ParserHelper
        .convertString2Int(
            mesure.getOrDefault(MivisuMessageConstant.EQT_MES_LG_TYPE, MivisuMessageConstant.DEFAULT_INT_VALUE).toString());
    String eqtMesType = mesure.getOrDefault(MivisuMessageConstant.EQT_MES_TYPE, MivisuMessageConstant.DEFAULT_STRING_VALUE).toString();
    int eqtMesNbVal = ParserHelper
        .convertString2Int(
            mesure.getOrDefault(MivisuMessageConstant.EQT_MES_NB_VAL, MivisuMessageConstant.DEFAULT_INT_VALUE).toString());

    WeatherMeasurement weatherMeasure = WeatherMeasurement.builder()
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

      WeatherMeasurementDetail weatherMeasurementDetail = WeatherMeasurementDetail.builder()
          .eqtMesVal(eqtMesVal)
          .eqtMesKlif(eqtMesKlif)
          .index(idx)
          .build();

      weatherMeasurementDetail.setWeatherMeasurement(weatherMeasure);
      weatherMeasure.getWeatherMeasurementDetails().add(weatherMeasurementDetail);
    }

    return wsMeasurementService.create(weatherMeasure);
  }

  private boolean checkAllId(Map<String, Object> mesure) {

    return isSelectedDataMeasure(mesure, Identification.SURFACE_TEMPERATURE)
        || isSelectedDataMeasure(mesure, Identification.SURFACE_STATUS)
        || isSelectedDataMeasure(mesure, Identification.FREEZING_TEMPERATURE)
        || isSelectedDataMeasure(mesure, Identification.WATER_FILM_HEIGHT)
        || isSelectedDataMeasure(mesure, Identification.AIR_TEMPERATURE)
        || isSelectedDataMeasure(mesure, Identification.AIR_HUMIDITY)
        || isSelectedDataMeasure(mesure, Identification.DEW_POINT_TEMPERATURE)
        || isSelectedDataMeasure(mesure, Identification.PRECIPITATION_HEIGHT)
        || isSelectedDataMeasure(mesure, Identification.WIND_SPEED_XM)
        || isSelectedDataMeasure(mesure, Identification.WIND_SPEED_AM)
        || isSelectedDataMeasure(mesure, Identification.TYPE_OF_PRECIPITATION)
        || isSelectedDataMeasure(mesure, Identification.INTENSITY_OF_PRECIPITATION)
        || isSelectedDataMeasure(mesure, Identification.WIND_DIRECTION)
        || isSelectedDataMeasure(mesure, Identification.ATMOSPHERIC_PRESSURE)
        || isSelectedDataMeasure(mesure, Identification.WINTER_CONDITIONS_WARNING)
        || isSelectedDataMeasure(mesure, Identification.WINTER_PRECIPITATIONS_WARNING)
        || isSelectedDataMeasure(mesure, Identification.COMMUNICATION)
        || isSelectedDataMeasure(mesure, Identification.GATE_OPEN)
        || isSelectedDataMeasure(mesure, Identification.POWER_DEFAULT);
  }

  private boolean isSelectedDataMeasure(Map<String, Object> mesure, String idValue) {
    String eqtMesId = mesure.getOrDefault(MivisuMessageConstant.EQT_MES_ID, MivisuMessageConstant.DEFAULT_STRING_VALUE).toString();
    return eqtMesId != null && eqtMesId.equals(idValue);
  }

  /*****************for producer****************/

  public void generateAlert(Body body, int deploymentId) {
    List<CentraleResponse> centralListResponse = new ArrayList<>();
    List<Mesure> measureListResponse;

    List<Centrale> central = body.getCentrale();

    for (Centrale cen : central) {
      String idExt = cen.getId_ext();
      int eqtNbMesure = cen.getMesure().size();
      measureListResponse = new ArrayList<>();
      for (int j = 0; j < eqtNbMesure; j++) {
        Map<String, Object> mesureItem = cen.getMesure().get(j);
        String measureId = mesureItem.getOrDefault(MivisuMessageConstant.EQT_MES_ID, MivisuMessageConstant.DEFAULT_STRING_VALUE).toString();
        if (measureId != null && measureId.equals(Identification.SURFACE_STATUS)) { // surface status
          Mesure measureSurface = setAlertSurface(mesureItem, deploymentId);
          measureListResponse.add(measureSurface);
          saveWeatherAlert((long) deploymentId, idExt, mesureItem);
        } else if (measureId != null && measureId.equals(Identification.WIND_SPEED_XM)) { // wind speed
          Mesure mesureWind = setAlertWind(mesureItem, deploymentId);
          measureListResponse.add(mesureWind);
          saveWeatherAlert((long) deploymentId, idExt, mesureItem);
        } else {
          Mesure mesureNomal = getMeasureValue(mesureItem);
          measureListResponse.add(mesureNomal);
        }
      }
      CentraleResponse centraleResponse = new CentraleResponse(measureListResponse, measureListResponse.size(), idExt);
      centralListResponse.add(centraleResponse);
    }
    AlertResponse alertResponse = AlertResponse.builder()
        .deploymentId(deploymentId)
        .size(centralListResponse.size())
        .centraleResponses(centralListResponse).build();

    weatherProducer.sendAlertToKafka(alertResponse);
  }

  private Mesure setAlertSurface(Map<String, Object> mesureItem, int deploymentId) {
    List<WeatherAlertRule> listAlertSurface = alertSettingsService.findByAlertCode(AlertRuleCode.ALR_SURFACE_SEARCH, deploymentId);
    int eqtMesVal = ParserHelper
        .convertString2Int(
            mesureItem.getOrDefault(MivisuMessageConstant.EQT_MES_VAL_1, MivisuMessageConstant.DEFAULT_INT_VALUE).toString());
    Mesure measureResponse = getMeasureValue(mesureItem);
    for (WeatherAlertRule item : listAlertSurface) {
      if (item.getValue() != null && eqtMesVal == item.getValue()) {
        AlertWeather alertWeather = new AlertWeather(item.getColor(), item.getContent(), item.getAlertCode());
        measureResponse.setAlertBase(alertWeather);
        return measureResponse;
      }
    }

    AlertWeather alertWeather = new AlertWeather();
    measureResponse.setAlertBase(alertWeather);

    return measureResponse;
  }

  private Mesure setAlertWind(Map<String, Object> mesureItem, int deploymentId) {
    List<WeatherAlertRule> listAlertWind = alertSettingsService.findByAlertCode(AlertRuleCode.ALR_WIND_SEARCH, deploymentId);
    listAlertWind.sort((Comparator.comparing(WeatherAlertRule::getValue)));
    int eqtMesVal = ParserHelper
        .convertString2Int(
            mesureItem.getOrDefault(MivisuMessageConstant.EQT_MES_VAL_1, MivisuMessageConstant.DEFAULT_INT_VALUE).toString());

    AlertWeather alertWeather = new AlertWeather();
    Integer condition;
    Integer alertRuleValue;
    WeatherAlertRule alertRule;
    if (!listAlertWind.isEmpty()) {
      alertRule = listAlertWind.get(0);
      condition = alertRule.getCondition();
      alertRuleValue = alertRule.getValue();
      if (alertRuleValue != null) {
        if ((eqtMesVal >= alertRuleValue && AlertRuleOperation.GREATER.equals(condition)) ||
            (eqtMesVal == alertRuleValue && AlertRuleOperation.EQUALS.equals(condition)) ||
            (eqtMesVal <= alertRuleValue && AlertRuleOperation.SMALLER.equals(condition))) {
          alertWeather = new AlertWeather(alertRule.getColor(), alertRule.getContent(), alertRule.getAlertCode());
        }
      }
      if (listAlertWind.size() == 2) {
        alertRule = listAlertWind.get(1);
        condition = alertRule.getCondition();
        alertRuleValue = alertRule.getValue();
        if (alertRuleValue != null) {
          if ((eqtMesVal >= alertRuleValue && AlertRuleOperation.GREATER.equals(condition)) ||
              (eqtMesVal <= alertRuleValue && AlertRuleOperation.SMALLER.equals(condition)) ||
              (eqtMesVal == alertRuleValue && AlertRuleOperation.EQUALS.equals(condition))) {
            alertWeather = new AlertWeather(alertRule.getColor(), alertRule.getContent(), alertRule.getAlertCode());
          }
        }
      }
    }

    Mesure mesureResponse = getMeasureValue(mesureItem);
    mesureResponse.setAlertBase(alertWeather);
    return mesureResponse;
  }


  private Mesure getMeasureValue(Map<String, Object> mesureItem) {

    int num = ParserHelper
        .convertString2Int(mesureItem.getOrDefault(MivisuMessageConstant.NUM, MivisuMessageConstant.DEFAULT_INT_VALUE).toString());
    String eqtDtMes = mesureItem.getOrDefault(MivisuMessageConstant.EQT_DT_MES, MivisuMessageConstant.DEFAULT_STRING_VALUE).toString();
    int eqtMesPer = ParserHelper
        .convertString2Int(
            mesureItem.getOrDefault(MivisuMessageConstant.EQT_MES_PER, MivisuMessageConstant.DEFAULT_INT_VALUE).toString());
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
        eqtMesKlif, new AlertWeather());
  }

  private void saveWeatherAlert(long deploymentId, String idExt, Map<String, Object> mesureItem) {
    int weatherMeasurementId = ParserHelper
        .convertString2Int(
            mesureItem.getOrDefault(MivisuMessageConstant.WEATHER_MES_ID, MivisuMessageConstant.DEFAULT_INT_VALUE).toString());
    WeatherAlert weatherAlert = WeatherAlert.builder()
        .weatherMeasurementId((long) weatherMeasurementId)
        .deploymentId(deploymentId)
        .externalId(idExt)
        .isHandled(false)
        .build();
    weatherAlertService.create(weatherAlert);
  }
}
