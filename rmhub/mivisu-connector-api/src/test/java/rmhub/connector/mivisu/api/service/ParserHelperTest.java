package rmhub.connector.mivisu.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.util.List;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import rmhub.common.exception.RmhubException;
import rmhub.model.mivisu.api.Cfg;
import rmhub.model.mivisu.api.Channel;
import rmhub.model.mivisu.api.ClassMesure;
import rmhub.model.mivisu.api.InfoGeneric;
import rmhub.model.mivisu.api.InfoGetIgx;
import rmhub.model.mivisu.api.Mesure;
import rmhub.model.mivisu.api.Nature;
import rmhub.model.mivisu.api.Period;
import rmhub.model.mivisu.api.Pool;

class ParserHelperTest {

  private ParserHelper parserHelper = new ParserHelper();

  @Test
  void buildNodeInfoGetIgx_passerOk() throws IOException {
    String json = IOUtils.toString(
        getClass().getResourceAsStream("/response_api_connect_2.json"),
        StandardCharsets.UTF_8
    );

    InfoGetIgx infoResult = ParserHelper.buildNodeInfoGetIgx(json);
    assertNotNull(infoResult);

    List<InfoGeneric> lsInfoGenericResult = infoResult.getInfoGenerics();
    assertNotNull(lsInfoGenericResult);

    InfoGeneric infoGenericResult = lsInfoGenericResult.get(0);
    assertEquals("Traffic counting PR 44+400 (Calcul) (TC_44_400)", infoGenericResult.getTitre());
    assertEquals("mivisu", infoGenericResult.getSource().getValue());
  }

  @Test
  void buildNodeInfoGetIgx_InfoGenericNotArray() {
    String json = "{\n"
        + "  \"INFOS\": {\n"
        + "    \"CRVAL\": \"1\",\n"
        + "    \"CRTXT\": {\n"
        + "      \"info_get_igx\": {\n"
        + "        \"np_mivisu_CXTMVS_MES_STA\": \"190828105008\",\n"
        + "        \"arg_nom_1\": \"/SOURCE\",\n"
        + "        \"arg_val_1\": \"mivisu\",\n"
        + "        \"arg_nom_2\": \"/CONTEXTE\",\n"
        + "        \"arg_val_2\": \"CXTMVS_MES_STA\",\n"
        + "        \"InfoGeneric\": {}\n"
        + "      }\n"
        + "    }\n"
        + "  }\n"
        + "}";

    InfoGetIgx infoResult = ParserHelper.buildNodeInfoGetIgx(json);
    assertNotNull(infoResult);

    List<InfoGeneric> lsInfoGenericResult = infoResult.getInfoGenerics();
    assertNotNull(lsInfoGenericResult);
  }

  @Test
  void buildNodeInfoGetIgx_getRootEx() {
    String json = "{\n"
        + "  \"INFOS\": {\n"
        + "    \"CRVAL\": \"1\",\n"
        + "    \"CRTXT\": {}\n"
        + "  }\n"
        + "";

    assertThrows(RmhubException.class, () -> ParserHelper.buildNodeInfoGetIgx(json));
  }

  @Test
  void buildNodeInfoGetIgx_getRootNull() {
    String json = "{\n"
        + "  \"INFOS\": {\n"
        + "    \"CRVAL\": \"0\",\n"
        + "    \"CRTXT\": {}\n"
        + "  }\n"
        + "}";
    InfoGetIgx infoResult = ParserHelper.buildNodeInfoGetIgx(json);

    assertNull(infoResult);
  }

  @Test
  void buildNodeInfoGeneric_passerOk() throws IOException {
    String json = IOUtils.toString(
        getClass().getResourceAsStream("/JsonNodeInfoGeneric.json"),
        StandardCharsets.UTF_8
    );

    ObjectMapper mapper = new ObjectMapper();
    JsonNode actualObj = mapper.readTree(json);
    InfoGeneric infoResult = ReflectionTestUtils.invokeMethod(parserHelper, "buildNodeInfoGeneric", actualObj);

    assertNotNull(infoResult);
    assertEquals("Traffic counting PR 44+400 (Calcul) (TC_44_400)", infoResult.getTitre());
    assertEquals("mivisu", infoResult.getSource().getValue());
  }

  @Test
  void buildNodeInfoGeneric_NonId() throws IOException {
    String json = IOUtils.toString(
        getClass().getResourceAsStream("/JsonNodeInfoGenericNonId.json"),
        StandardCharsets.UTF_8
    );

    ObjectMapper mapper = new ObjectMapper();
    JsonNode actualObj = mapper.readTree(json);
    InfoGeneric infoResult = ReflectionTestUtils.invokeMethod(parserHelper, "buildNodeInfoGeneric", actualObj);

    assertNotNull(infoResult);
    assertNull(infoResult.getTitre());
  }

  @Test
  void buildNodeCfg_passerOk() throws IOException {
    String json = IOUtils.toString(
        getClass().getResourceAsStream("/JsonNodeCfg.json"),
        StandardCharsets.UTF_8
    );

    ObjectMapper mapper = new ObjectMapper();
    JsonNode actualObj = mapper.readTree(json);
    Cfg cfgResult = ReflectionTestUtils.invokeMethod(parserHelper, "builNodeCfg", actualObj);

    assertNotNull(cfgResult);
    assertEquals("Traffic counting 44+400 Km", cfgResult.getDescription());
    assertEquals("TC_44_400", cfgResult.getId_interne());
  }

  @Test
  void getValueNodeCfg_passerOk() throws IOException {
    String json = "{\"_text\":\"TC_44_400\"}";
    ObjectMapper mapper = new ObjectMapper();
    JsonNode actualObj = mapper.readTree(json);
    String cfgResult = ReflectionTestUtils.invokeMethod(parserHelper, "getValueNodeCfg", actualObj);

    assertNotNull(cfgResult);
    assertEquals("TC_44_400", cfgResult);
  }

  @Test
  void getValueNodeCfg_nothing() {
    JsonNode actualObj = mock(JsonNode.class);
    when(actualObj.getNodeType()).thenReturn(JsonNodeType.NULL);
    String cfgResult = ReflectionTestUtils.invokeMethod(parserHelper, "getValueNodeCfg", actualObj);

    assertNull(cfgResult);
  }

  @Test
  void buildNodeMesure_passerOk() throws IOException {
    String json = IOUtils.toString(
        getClass().getResourceAsStream("/JsonNodeMesure.json"),
        StandardCharsets.UTF_8
    );

    ObjectMapper mapper = new ObjectMapper();
    JsonNode actualObj = mapper.readTree(json);

    Mesure mesureResult = ReflectionTestUtils.invokeMethod(parserHelper, "buildNodeMesure", actualObj);
    assertNotNull(mesureResult);

    List<Pool> lsPool = mesureResult.getPools();
    assertNotNull(lsPool);

    assertEquals("Direction_1_WE", lsPool.get(0).get_text());
    assertEquals("Direction_2_EW", lsPool.get(1).get_text());
  }

  @Test
  void buildNodeChannel_passerOk() throws IOException {
    String json = IOUtils.toString(
        getClass().getResourceAsStream("/JsonNodeChannel.json"),
        StandardCharsets.UTF_8
    );

    ObjectMapper mapper = new ObjectMapper();
    JsonNode actualObj = mapper.readTree(json);

    Channel channelResult = ReflectionTestUtils.invokeMethod(parserHelper, "buildNodeChannel", actualObj, "_0");
    assertNotNull(channelResult);
    assertEquals("Right Lane Direction 1", channelResult.get_text());

    List<Nature> lsNature = channelResult.getNatures();
    assertNotNull(lsNature);
    assertEquals("QT", lsNature.get(0).getValue());
    assertEquals("TT", lsNature.get(1).getValue());
  }

  @Test
  void buildNodeNature_passerOk() throws IOException {
    String json = IOUtils.toString(
        getClass().getResourceAsStream("/JsonNodeNature.json"),
        StandardCharsets.UTF_8
    );

    ObjectMapper mapper = new ObjectMapper();
    JsonNode actualObj = mapper.readTree(json);

    Nature natureResult = ReflectionTestUtils.invokeMethod(parserHelper, "buildNodeNature", actualObj, "QT");
    assertNotNull(natureResult);
    assertEquals("QT", natureResult.getValue());

    List<Period> lsPeriod = natureResult.getPeriods();
    assertNotNull(lsPeriod);
    assertEquals("_60", lsPeriod.get(0).getValue());
    assertEquals("_360", lsPeriod.get(1).getValue());
  }

  @Test
  void buildNodePeriod_passerOk() throws IOException {

    String json = "{\"_0000\":{\"id_mesure\":{\"_text\":\"1/0/QT/60/0000\"},\"type\":{\"_text\":\"0QT_SQ_E0\"},\"libelle\":{\"_text\":\"Összes gépjármuadat 1 perces\"}}}";
    ObjectMapper mapper = new ObjectMapper();
    JsonNode actualObj = mapper.readTree(json);
    Period periodResult = ReflectionTestUtils.invokeMethod(parserHelper, "buildNodePeriod", actualObj, "_0000");
    assertNotNull(periodResult);
    assertEquals("_0000", periodResult.getValue());

    ClassMesure classMesure = periodResult.getClassMesure();
    assertNotNull(classMesure);
    assertEquals("1/0/QT/60/0000", classMesure.getId_mesure().get_text());
  }

  @Test
  void buildNodeClassMesure_nothing() throws IOException {
    String json = "{\n"
        + "  \"_0000\": {}\n"
        + "}";
    ObjectMapper mapper = new ObjectMapper();
    JsonNode actualObj = mapper.readTree(json);
    ClassMesure measureResuult = ReflectionTestUtils.invokeMethod(parserHelper, "buildNodeClassMesure", actualObj, "_0000");

    assertNotNull(measureResuult);
    assertNull(measureResuult.getId_mesure());
  }

  @Test
  void buildNodeClassMesure_invalidKey() throws IOException {
    String json = "{\"_0000\":{\"id_mesure\":{\"_text\":\"1/0/QT/60/0000\"},\"type\":{\"_text\":\"0QT_SQ_E0\"},\"libelle_invalid\":{\"_text\":\"Összes gépjármuadat 1 perces\"}}}";
    ObjectMapper mapper = new ObjectMapper();
    JsonNode actualObj = mapper.readTree(json);
    ClassMesure measureResuult = ReflectionTestUtils.invokeMethod(parserHelper, "buildNodeClassMesure", actualObj, "_0000");

    assertNotNull(measureResuult);
    assertNull(measureResuult.getMesureLibelle());
  }

  @Test
  void createInfoBase_error() {
    assertThrows(RmhubException.class,
        () -> ReflectionTestUtils.invokeMethod(parserHelper, "createInfoBase", DateFormat.class, null));
  }

  @Test
  void classMesure_error() throws Exception {
    String json = "{\"_0000\":{\"id_mesure\":{\"_text\":\"1/0/QT/60/0000\"},\"type\":{\"_text\":\"0QT_SQ_E0\"},\"libelle\":{\"_text\":\"Összes gépjármuadat 1 perces\"}}}";
    ObjectMapper mapper = new ObjectMapper();
    JsonNode actualObj = mapper.readTree(json);
    Map.Entry<String, JsonNode> jsonNode = mock(Map.Entry.class);
    doThrow(new RuntimeException()).when(jsonNode).getValue();
    ReflectionTestUtils
        .invokeMethod(parserHelper, "buildNodePeriod", actualObj, "_0000");
  }
}
