package rmhub.mod.trafficlogger.behaviour;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import rmhub.common.constant.MivisuMessageConstant;
import rmhub.mod.trafficlogger.dto.response.TrafficAlertSettingDto;
import rmhub.mod.trafficlogger.entity.TrafficAlert;
import rmhub.mod.trafficlogger.entity.TrafficMeasurement;
import rmhub.mod.trafficlogger.producer.TrafficProducer;
import rmhub.mod.trafficlogger.service.TrafficAlertService;
import rmhub.mod.trafficlogger.service.TrafficAlertSettingService;
import rmhub.mod.trafficlogger.service.TrafficMeasurementService;
import rmhub.model.mivisu.ssil.AlertResponse;
import rmhub.model.mivisu.ssil.Body;
import rmhub.model.mivisu.ssil.Centrale;
import rmhub.model.mivisu.ssil.MivisuXml;
import rmhub.model.mivisu.ssil.SSILMessageHeader;

@ExtendWith(SpringExtension.class)
class GenerateTrafficAlertImplTest {

  private static final String FAKE_PERIOD = "60";

  /**
   * related to {@link #FAKE_PERIOD}
   */
  private static final String FAKE_EQT_MES_PER = "60";

  private static final String FAKE_STAR = "/*/";

  /**
   * related to {@link #FAKE_STAR} and {@link #FAKE_ALR}
   */
  private static final String DUMMY_EQT_MES_ID = "test/*/test";

  private static final String FAKE_ALR = "ALR";

  private static final String FAKE_VT_TYPE = "/*/VT/60/";

  @InjectMocks
  private GenerateTrafficAlertImpl generator;

  @Mock
  private TrafficMeasurementService trafficMeasurementService;

  @Mock
  private TrafficAlertService trafficAlertService;

  @Mock
  private TrafficAlertSettingService trafficLoggerAlertService;

  @Mock
  private TrafficProducer trafficProducer;

  private ModelMapper modelMapper = new ModelMapper();

  @BeforeEach
  void setUp() {
    ReflectionTestUtils.setField(generator, "period", FAKE_PERIOD);
    ReflectionTestUtils.setField(generator, "star", FAKE_STAR);
    ReflectionTestUtils.setField(generator, "alr", FAKE_ALR);
    ReflectionTestUtils.setField(generator, "vtType", FAKE_VT_TYPE);
  }

  private final TrafficMeasurement fakeTrafficMeasurement = TrafficMeasurement.builder().id(1L)
      .eqtMesNbVal(1)
      .build();

  private final TrafficAlert fakeTrafficAlert = TrafficAlert.builder()
      .trafficMeasurementId(1L)
      .deploymentId(1)
      .build();

  @Test
  void processData_notGenerateAlert() {

    // given MESURE
    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_DT_MES, "test");

    // given data from Mivisu
    var mivisuXml = fakeData(mesureMap);

    mockCreateTrafficMeasurement();

    // then
    generator.processData(mivisuXml);

    // verify
    verifyMockCreateMeasurement();
  }

  @Test
  void processData_generateAlert_withoutVtType() {

    // given MESURE
    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_PER, FAKE_EQT_MES_PER); // assure triggering an alert
    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, DUMMY_EQT_MES_ID); // assure triggering an alert, contain star but not alr

    // given data from Mivisu
    var mivisuXml = fakeData(mesureMap);

    // given expected entity
    var expectedTrafficMeasurement = TrafficMeasurement.builder().id(1L)
        .eqtMesPer(Integer.valueOf(FAKE_EQT_MES_PER))
        .eqtMesId(DUMMY_EQT_MES_ID)
        .build();

    // assure generateAlert() return true
    when(trafficMeasurementService.create(any(TrafficMeasurement.class))).thenReturn(expectedTrafficMeasurement);

    // assure generateAlert() is called with data
    TrafficAlertSettingDto expectedDto = modelMapper.map(expectedTrafficMeasurement, TrafficAlertSettingDto.class);
    when(trafficLoggerAlertService.findAll()).thenReturn(Collections.singletonList(expectedDto));

    // then
    generator.processData(mivisuXml);

    // verify
    Mockito.verify(trafficProducer, Mockito.only()).sendAlertToKafka(any(AlertResponse.class));
  }

  @Test
  void processData_generateAlert_withVtType() {

    // given MESURE
    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_PER, FAKE_EQT_MES_PER); // assure triggering an alert
    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, FAKE_VT_TYPE); // assure triggering an alert, contain star but not alr, with vtType
    mesureMap.put(MivisuMessageConstant.EQT_MES_NB_VAL, 1);
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, "50"); // between 1 and 100

    // given data from Mivisu
    var mivisuXml = fakeData(mesureMap);

    // given expected entity
    var expectedTrafficMeasurement = TrafficMeasurement.builder().id(1L)
        .eqtMesPer(Integer.valueOf(FAKE_EQT_MES_PER))
        .eqtMesId(DUMMY_EQT_MES_ID)
        .eqtMesNbVal(1)
        .build();

    // assure generateAlert() return true
    when(trafficMeasurementService.create(any(TrafficMeasurement.class))).thenReturn(expectedTrafficMeasurement);

    // assure generateAlert() is called with data
    var expectedDto = TrafficAlertSettingDto.builder()
        .min(1)
        .max(100)
        .build();

    when(trafficLoggerAlertService.findAll()).thenReturn(Collections.singletonList(expectedDto));

    var fakeTrafficAlert = TrafficAlert.builder()
        .trafficMeasurementId(1L)
        .deploymentId(1)
        .build();

    when(trafficAlertService.create(any(TrafficAlert.class))).thenReturn(fakeTrafficAlert);

    mockSaveTrafficAlert();

    // then
    generator.processData(mivisuXml);

    // verify
    Mockito.verify(trafficProducer, Mockito.only()).sendAlertToKafka(any(AlertResponse.class));
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
    generator.processData(mivisuXml);

    // verify
    Mockito.verify(trafficMeasurementService, never()).create(any(TrafficMeasurement.class));
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
    generator.processData(mivisuXml);

    // verify
    Mockito.verify(trafficMeasurementService, never()).create(any(TrafficMeasurement.class));
  }

  @Test
  void generateAlert_withAlertSmallMin() {

    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, FAKE_VT_TYPE);
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, 120);

    // given data from Mivisu
    var mivisuXml = fakeData(mesureMap);

    List<TrafficAlertSettingDto> lsTrafficAlert = new ArrayList<>();
    var alertRule1 = TrafficAlertSettingDto.builder().min(60).max(80).build();
    var alertRule2 = TrafficAlertSettingDto.builder().min(81).max(100).build();
    lsTrafficAlert.add(alertRule1);
    lsTrafficAlert.add(alertRule2);

    when(trafficLoggerAlertService.findAll()).thenReturn(lsTrafficAlert);

    ReflectionTestUtils.invokeMethod(generator, "generateAlert", mivisuXml.getBody(), 1);

    // verify
    Mockito.verify(trafficAlertService, Mockito.never()).create(any());
  }

  @Test
  void generateAlert_withAlertSmallMin2() {

    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, FAKE_VT_TYPE);
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, 50);

    // given data from Mivisu
    var mivisuXml = fakeData(mesureMap);

    List<TrafficAlertSettingDto> lsTrafficAlert = new ArrayList<>();
    TrafficAlertSettingDto alertRule1 = TrafficAlertSettingDto.builder().min(60).max(80).build();
    TrafficAlertSettingDto alertRule2 = TrafficAlertSettingDto.builder().min(81).max(100).build();
    lsTrafficAlert.add(alertRule1);
    lsTrafficAlert.add(alertRule2);

    when(trafficLoggerAlertService.findAll()).thenReturn(lsTrafficAlert);

    ReflectionTestUtils.invokeMethod(generator, "generateAlert", mivisuXml.getBody(), 1);

    // verify
    Mockito.verify(trafficAlertService, Mockito.never()).create(any());
  }

  @Test
  void generateAlert_withAlertSmallMin3() {

    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, FAKE_VT_TYPE);
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, 100);

    // given data from Mivisu
    var mivisuXml = fakeData(mesureMap);

    List<TrafficAlertSettingDto> lsTrafficAlert = new ArrayList<>();
    TrafficAlertSettingDto alertRule1 = TrafficAlertSettingDto.builder().min(60).max(80).build();
    TrafficAlertSettingDto alertRule2 = TrafficAlertSettingDto.builder().min(81).max(100).build();
    lsTrafficAlert.add(alertRule1);
    lsTrafficAlert.add(alertRule2);

    when(trafficLoggerAlertService.findAll()).thenReturn(lsTrafficAlert);

    ReflectionTestUtils.invokeMethod(generator, "generateAlert", mivisuXml.getBody(), 1);

    // verify
    Mockito.verify(trafficAlertService, Mockito.times(1)).create(any());
  }

  @Test
  void generateAlert_withAlertSmallMin4() {

    Map<String, Object> mesureMap = new HashMap<>();
    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, FAKE_VT_TYPE);
    mesureMap.put(MivisuMessageConstant.EQT_MES_VAL_1, 60);

    // given data from Mivisu
    var mivisuXml = fakeData(mesureMap);

    List<TrafficAlertSettingDto> lsTrafficAlert = new ArrayList<>();
    TrafficAlertSettingDto alertRule1 = TrafficAlertSettingDto.builder().min(60).max(80).build();
    TrafficAlertSettingDto alertRule2 = TrafficAlertSettingDto.builder().min(81).max(100).build();
    lsTrafficAlert.add(alertRule1);
    lsTrafficAlert.add(alertRule2);

    when(trafficLoggerAlertService.findAll()).thenReturn(lsTrafficAlert);

    ReflectionTestUtils.invokeMethod(generator, "generateAlert", mivisuXml.getBody(), 1);

    // verify
    Mockito.verify(trafficAlertService, Mockito.times(1)).create(any());
  }

  @Test
  void isSelectedDataMesure_falseWithSTAR_VT_TYPE() {
    Map<String, Object> mesureMap = new HashMap<>();

    mesureMap.put(MivisuMessageConstant.EQT_MES_ID, FAKE_STAR);
    Assertions.assertEquals(false, ReflectionTestUtils.invokeMethod(generator, "isSelectedDataMesure", mesureMap));

    mesureMap.put(MivisuMessageConstant.EQT_MES_PER, FAKE_VT_TYPE);
    Assertions.assertEquals(false, ReflectionTestUtils.invokeMethod(generator, "isSelectedDataMesure", mesureMap));
  }

  @Test
  void isSelectedDataMesure_falseWithVT_TYPE_STAR() {
    Map<String, Object> mesureMap2 = new HashMap<>();

    mesureMap2.put(MivisuMessageConstant.EQT_MES_ID, FAKE_VT_TYPE);
    Assertions.assertEquals(false, ReflectionTestUtils.invokeMethod(generator, "isSelectedDataMesure", mesureMap2));

    mesureMap2.put(MivisuMessageConstant.EQT_MES_PER, FAKE_STAR);
    Assertions.assertEquals(false, ReflectionTestUtils.invokeMethod(generator, "isSelectedDataMesure", mesureMap2));
  }

  @Test
  void isSelectedDataMesure_falseWith_ALR_PERIOD() {
    Map<String, Object> mesureMap3 = new HashMap<>();

    mesureMap3.put(MivisuMessageConstant.EQT_MES_ID, FAKE_ALR);
    Assertions.assertEquals(false, ReflectionTestUtils.invokeMethod(generator, "isSelectedDataMesure", mesureMap3));

    mesureMap3.put(MivisuMessageConstant.EQT_MES_PER, FAKE_PERIOD);
    Assertions.assertEquals(false, ReflectionTestUtils.invokeMethod(generator, "isSelectedDataMesure", mesureMap3));
  }

  @Test
  void isSelectedDataMesure_falseWithPERIOD_STAR() {
    Map<String, Object> mesureMap4 = new HashMap<>();
    mesureMap4.put(MivisuMessageConstant.EQT_MES_PER, FAKE_PERIOD);
    Assertions.assertEquals(false, ReflectionTestUtils.invokeMethod(generator, "isSelectedDataMesure", mesureMap4));

    mesureMap4.put(MivisuMessageConstant.EQT_MES_ID, FAKE_STAR);
    Assertions.assertEquals(true, ReflectionTestUtils.invokeMethod(generator, "isSelectedDataMesure", mesureMap4));
  }

  @Test
  void isSelectedDataMesure_falseWithPERIOD_STAR_ALR() {
    Map<String, Object> mesureMap5 = new HashMap<>();

    mesureMap5.put(MivisuMessageConstant.EQT_MES_PER, FAKE_PERIOD);
    mesureMap5.put(MivisuMessageConstant.EQT_MES_ID, FAKE_STAR + "/" + FAKE_ALR);
    Assertions.assertEquals(false, ReflectionTestUtils.invokeMethod(generator, "isSelectedDataMesure", mesureMap5));
  }

  private void mockCreateTrafficMeasurement() {
    when(trafficMeasurementService.create(any(TrafficMeasurement.class))).thenReturn(fakeTrafficMeasurement);
  }

  private void verifyMockCreateMeasurement() {
    Mockito.verify(trafficMeasurementService, Mockito.times(1)).create(any(TrafficMeasurement.class));
  }

  private void mockSaveTrafficAlert() {
    when(trafficAlertService.create(any(TrafficAlert.class))).thenReturn(fakeTrafficAlert);
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
