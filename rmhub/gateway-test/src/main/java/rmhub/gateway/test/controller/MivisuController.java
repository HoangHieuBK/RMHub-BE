package rmhub.gateway.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import rmhub.common.constant.MivisuMessageConstant;
import rmhub.common.kafkaconnector.messagebased.KafkaProducible;
import rmhub.common.utility.JsonHelper;
import rmhub.common.utility.XmlHelper;
import rmhub.model.mivisu.ssil.AbstractMsgBody;
import rmhub.model.mivisu.ssil.AllDataMsgBody;
import rmhub.model.mivisu.ssil.AuthMsgBody;
import rmhub.model.mivisu.ssil.SSILMessage;
import rmhub.model.mivisu.ssil.SSILMessageHeader;

@RestController
@Slf4j
@RequestMapping("/mivisu")
public class MivisuController {

  @Autowired
  private KafkaProducible<String, String> kafkaProducible;

  @Value("${rmhub.mivisu.topic.request}")
  private String topicRequest;

  @Value("${rmhub.mivisu.topic.request.device}")
  private String requestDevice;

  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  private void login() throws JsonProcessingException {
    SSILMessage ssilMessage = new SSILMessage();

    AbstractMsgBody corps = new AuthMsgBody("LABOCOM", "labocom");
    ssilMessage.setCorps(corps);

    SSILMessageHeader ent = new SSILMessageHeader();
    ent.setType("w");
    ent.setVersion(2);
    ent.setLgmes(0);
    ent.setId("DEF");
    ssilMessage.setEnt(ent);
    String json = JsonHelper.convertObject2Json(ssilMessage);
    kafkaProducible.send(topicRequest, json);
  }

  @PostMapping("/getMeasure")
  @ResponseStatus(HttpStatus.OK)
  private void getMeasure() throws JsonProcessingException {
    SSILMessage ssilMessage = new SSILMessage();
    ssilMessage.setInfo_txt("Externe 2 Mesure.Demande general equipements");
    SSILMessageHeader ent = new SSILMessageHeader();
    ent.setType("y");
    ent.setVersion(1);
    ent.setLgmes(0);
    ent.setId("123");
    ssilMessage.setEnt(ent);
    AbstractMsgBody corps = new AllDataMsgBody();
    ssilMessage.setCorps(corps);
    String json = JsonHelper.convertObject2Json(ssilMessage);

    String xmlString = XmlHelper.convertObject2XmlString(ssilMessage);

    System.out.println("xmlString" + xmlString);

    System.out.println("json " + json);

    String xml = XmlHelper.convertJsonString2XmlString(json);

    System.out.println("xml " + xml);

    kafkaProducible.send(topicRequest, json);

  }

  @GetMapping("/getDevices")
  @ResponseStatus(HttpStatus.OK)
  private void getDevice(@RequestParam Map<String, String> requestParams) throws JsonProcessingException {

    requestParams.put(MivisuMessageConstant.REQUEST_KEY, "request 123");
    String json = JsonHelper.convertObject2JsonNoWrapRoot(requestParams);

    kafkaProducible.send(requestDevice, json);
  }

  public static void main(String[] args) throws JsonProcessingException {
    SSILMessage ssilMessage = new SSILMessage();
    ssilMessage.setInfo_txt("Externe 2 Mesure.Demande general equipements");
    SSILMessageHeader ent = new SSILMessageHeader();
    ent.setType("y");
    ent.setVersion(1);
    ent.setLgmes(0);
    ent.setId("123");
    ssilMessage.setEnt(ent);
    AbstractMsgBody corps = new AllDataMsgBody();
    ssilMessage.setCorps(corps);
    String json = JsonHelper.convertObject2Json(ssilMessage);

    String xmlString = XmlHelper.convertObject2XmlString(ssilMessage);

    System.out.println("xmlString" + xmlString);

    System.out.println("json " + json);

    String xml = XmlHelper.convertJsonString2XmlString(json);

    System.out.println("xml " + xml);
  }
}
