package rmhub.mod.weatherstation.behaviour;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import rmhub.common.constant.MivisuMessageConstant;
import rmhub.mod.weatherstation.constant.Identification;
import rmhub.mod.weatherstation.entity.WeatherAlert;
import rmhub.mod.weatherstation.entity.WeatherAlertRule;
import rmhub.mod.weatherstation.entity.WeatherMeasurement;
import rmhub.mod.weatherstation.producer.WeatherProducer;
import rmhub.mod.weatherstation.service.AlertSettingsService;
import rmhub.mod.weatherstation.service.WSMeasurementService;
import rmhub.mod.weatherstation.service.WeatherAlertService;
import rmhub.model.mivisu.ssil.AlertResponse;
import rmhub.model.mivisu.ssil.Body;
import rmhub.model.mivisu.ssil.Centrale;
import rmhub.model.mivisu.ssil.Mesure;
import rmhub.model.mivisu.ssil.MivisuXml;
import rmhub.model.mivisu.ssil.SSILMessageHeader;

@ExtendWith(SpringExtension.class)
class GenerateWeatherAlertImplTest {

  private static final int FAKE_WIND_ALERT_VALUE = 50;

  private static final int FAKE_WIND_ALERT_LOWER = 1;

  private static final int FAKE_WIND_ALERT_UPPER = 100;

  private static final int FAKE_WIND_ALERT_BEYOND = 101;

  private static final int FAKE_SURFACE_ALERT_VALUE = 50;

  @InjectMocks
  private GenerateWeatherAlertImpl generator;

  @Mock
  private WSMeasurementService measurementService;

  @Mock
  private WeatherAlertService alertService;

  @Mock
  private AlertSettingsService alertSettingsService;

  @Mock
  private WeatherProducer producer;

  private final WeatherMeasurement fakeWeatherMeasurement = WeatherMeasurement.builder().id(1L)
      .eqtDtMes("test")
      .eqtMesPer(1)
      .eqtMesLgId(1)
      .eqtMesId("test")
      .eqtMesLgType(1)
      .eqtMesType("test")
      .eqtMesNbVal(1)
      .build();

  private final WeatherAlert fakeWeatherAlert = WeatherAlert.builder()
      .weatherMeasurementId(1L)
      .deploymentId(1L)
      .externalId("test")
      .isHandled(false)
      .build();

  @Test
  void processData_notGenerateAlert() {

    // given MESURE
    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_DT_MES, "test");

    // given data from Mivisu
    var mivisuXml = fakeData(mesureMap);

    mockCreateWeatherMeasurement();

    // then
    generator.process(mivisuXml);

    // verify
    verifyMockCreateMeasurement();
  }

  @Test
  void processData_notBody() {

    // given MESURE
    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_DT_MES, "test");

    // given data from Mivisu
    var mivisuXml = fakeData(mesureMap);
    mivisuXml.setBody(null);

    // then
    generator.process(mivisuXml);

    // verify
    Mockito.verify(measurementService, never()).create(any(WeatherMeasurement.class));
  }

  @Test
  void processData_notRep_Nb_Eqt() {

    // given MESURE
    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_DT_MES, "test");

    // given data from Mivisu
    var mivisuXml = fakeData(mesureMap);
    mivisuXml.getBody().setRep_Nb_Eqt(0);

    // then
    generator.process(mivisuXml);

    // verify
    Mockito.verify(measurementService, never()).create(any(WeatherMeasurement.class));
  }

  @Test
  void processData_generateAlert_surface_noAlertRule() {

    // given MESURE
    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, Identification.SURFACE_STATUS); // assure triggering surface alert
    mesureMap.put(MivisuMessageConstant.EQT_MES_NB_VAL, 1);
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, FAKE_SURFACE_ALERT_VALUE); // for coverage

    // given data from Mivisu
    var mivisuXml = fakeData(mesureMap);

    // given expected entity
    var expectedWeatherMeasurement = WeatherMeasurement.builder().id(1L)
        .eqtMesId(Identification.SURFACE_STATUS)
        .eqtMesNbVal(1)
        .build();

    // assure generateAlert() return true
    when(measurementService.create(any(WeatherMeasurement.class))).thenReturn(expectedWeatherMeasurement);

    // mock setAlertSurface
    when(alertSettingsService.findByAlertCode(anyString(), anyInt())).thenReturn(Collections.emptyList());

    // then
    generator.process(mivisuXml);

    // verify
    Mockito.verify(producer, Mockito.only()).sendAlertToKafka(any(AlertResponse.class));
  }

  @Test
  void processData_generateAlert_surface_withAlertRule() {

    // given MESURE
    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, Identification.SURFACE_STATUS); // assure triggering surface alert
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, FAKE_SURFACE_ALERT_VALUE); // for coverage

    // given data from Mivisu
    var mivisuXml = fakeData(mesureMap);

    // given expected entity
    var expectedWeatherMeasurement = WeatherMeasurement.builder().id(1L)
        .eqtMesId(Identification.SURFACE_STATUS)
        .build();

    // assure generateAlert() return true
    when(measurementService.create(any(WeatherMeasurement.class))).thenReturn(expectedWeatherMeasurement);

    // mock setAlertSurface
    var rule = WeatherAlertRule.builder().value(FAKE_SURFACE_ALERT_VALUE).build(); // equal EQT_MES_VAL_1
    when(alertSettingsService.findByAlertCode(anyString(), anyInt())).thenReturn(Collections.singletonList(rule));

    // then
    generator.process(mivisuXml);

    // verify
    Mockito.verify(producer, Mockito.only()).sendAlertToKafka(any(AlertResponse.class));
  }

  @Test
  void processData_generateAlert_surface_withAlertRule_forLoopCoverage() {

    // given MESURE
    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, Identification.SURFACE_STATUS); // assure triggering surface alert
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, FAKE_SURFACE_ALERT_VALUE); // for coverage

    // given data from Mivisu
    var mivisuXml = fakeData(mesureMap);

    // given expected entity
    var expectedWeatherMeasurement = WeatherMeasurement.builder().id(1L)
        .eqtMesId(Identification.SURFACE_STATUS)
        .build();

    // assure generateAlert() return true
    when(measurementService.create(any(WeatherMeasurement.class))).thenReturn(expectedWeatherMeasurement);

    // mock setAlertSurface
    var rule = WeatherAlertRule.builder().value(69).build(); // not equal EQT_MES_VAL_1
    when(alertSettingsService.findByAlertCode(anyString(), anyInt())).thenReturn(Collections.singletonList(rule));

    // then
    generator.process(mivisuXml);

    // verify
    Mockito.verify(producer, Mockito.only()).sendAlertToKafka(any(AlertResponse.class));
  }

  @Test
  void processData_generateAlert_wind_withAlertRule_between() {

    // given MESURE
    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, Identification.WIND_SPEED_AM); // assure triggering wind alert
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, FAKE_WIND_ALERT_VALUE); // for coverage

    // given data from Mivisu
    var mivisuXml = fakeData(mesureMap);

    // given expected entity
    var expectedWeatherMeasurement = WeatherMeasurement.builder().id(1L)
        .eqtMesId(Identification.WIND_SPEED_AM)
        .build();

    // assure generateAlert() return true
    when(measurementService.create(any(WeatherMeasurement.class))).thenReturn(expectedWeatherMeasurement);

    // mock setAlertSurface
    List<WeatherAlertRule> listAlertWind = new ArrayList<>();
    WeatherAlertRule weatherAlertRule1 = WeatherAlertRule.builder().value(FAKE_WIND_ALERT_LOWER).build();
    WeatherAlertRule weatherAlertRule2 = WeatherAlertRule.builder().value(FAKE_WIND_ALERT_UPPER).build();
    listAlertWind.add(weatherAlertRule1);
    listAlertWind.add(weatherAlertRule2);

    when(alertSettingsService.findByAlertCode(anyString(), anyInt())).thenReturn(listAlertWind);

    // then
    generator.process(mivisuXml);

    // verify
    Mockito.verify(producer, Mockito.only()).sendAlertToKafka(any(AlertResponse.class));
  }

  @Test
  void processData_generateAlert_wind_withAlertRule_beyond() {

    // given MESURE
    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, Identification.WIND_SPEED_AM); // assure triggering wind alert
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, FAKE_WIND_ALERT_BEYOND); // for coverage

    // given data from Mivisu
    var mivisuXml = fakeData(mesureMap);

    // given expected entity
    var expectedWeatherMeasurement = WeatherMeasurement.builder().id(1L)
        .eqtMesId(Identification.WIND_SPEED_AM)
        .build();

    // assure generateAlert() return true
    when(measurementService.create(any(WeatherMeasurement.class))).thenReturn(expectedWeatherMeasurement);

    // mock setAlertSurface
    List<WeatherAlertRule> listAlertWind = new ArrayList<>();
    WeatherAlertRule weatherAlertRule1 = WeatherAlertRule.builder().value(FAKE_WIND_ALERT_LOWER).build();
    WeatherAlertRule weatherAlertRule2 = WeatherAlertRule.builder().value(FAKE_WIND_ALERT_UPPER).build();
    listAlertWind.add(weatherAlertRule1);
    listAlertWind.add(weatherAlertRule2);

    when(alertSettingsService.findByAlertCode(anyString(), anyInt())).thenReturn(listAlertWind);

    // then
    generator.process(mivisuXml);

    // verify
    Mockito.verify(producer, Mockito.only()).sendAlertToKafka(any(AlertResponse.class));
  }

  @Test
  void processData_generateAlert_notSurfaceNorWind() {

    // given MESURE
    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, Identification.POWER_DEFAULT); // assure triggering other alert

    // given data from Mivisu
    var mivisuXml = fakeData(mesureMap);

    // given expected entity
    var expectedWeatherMeasurement = WeatherMeasurement.builder().id(1L)
        .eqtMesId(Identification.POWER_DEFAULT)
        .build();

    // assure generateAlert() return true
    when(measurementService.create(any(WeatherMeasurement.class))).thenReturn(expectedWeatherMeasurement);

    // mock setAlertSurface
    when(alertSettingsService.findByAlertCode(anyString(), anyInt())).thenReturn(Collections.emptyList());

    // mock create, but not necessary
    mockSaveTrafficAlert();

    // then
    generator.process(mivisuXml);

    // verify
    Mockito.verify(producer, Mockito.only()).sendAlertToKafka(any(AlertResponse.class));
  }

  @Test
  void checkAllId_testAll() {
    Map<String, Object> mesureMap = new HashMap<>();

    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, Identification.SURFACE_TEMPERATURE);
    Assertions.assertEquals(true, ReflectionTestUtils
        .invokeMethod(generator, "checkAllId", mesureMap));

    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, Identification.SURFACE_STATUS);
    Assertions.assertEquals(true, ReflectionTestUtils
        .invokeMethod(generator, "checkAllId", mesureMap));

    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, Identification.FREEZING_TEMPERATURE);
    Assertions.assertEquals(true, ReflectionTestUtils
        .invokeMethod(generator, "checkAllId", mesureMap));

    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, Identification.WATER_FILM_HEIGHT);
    Assertions.assertEquals(true, ReflectionTestUtils
        .invokeMethod(generator, "checkAllId", mesureMap));

    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, Identification.AIR_TEMPERATURE);
    Assertions.assertEquals(true, ReflectionTestUtils
        .invokeMethod(generator, "checkAllId", mesureMap));
    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, Identification.AIR_HUMIDITY);
    Assertions.assertEquals(true, ReflectionTestUtils
        .invokeMethod(generator, "checkAllId", mesureMap));

    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, Identification.DEW_POINT_TEMPERATURE);
    Assertions.assertEquals(true, ReflectionTestUtils
        .invokeMethod(generator, "checkAllId", mesureMap));

    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, Identification.PRECIPITATION_HEIGHT);
    Assertions.assertEquals(true, ReflectionTestUtils
        .invokeMethod(generator, "checkAllId", mesureMap));

    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, Identification.WIND_SPEED_XM);
    Assertions.assertEquals(true, ReflectionTestUtils
        .invokeMethod(generator, "checkAllId", mesureMap));

    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, Identification.WIND_SPEED_AM);
    Assertions.assertEquals(true, ReflectionTestUtils
        .invokeMethod(generator, "checkAllId", mesureMap));

    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, Identification.TYPE_OF_PRECIPITATION);
    Assertions.assertEquals(true, ReflectionTestUtils
        .invokeMethod(generator, "checkAllId", mesureMap));

    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, Identification.INTENSITY_OF_PRECIPITATION);
    Assertions.assertEquals(true, ReflectionTestUtils
        .invokeMethod(generator, "checkAllId", mesureMap));

    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, Identification.WIND_DIRECTION);
    Assertions.assertEquals(true, ReflectionTestUtils
        .invokeMethod(generator, "checkAllId", mesureMap));

    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, Identification.ATMOSPHERIC_PRESSURE);
    Assertions.assertEquals(true, ReflectionTestUtils
        .invokeMethod(generator, "checkAllId", mesureMap));

    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, Identification.WINTER_CONDITIONS_WARNING);
    Assertions.assertEquals(true, ReflectionTestUtils
        .invokeMethod(generator, "checkAllId", mesureMap));

    Assertions.assertEquals(true, ReflectionTestUtils
        .invokeMethod(generator, "checkAllId", mesureMap));
    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, Identification.WINTER_PRECIPITATIONS_WARNING);

    Assertions.assertEquals(true, ReflectionTestUtils
        .invokeMethod(generator, "checkAllId", mesureMap));
    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, Identification.COMMUNICATION);

    Assertions.assertEquals(true, ReflectionTestUtils
        .invokeMethod(generator, "checkAllId", mesureMap));

    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, Identification.GATE_OPEN);
    Assertions.assertEquals(true, ReflectionTestUtils
        .invokeMethod(generator, "checkAllId", mesureMap));

    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, Identification.POWER_DEFAULT);

    Assertions.assertEquals(true, ReflectionTestUtils
        .invokeMethod(generator, "checkAllId", mesureMap));
  }

  @Test
  void setAlertSurface_nullItem() {
    WeatherAlertRule nullAlert = WeatherAlertRule.builder().value(null).build();

    when(alertSettingsService.findByAlertCode(any(), any()))
        .thenReturn(Collections.singletonList(nullAlert));
    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, MivisuMessageConstant.DEFAULT_INT_VALUE);

    Assertions.assertNotNull(ReflectionTestUtils
        .invokeMethod(generator, "setAlertSurface", mesureMap, 1));
  }

  @Test
  void setAlertWind_nullItem() {
    WeatherAlertRule nullAlert = WeatherAlertRule.builder().value(null).build();

    when(alertSettingsService.findByAlertCode(any(), any()))
        .thenReturn(Collections.singletonList(nullAlert));
    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, MivisuMessageConstant.DEFAULT_INT_VALUE);

    Assertions.assertNotNull(ReflectionTestUtils
        .invokeMethod(generator, "setAlertWind", mesureMap, 1));
  }

  @Test
  void setAlertWind_listNotEmpty() {
    List<WeatherAlertRule> listAlertWind = new ArrayList<>();

    when(alertSettingsService.findByAlertCode(any(), any())).thenReturn(listAlertWind);
    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, MivisuMessageConstant.DEFAULT_INT_VALUE);
    Mesure mesureResult = ReflectionTestUtils
        .invokeMethod(generator, "setAlertWind", mesureMap, 1);

    Assertions.assertNull(mesureResult.getAlertBase().getColor());
  }

  @Test
  void setAlertWind_ListSize1_Condition1() {
    List<WeatherAlertRule> listAlertWind = new ArrayList<>();
    WeatherAlertRule alert1 = WeatherAlertRule.builder().value(80).condition(1).build();
    listAlertWind.add(alert1);

    when(alertSettingsService.findByAlertCode(any(), any())).thenReturn(listAlertWind);

    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, 90);
    Mesure mesureResult = ReflectionTestUtils
        .invokeMethod(generator, "setAlertWind", mesureMap, 1);

    Assertions.assertNull(mesureResult.getAlertBase().getColor());
  }

  @Test
  void setAlertWind_ListSize1_Condition2() {
    List<WeatherAlertRule> listAlertWind = new ArrayList<>();
    WeatherAlertRule alert1 = WeatherAlertRule.builder().value(80).condition(2).build();
    listAlertWind.add(alert1);

    when(alertSettingsService.findByAlertCode(any(), any())).thenReturn(listAlertWind);

    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, 70);
    Mesure mesureResult = ReflectionTestUtils
        .invokeMethod(generator, "setAlertWind", mesureMap, 1);

    Assertions.assertNull(mesureResult.getAlertBase().getColor());
  }

  @Test
  void setAlertWind_ListSize1_Condition3() {
    List<WeatherAlertRule> listAlertWind = new ArrayList<>();
    WeatherAlertRule alert1 = WeatherAlertRule.builder().value(80).condition(3).build();
    listAlertWind.add(alert1);

    when(alertSettingsService.findByAlertCode(any(), any())).thenReturn(listAlertWind);

    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, 80);
    Mesure mesureResult = ReflectionTestUtils
        .invokeMethod(generator, "setAlertWind", mesureMap, 1);

    Assertions.assertNull(mesureResult.getAlertBase().getColor());
  }

  @Test
  void setAlertWind_ListSize2_Condition1() {
    List<WeatherAlertRule> listAlertWind = new ArrayList<>();
    WeatherAlertRule alert1 = WeatherAlertRule.builder().value(80).condition(1).build();
    WeatherAlertRule alert2 = WeatherAlertRule.builder().value(120).condition(1).build();
    listAlertWind.add(alert1);
    listAlertWind.add(alert2);

    when(alertSettingsService.findByAlertCode(any(), any())).thenReturn(listAlertWind);

    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, 130);
    Mesure mesureResult = ReflectionTestUtils
        .invokeMethod(generator, "setAlertWind", mesureMap, 1);

    Assertions.assertNull(mesureResult.getAlertBase().getColor());
  }

  @Test
  void setAlertWind_ListSize2_Condition1_valEqualval1() {
    List<WeatherAlertRule> listAlertWind = new ArrayList<>();
    WeatherAlertRule alert1 = WeatherAlertRule.builder().value(80).condition(1).build();
    WeatherAlertRule alert2 = WeatherAlertRule.builder().value(120).condition(1).build();
    listAlertWind.add(alert1);
    listAlertWind.add(alert2);

    when(alertSettingsService.findByAlertCode(any(), any())).thenReturn(listAlertWind);

    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, 80);
    Mesure mesureResult = ReflectionTestUtils
        .invokeMethod(generator, "setAlertWind", mesureMap, 1);

    Assertions.assertNull(mesureResult.getAlertBase().getColor());
  }

  @Test
  void setAlertWind_ListSize2_Condition1_valSmallval1() {
    List<WeatherAlertRule> listAlertWind = new ArrayList<>();
    WeatherAlertRule alert1 = WeatherAlertRule.builder().value(80).condition(1).build();
    WeatherAlertRule alert2 = WeatherAlertRule.builder().value(120).condition(1).build();
    listAlertWind.add(alert1);
    listAlertWind.add(alert2);

    when(alertSettingsService.findByAlertCode(any(), any())).thenReturn(listAlertWind);

    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, 60);
    Mesure mesureResult = ReflectionTestUtils
        .invokeMethod(generator, "setAlertWind", mesureMap, 1);

    Assertions.assertNull(mesureResult.getAlertBase().getColor());
  }

  @Test
  void setAlertWind_ListSize2_Condition1_valGreaterval1() {
    List<WeatherAlertRule> listAlertWind = new ArrayList<>();
    WeatherAlertRule alert1 = WeatherAlertRule.builder().value(80).condition(1).build();
    WeatherAlertRule alert2 = WeatherAlertRule.builder().value(120).condition(1).build();
    listAlertWind.add(alert1);
    listAlertWind.add(alert2);

    when(alertSettingsService.findByAlertCode(any(), any())).thenReturn(listAlertWind);

    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, 90);
    Mesure mesureResult = ReflectionTestUtils
        .invokeMethod(generator, "setAlertWind", mesureMap, 1);

    Assertions.assertNull(mesureResult.getAlertBase().getColor());
  }

  @Test
  void setAlertWind_ListSize2_Condition1_valGreaterval2() {
    List<WeatherAlertRule> listAlertWind = new ArrayList<>();
    WeatherAlertRule alert1 = WeatherAlertRule.builder().value(80).condition(1).build();
    WeatherAlertRule alert2 = WeatherAlertRule.builder().value(120).condition(1).build();
    listAlertWind.add(alert1);
    listAlertWind.add(alert2);

    when(alertSettingsService.findByAlertCode(any(), any())).thenReturn(listAlertWind);

    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, 150);
    Mesure mesureResult = ReflectionTestUtils
        .invokeMethod(generator, "setAlertWind", mesureMap, 1);

    Assertions.assertNull(mesureResult.getAlertBase().getColor());
  }

  @Test
  void setAlertWind_ListSize2_Condition2() {
    List<WeatherAlertRule> listAlertWind = new ArrayList<>();
    WeatherAlertRule alert1 = WeatherAlertRule.builder().value(80).condition(2).build();
    WeatherAlertRule alert2 = WeatherAlertRule.builder().value(120).condition(2).build();
    listAlertWind.add(alert1);
    listAlertWind.add(alert2);

    when(alertSettingsService.findByAlertCode(any(), any())).thenReturn(listAlertWind);

    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, 90);
    Mesure mesureResult = ReflectionTestUtils
        .invokeMethod(generator, "setAlertWind", mesureMap, 1);

    Assertions.assertNull(mesureResult.getAlertBase().getColor());
  }

  @Test
  void setAlertWind_ListSize2_Condition2_valEqualval1() {
    List<WeatherAlertRule> listAlertWind = new ArrayList<>();
    WeatherAlertRule alert1 = WeatherAlertRule.builder().value(80).condition(2).build();
    WeatherAlertRule alert2 = WeatherAlertRule.builder().value(120).condition(2).build();
    listAlertWind.add(alert1);
    listAlertWind.add(alert2);

    when(alertSettingsService.findByAlertCode(any(), any())).thenReturn(listAlertWind);

    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, 80);
    Mesure mesureResult = ReflectionTestUtils
        .invokeMethod(generator, "setAlertWind", mesureMap, 1);

    Assertions.assertNull(mesureResult.getAlertBase().getColor());
  }

  @Test
  void setAlertWind_ListSize2_Condition2_valNull() {
    List<WeatherAlertRule> listAlertWind = new ArrayList<>();
    WeatherAlertRule alert1 = WeatherAlertRule.builder().value(null).condition(2).build();
    WeatherAlertRule alert2 = WeatherAlertRule.builder().value(null).condition(2).build();
    listAlertWind.add(alert1);
    listAlertWind.add(alert2);

    when(alertSettingsService.findByAlertCode(any(), any())).thenReturn(listAlertWind);

    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, 120);

    Assertions.assertNotNull(ReflectionTestUtils
        .invokeMethod(generator, "setAlertSurface", mesureMap, 1));

  }

  @Test
  void setAlertWind_ListSize2_Condition2_valEqualval2() {
    List<WeatherAlertRule> listAlertWind = new ArrayList<>();
    WeatherAlertRule alert1 = WeatherAlertRule.builder().value(80).condition(2).build();
    WeatherAlertRule alert2 = WeatherAlertRule.builder().value(120).condition(2).build();
    listAlertWind.add(alert1);
    listAlertWind.add(alert2);

    when(alertSettingsService.findByAlertCode(any(), any())).thenReturn(listAlertWind);

    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, 120);
    Mesure mesureResult = ReflectionTestUtils
        .invokeMethod(generator, "setAlertWind", mesureMap, 1);

    Assertions.assertNull(mesureResult.getAlertBase().getColor());
  }

  @Test
  void setAlertWind_ListSize2_Condition2_valSmallval1() {
    List<WeatherAlertRule> listAlertWind = new ArrayList<>();
    WeatherAlertRule alert1 = WeatherAlertRule.builder().value(80).condition(2).build();
    WeatherAlertRule alert2 = WeatherAlertRule.builder().value(120).condition(2).build();
    listAlertWind.add(alert1);
    listAlertWind.add(alert2);

    when(alertSettingsService.findByAlertCode(any(), any())).thenReturn(listAlertWind);

    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, 60);
    Mesure mesureResult = ReflectionTestUtils
        .invokeMethod(generator, "setAlertWind", mesureMap, 1);

    Assertions.assertNull(mesureResult.getAlertBase().getColor());
  }

  @Test
  void setAlertWind_ListSize2_Condition2_valGreaterval1() {
    List<WeatherAlertRule> listAlertWind = new ArrayList<>();
    WeatherAlertRule alert1 = WeatherAlertRule.builder().value(80).condition(2).build();
    WeatherAlertRule alert2 = WeatherAlertRule.builder().value(120).condition(2).build();
    listAlertWind.add(alert1);
    listAlertWind.add(alert2);

    when(alertSettingsService.findByAlertCode(any(), any())).thenReturn(listAlertWind);

    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, 90);
    Mesure mesureResult = ReflectionTestUtils
        .invokeMethod(generator, "setAlertWind", mesureMap, 1);

    Assertions.assertNull(mesureResult.getAlertBase().getColor());
  }

  @Test
  void setAlertWind_ListSize2_Condition2_valGreaterval2() {
    List<WeatherAlertRule> listAlertWind = new ArrayList<>();
    WeatherAlertRule alert1 = WeatherAlertRule.builder().value(80).condition(2).build();
    WeatherAlertRule alert2 = WeatherAlertRule.builder().value(120).condition(2).build();
    listAlertWind.add(alert1);
    listAlertWind.add(alert2);

    when(alertSettingsService.findByAlertCode(any(), any())).thenReturn(listAlertWind);

    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, 150);
    Mesure mesureResult = ReflectionTestUtils
        .invokeMethod(generator, "setAlertWind", mesureMap, 1);

    Assertions.assertNull(mesureResult.getAlertBase().getColor());
  }

  @Test
  void setAlertWind_ListSize2_Condition3() {
    List<WeatherAlertRule> listAlertWind = new ArrayList<>();
    WeatherAlertRule alert1 = WeatherAlertRule.builder().value(80).condition(3).build();
    WeatherAlertRule alert2 = WeatherAlertRule.builder().value(120).condition(3).build();
    listAlertWind.add(alert1);
    listAlertWind.add(alert2);

    when(alertSettingsService.findByAlertCode(any(), any())).thenReturn(listAlertWind);

    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, 120);
    Mesure mesureResult = ReflectionTestUtils
        .invokeMethod(generator, "setAlertWind", mesureMap, 1);

    Assertions.assertNull(mesureResult.getAlertBase().getColor());
  }

  @Test
  void setAlertWind_ListSize2_Condition3_valEqualval1() {
    List<WeatherAlertRule> listAlertWind = new ArrayList<>();
    WeatherAlertRule alert1 = WeatherAlertRule.builder().value(80).condition(3).build();
    WeatherAlertRule alert2 = WeatherAlertRule.builder().value(120).condition(3).build();
    listAlertWind.add(alert1);
    listAlertWind.add(alert2);

    when(alertSettingsService.findByAlertCode(any(), any())).thenReturn(listAlertWind);

    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, 80);
    Mesure mesureResult = ReflectionTestUtils
        .invokeMethod(generator, "setAlertWind", mesureMap, 1);

    Assertions.assertNull(mesureResult.getAlertBase().getColor());
  }

  @Test
  void setAlertWind_ListSize2_Condition3_valSmallval1() {
    List<WeatherAlertRule> listAlertWind = new ArrayList<>();
    WeatherAlertRule alert1 = WeatherAlertRule.builder().value(80).condition(3).build();
    WeatherAlertRule alert2 = WeatherAlertRule.builder().value(120).condition(3).build();
    listAlertWind.add(alert1);
    listAlertWind.add(alert2);

    when(alertSettingsService.findByAlertCode(any(), any())).thenReturn(listAlertWind);

    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, 60);
    Mesure mesureResult = ReflectionTestUtils
        .invokeMethod(generator, "setAlertWind", mesureMap, 1);

    Assertions.assertNull(mesureResult.getAlertBase().getColor());
  }

  @Test
  void setAlertWind_ListSize2_Condition3_valGreaterval1() {
    List<WeatherAlertRule> listAlertWind = new ArrayList<>();
    WeatherAlertRule alert1 = WeatherAlertRule.builder().value(80).condition(3).build();
    WeatherAlertRule alert2 = WeatherAlertRule.builder().value(120).condition(3).build();
    listAlertWind.add(alert1);
    listAlertWind.add(alert2);

    when(alertSettingsService.findByAlertCode(any(), any())).thenReturn(listAlertWind);

    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, 90);
    Mesure mesureResult = ReflectionTestUtils
        .invokeMethod(generator, "setAlertWind", mesureMap, 1);

    Assertions.assertNull(mesureResult.getAlertBase().getColor());
  }

  @Test
  void setAlertWind_ListSize2_Condition3_valGreaterval2() {
    List<WeatherAlertRule> listAlertWind = new ArrayList<>();
    WeatherAlertRule alert1 = WeatherAlertRule.builder().value(80).condition(3).build();
    WeatherAlertRule alert2 = WeatherAlertRule.builder().value(120).condition(3).build();
    listAlertWind.add(alert1);
    listAlertWind.add(alert2);

    when(alertSettingsService.findByAlertCode(any(), any())).thenReturn(listAlertWind);

    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, 150);
    Mesure mesureResult = ReflectionTestUtils
        .invokeMethod(generator, "setAlertWind", mesureMap, 1);

    Assertions.assertNull(mesureResult.getAlertBase().getColor());
  }

  // =======================================================================================================================================

  private void mockCreateWeatherMeasurement() {
    when(measurementService.create(any(WeatherMeasurement.class))).thenReturn(fakeWeatherMeasurement);
  }

  private void verifyMockCreateMeasurement() {
    Mockito.verify(measurementService, Mockito.times(1)).create(any(WeatherMeasurement.class));
  }

  private void mockSaveTrafficAlert() {
    when(alertService.create(any(WeatherAlert.class))).thenReturn(fakeWeatherAlert);
  }

  /**
   * fake Mivisu xml data with List of Map
   */
  private MivisuXml fakeData(List<Map<String, Object>> mesure) {
    // given CENTRALE
    Centrale centrale = new Centrale();
    centrale.setMesure(mesure);

    // given CORPS as Body
    var body = new Body();
    body.setRep_Nb_Eqt(1); // CENTRALE quantity
    body.setCentrale(Collections.singletonList(centrale));

    // given data from Mivisu
    var mivisuXml = new MivisuXml();
    mivisuXml.setDeploymentId(1);
    mivisuXml.setInfo_txt("test");
    mivisuXml.setSsilMessageHeader(new SSILMessageHeader());
    mivisuXml.setBody(body);

    return mivisuXml;
  }

  /**
   * fake Mivisu xml data with a singleton List of Map
   */
  private MivisuXml fakeData(Map<String, Object> mesure) {
    return fakeData(Collections.singletonList(mesure));
  }
}
