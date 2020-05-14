package rmhub.connector.mivisu.ssilm.behaviour;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import rmhub.common.constant.MivisuMessageConstant;
import rmhub.common.utility.JsonHelper;
import rmhub.common.utility.XmlHelper;
import rmhub.connector.mivisu.ssilm.queue.RequestQueue;
import rmhub.infras.socket.SocketIO;
import rmhub.model.mivisu.ssil.Centrale;
import rmhub.model.mivisu.ssil.Et;
import rmhub.model.mivisu.ssil.MivisuXml;
import rmhub.model.mivisu.ssil.TechnicalStatus;

@Component
@Slf4j
public class SsilmBehaviour {

  @Value("${rmhub.mivisu.topic.weather.station}")
  private String topicWs;

  @Value("${rmhub.mivisu.topic.technical.status}")
  private String topicTec;

  @Value("${rmhub.mivisu.topic.traffic.counting}")
  private String topicTc;

  @Value("${mivisu.connector.deploymentid}")
  private int deploymentId;

  @Value("${request.technical.data}")
  private String request;

  public void readFromMivisu(SocketIO io, Queue<byte[]> outgoing) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      InputStream input = io.getInputStream();
      String residue = "";
      int receiveBufferSize = io.getReceiveBufferSize();
      final byte[] bytes = new byte[receiveBufferSize];
      while (true) {
        int len = input.read(bytes);
        // If no byte is available because the end of the stream has been reached, the returned value is -1
        if (len == -1) {
          io.close();
        }
        else if (len > 0) {
          baos.write(bytes, 0, len);
          String s = new String(baos.toByteArray(), StandardCharsets.UTF_8);
          s = residue + s;
          int pos = s.indexOf(MivisuMessageConstant.XML_END_TAGE_MIVISU);
          int lenTag = MivisuMessageConstant.XML_END_TAGE_MIVISU.length();
          if (pos > 0) {
            String receive = s.substring(0, pos + lenTag);
            outgoing.add(receive.getBytes());
            residue = s.substring(pos + lenTag);
            baos.reset();
          }
        }
      }
    } catch (IOException e) {
      log.warn("Error when read mesuament data to mivisu: {} ", e.getMessage());
      io.close();
    }
  }

  public void writeToMivisu(Queue<byte[]> incoming, SocketIO io) {
    try {
      OutputStream output = io.getOutputStream();
      if (output != null) {
        do {
          final byte[] sendBytes = incoming.remove();
          log.info("Byte send request info: {} ", new String(sendBytes));
          output.write(sendBytes);
        } while (incoming.size() > 0);
        output.flush();
      }
    } catch (IOException e) {
      log.warn("Error when write request to mivisu: {} ", e.getMessage());
      io.close();
    }
  }

  public Map<String, String> analysisData(Queue<byte[]> outgoing) {
    var map = new HashMap<String, String>();
    try {
      byte[] receiveData = outgoing.remove();
      String xmlString = new String(receiveData, StandardCharsets.UTF_8);
      MivisuXml mivisuXml = XmlHelper.convertXmlString2Object(xmlString, MivisuXml.class);
      mivisuXml.setDeploymentId(deploymentId);
      String type = mivisuXml.getSsilMessageHeader().getType();
      int val = mivisuXml.getBody().getCr_Val();

      if (type != null && type.equals(MivisuMessageConstant.IEM_MES_EXP_CPR_POS_EQT) && val == 1) {

        log.info("Login successfully with code: {} from requestId: {}", val,
            mivisuXml.getSsilMessageHeader().getId());
        RequestQueue.pushRequestInfo(request.getBytes());
      } else if (type != null && type.equals(MivisuMessageConstant.IEM_MES_EXP_CPR_POS_EQT)) {

        log.error("Login error with error code: {} from requestId: {}",
            val, mivisuXml.getSsilMessageHeader().getId());
        return null;
      }
      if (type != null && type.equals(MivisuMessageConstant.IEM_MES_EXP_CPR_CPT_EQT)) {
        List<Centrale> centrales = mivisuXml.getBody().getCentrale();
        List<Centrale> centralesTc = new ArrayList<>();
        List<Centrale> centralesWs = new ArrayList<>();
        if (centrales != null) {
          for (Centrale item : centrales) {
            if (item.getId_ext().contains(MivisuMessageConstant.EQT_WS_TYPE)) {
              centralesWs.add(item);
            } else if (item.getId_ext().contains(MivisuMessageConstant.EQT_TC_TYPE)) {
              centralesTc.add(item);
            }
          }
          MivisuXml mivisuWs = SerializationUtils.clone(mivisuXml);
          MivisuXml mivisuTc = SerializationUtils.clone(mivisuXml);

          mivisuWs.getBody().setCentrale(centralesWs);
          mivisuTc.getBody().setCentrale(centralesTc);
          String jsonWs = JsonHelper.objectToJsonString(mivisuWs);
          String jsonTc = JsonHelper.objectToJsonString(mivisuTc);
          if (centralesWs.size() > 0) {
            map.put(topicWs, jsonWs);
          }
          if (centralesTc.size() > 0) {
            map.put(topicTc, jsonTc);
          }
        }
      } else if (type != null && type.equals(MivisuMessageConstant.IEM_MES_EXP_DEM_STA_EQT)) {
        int numberEt = mivisuXml.getBody().getNb_Et();
        List<Et> listEt = mivisuXml.getBody().getEt();
        var technicalStatus = TechnicalStatus.builder().nb_Et(numberEt).ets(listEt).deploymentId(deploymentId).build();
        String jsonTechnicalData = JsonHelper.objectToJsonString(technicalStatus);
        map.put(topicTec, jsonTechnicalData);
      }
    } catch (IOException e) {
      log.error("Convert from xml to object error");
      e.printStackTrace();
    }
    return map;
  }
}
