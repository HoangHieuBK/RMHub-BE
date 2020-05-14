package rmhub.connector.mivisu.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import rmhub.common.exception.RmhubException;
import rmhub.connector.mivisu.api.constant.MivisuApiConstant;
import rmhub.model.mivisu.api.Cfg;
import rmhub.model.mivisu.api.Channel;
import rmhub.model.mivisu.api.ClassMesure;
import rmhub.model.mivisu.api.Contexte;
import rmhub.model.mivisu.api.DateFin;
import rmhub.model.mivisu.api.DateInfo;
import rmhub.model.mivisu.api.DateUpdate;
import rmhub.model.mivisu.api.Id;
import rmhub.model.mivisu.api.InfoBase;
import rmhub.model.mivisu.api.InfoGeneric;
import rmhub.model.mivisu.api.InfoGetIgx;
import rmhub.model.mivisu.api.Libelle;
import rmhub.model.mivisu.api.Mesure;
import rmhub.model.mivisu.api.MesureId;
import rmhub.model.mivisu.api.MesureType;
import rmhub.model.mivisu.api.Nature;
import rmhub.model.mivisu.api.Period;
import rmhub.model.mivisu.api.Pool;
import rmhub.model.mivisu.api.Source;
import rmhub.model.mivisu.api.Sstype;
import rmhub.model.mivisu.api.Titre;
import rmhub.model.mivisu.api.Type;

@Slf4j
public class ParserHelper {

  public static InfoGetIgx buildNodeInfoGetIgx(String json) {

    // process JSON data
    byte[] jsonData = json.getBytes(StandardCharsets.UTF_8);

    ObjectMapper objectMapper = new ObjectMapper();
    JsonNode rootNode;

    try {
      rootNode = objectMapper.readTree(jsonData);
    } catch (IOException ex) {
      log.error("Error when passer json {} ", ex.getMessage());
      throw new RmhubException(ex.getMessage(), ex);
    }

    JsonNode nodeCrval = rootNode.findPath(MivisuApiConstant.NODE_CRVAL);
    String val = nodeCrval.asText();

    if (!MivisuApiConstant.RESULT_SUCCESS.equals(val)) {
      log.warn("Can not build Node InfoGetIgx.");
      return null;
    }

    JsonNode jsonNodeInfoGeneric = rootNode.findPath(MivisuApiConstant.NODE_INFOGENERIC);
    List<InfoGeneric> listInfoGeneric = new ArrayList<>();

    InfoGetIgx infoGetgx = new InfoGetIgx();

    if (jsonNodeInfoGeneric.isArray()) {

      for (int i = 0; i < jsonNodeInfoGeneric.size(); i++) {
        JsonNode jsonNode = jsonNodeInfoGeneric.get(i);
        InfoGeneric infoGeneric = buildNodeInfoGeneric(jsonNode);
        listInfoGeneric.add(infoGeneric);
      }
    } else {

      InfoGeneric infoGeneric = buildNodeInfoGeneric(jsonNodeInfoGeneric);
      listInfoGeneric.add(infoGeneric);
    }

    infoGetgx.setInfoGenerics(listInfoGeneric);

    return infoGetgx;
  }

  private static InfoGeneric buildNodeInfoGeneric(JsonNode jsonNode) {

    InfoGeneric infoGeneric = new InfoGeneric();
    Iterator<Map.Entry<String, JsonNode>> iterator = jsonNode.fields();

    while (iterator.hasNext()) {
      Map.Entry<String, JsonNode> map = iterator.next();
      String key = map.getKey();
      JsonNode nodeValue = map.getValue();

      switch (key) {
        case MivisuApiConstant.NODE_TITRE:
          infoGeneric.setTitre(nodeValue.asText());
          break;
        case MivisuApiConstant.NODE_DATEINFO:
          DateInfo dateInfo = createInfoBase(DateInfo.class, nodeValue);
          infoGeneric.setDateInfo(dateInfo);
          break;
        case MivisuApiConstant.NODE_SOURCE:
          Source source = createInfoBase(Source.class, nodeValue);
          infoGeneric.setSource(source);
          break;
        case MivisuApiConstant.NODE_CONTEXTE:
          Contexte contexte = createInfoBase(Contexte.class, nodeValue);
          infoGeneric.setContexte(contexte);
          break;
        case MivisuApiConstant.NODE_ID:
          Id id = createInfoBase(Id.class, nodeValue);
          infoGeneric.setId(id);
          break;
        case MivisuApiConstant.NODE_UPCASE_TITRE:
          Titre titre = createInfoBase(Titre.class, nodeValue);
          infoGeneric.setObjTitre(titre);
          break;
        case MivisuApiConstant.NODE_TYPE:
          Type type = createInfoBase(Type.class, nodeValue);
          infoGeneric.setType(type);
        case MivisuApiConstant.NODE_SSTYPE:
          Sstype sstype = createInfoBase(Sstype.class, nodeValue);
          infoGeneric.setSstype(sstype);
        case MivisuApiConstant.NODE_DATEUPDATE:
          DateUpdate dateUpdate = createInfoBase(DateUpdate.class, nodeValue);
          infoGeneric.setDateUpdate(dateUpdate);
          break;
        case MivisuApiConstant.NODE_DATEFIN:
          DateFin dateFin = createInfoBase(DateFin.class, nodeValue);
          infoGeneric.setDateFin(dateFin);
          break;
        case MivisuApiConstant.NODE_CFG:
          Cfg cfg = builNodeCfg(nodeValue);
          infoGeneric.setCfg(cfg);
          break;
        default:
          if (log.isDebugEnabled()) {
            log.debug("No valid node for building InfoGeneric: {}", key);
          }
      }
    }
    return infoGeneric;
  }

  @SuppressWarnings("deprecation")
  private static <T extends InfoBase> T createInfoBase(Class<T> cls, JsonNode nodeValue) {
    try {
      T myObject = cls.newInstance();
      Iterator<Map.Entry<String, JsonNode>> iterator = nodeValue.fields();

      while (iterator.hasNext()) {
        Map.Entry<String, JsonNode> map = iterator.next();
        JsonNode nodeValue1 = map.getValue();
        String key = map.getKey();

        if (MivisuApiConstant.NODE_TEXT.equals(key)) {
          myObject.setValue(nodeValue1.asText());
        } else {
          myObject.setType(nodeValue1.asText());
        }
      }
      return myObject;

    } catch (InstantiationException | IllegalAccessException ex) {
      log.error("Error when building NodeClassMesure: {} ", ex.getMessage());
      throw new RmhubException(ex.getMessage(), ex);
    }
  }

  private static Cfg builNodeCfg(JsonNode nodeValue) {
    Cfg cfg = new Cfg();
    Iterator<Map.Entry<String, JsonNode>> iterator = nodeValue.fields();

    while (iterator.hasNext()) {
      Map.Entry<String, JsonNode> map = iterator.next();
      String key = map.getKey();
      JsonNode node = map.getValue();
      String value = getValueNodeCfg(node);

      if (MivisuApiConstant.NODE_LOWCASE_TYPE.equals(key)) {
        cfg.setType(value);
      } else if (MivisuApiConstant.NODE_ID_INTERNE.equals(key)) {
        cfg.setId_interne(value);
      } else if (MivisuApiConstant.NODE_EQT_ACTIF.equals(key)) {
        cfg.setEqt_actif(value);
      } else if (MivisuApiConstant.NODE_DESCRIPTION.equals(key)) {
        cfg.setDescription(value);
      } else if (MivisuApiConstant.NODE_PROFIL.equals(key)) {
        cfg.setProfil(value);
      } else if (MivisuApiConstant.NODE_SOUS_TYPE.equals(key)) {
        cfg.setSous_type(value);
      } else if (MivisuApiConstant.NODE_MESURES.equals(key)) {
        Mesure mesure = buildNodeMesure(node);
        cfg.setMesure(mesure);
      }
    }
    return cfg;
  }

  private static String getValueNodeCfg(JsonNode node) {
    String value = null;
    if (!node.isNull()) {
      Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
      if (iterator.hasNext()) {
        Map.Entry<String, JsonNode> map = iterator.next();
        JsonNode nodeValue = map.getValue();
        value = nodeValue.asText();
      }
    }
    return value;
  }

  private static Mesure buildNodeMesure(JsonNode node) {

    Mesure mesure = new Mesure();
    Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
    List<Pool> pools = new ArrayList<>();

    while (iterator.hasNext()) {
      Map.Entry<String, JsonNode> map = iterator.next();
      Pool pool = new Pool();
      String key = map.getKey();
      pool.setValue(key);
      List<Channel> channels = new ArrayList<>();
      JsonNode nodeValue = map.getValue();
      Iterator<Map.Entry<String, JsonNode>> internalIterator = nodeValue.fields();

      while (internalIterator.hasNext()) {
        Map.Entry<String, JsonNode> map1 = internalIterator.next();
        JsonNode nodeValue1 = map1.getValue();
        String key1 = map1.getKey();
        if (nodeValue1.isTextual()) {
          pool.set_text(nodeValue1.asText());
        } else {
          channels.add(buildNodeChannel(nodeValue1, key1));
        }
      }
      pool.setChannels(channels);
      pools.add(pool);
    }
    mesure.setPools(pools);
    return mesure;
  }

  private static Channel buildNodeChannel(JsonNode node, String key) {

    Channel channel = new Channel();
    channel.setValue(key);
    Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();
    List<Nature> natures = new ArrayList<>();

    while (iterator.hasNext()) {
      Map.Entry<String, JsonNode> map = iterator.next();
      String key1 = map.getKey();
      JsonNode jsonNode = map.getValue();
      if (MivisuApiConstant.NODE_TEXT.equals(key1)) {
        channel.set_text(jsonNode.asText());
      } else {
        natures.add(buildNodeNature(jsonNode, key1));
      }
    }
    channel.setNatures(natures);
    return channel;
  }

  private static Nature buildNodeNature(JsonNode node, String key) {
    Nature nature = new Nature();
    nature.setValue(key);
    List<Period> periods = new ArrayList<>();
    Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();

    while (iterator.hasNext()) {
      Map.Entry<String, JsonNode> map = iterator.next();
      String key1 = map.getKey();
      JsonNode node1 = map.getValue();
      periods.add(buildNodePeriod(node1, key1));
    }
    nature.setPeriods(periods);
    return nature;
  }

  private static Period buildNodePeriod(JsonNode node, String key) {

    Period period = new Period();
    period.setValue(key);
    Iterator<Map.Entry<String, JsonNode>> iterator = node.fields();

    while (iterator.hasNext()) {
      Map.Entry<String, JsonNode> map = iterator.next();
      String key1 = map.getKey();
      JsonNode node1 = map.getValue();
      period.setClassMesure(buildNodeClassMesure(node1, key1));
    }
    return period;
  }

  private static ClassMesure buildNodeClassMesure(JsonNode root, String rootKey) {

    ClassMesure classMesure = new ClassMesure();
    classMesure.setValue(rootKey);
    Iterator<Map.Entry<String, JsonNode>> parentIterator = root.fields();

    while (parentIterator.hasNext()) {
      Map.Entry<String, JsonNode> mapParent = parentIterator.next();
      String key8 = mapParent.getKey();
      JsonNode child = mapParent.getValue();
      Iterator<Map.Entry<String, JsonNode>> childIterator = child.fields();

      try {
        if (childIterator.hasNext()) {
          Map.Entry<String, JsonNode> mapChild = childIterator.next();
          JsonNode nodeLeaf = mapChild.getValue();

          if (MivisuApiConstant.NODE_ID_MESURE.equals(key8)) {
            MesureId mesureId = new MesureId();
            mesureId.set_text(nodeLeaf.asText());
            classMesure.setId_mesure(mesureId);

          } else if (MivisuApiConstant.NODE_LOWCASE_TYPE.equals(key8)) {
            MesureType mesureType = new MesureType();
            mesureType.set_text(nodeLeaf.asText());
            classMesure.setMesureType(mesureType);

          } else if (MivisuApiConstant.NODE_LIBELLE.equals(key8)) {
            Libelle mesureLibelle = new Libelle();
            mesureLibelle.set_text(nodeLeaf.asText());
            classMesure.setMesureLibelle(mesureLibelle);
          }
        }
      } catch (Exception ex) {
        log.error("Error when building node ClassMesure: {} ", ex.getMessage());
        throw new RmhubException(ex.getMessage(), ex);
      }
    }
    return classMesure;
  }
}
