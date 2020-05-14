package rmhub.connector.mivisu.ssilm.behaviour;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Queue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import rmhub.connector.mivisu.ssilm.queue.RequestQueue;
import rmhub.connector.mivisu.ssilm.queue.ResponseQueue;
import rmhub.infras.socket.SocketIO;

@ExtendWith(SpringExtension.class)
class SsilmBehaviourTest {

  @InjectMocks
  private SsilmBehaviour ssilmBehaviour;

  @Mock
  private SocketIO socketIO;

  @Mock
  private OutputStream output;

  @Mock
  private InputStream inputStream;

  @BeforeEach
  void setupBefore() {
    ReflectionTestUtils.setField(ssilmBehaviour, "topicWs", "weather.station");
    ReflectionTestUtils.setField(ssilmBehaviour, "topicTec", "technical.status");
    ReflectionTestUtils.setField(ssilmBehaviour, "topicTc", "traffic.counting");
    ReflectionTestUtils.setField(ssilmBehaviour, "deploymentId", 1);
    ReflectionTestUtils.setField(ssilmBehaviour, "request", "test");
  }

  @Disabled("do not know how to test")
  @Test
  void readFromMivisu() {
    // TODO
  }

  @Test
  void readFromMivisu_throwException() throws Exception {
    Queue<byte[]> requestQueue = RequestQueue.getRequestQueue();
    requestQueue.add("test".getBytes());

    when(socketIO.getInputStream()).thenReturn(inputStream);

    doThrow(new IOException()).when(inputStream).read(Mockito.any());

    ssilmBehaviour.readFromMivisu(socketIO, requestQueue);
  }

  @Test
  void writeToMivisu() {
    Queue<byte[]> requestQueue = RequestQueue.getRequestQueue();
    requestQueue.add(new byte[1]);

    when(socketIO.getOutputStream()).thenReturn(output);

    ssilmBehaviour.writeToMivisu(requestQueue, socketIO);

    requestQueue.clear();
  }

  @Test
  void writeToMivisu_outputNull() {
    Queue<byte[]> requestQueue = RequestQueue.getRequestQueue();
    requestQueue.add(new byte[1]);

    when(socketIO.getOutputStream()).thenReturn(null);

    ssilmBehaviour.writeToMivisu(requestQueue, socketIO);

    requestQueue.clear();
  }

  @Test
  void writeToMivisu_throwException() throws Exception {

    Queue<byte[]> requestQueue = RequestQueue.getRequestQueue();
    requestQueue.add("test".getBytes());

    when(socketIO.getOutputStream()).thenReturn(output);

    doThrow(new IOException()).when(output).write(Mockito.any());

    ssilmBehaviour.writeToMivisu(requestQueue, socketIO);
  }

  @Test
  void analysisData_TypeA() throws Exception {
    File file = new ClassPathResource("dataTestSendMivisuResponseTypeA.xml").getFile();

    BufferedReader br = new BufferedReader(new FileReader(file));

    String st;
    StringBuilder tmp = new StringBuilder();
    while ((st = br.readLine()) != null) {
      tmp.append(st);
    }

    Queue<byte[]> outgoing = ResponseQueue.getResponseQueue();

    outgoing.add(tmp.toString().getBytes());

    ssilmBehaviour.analysisData(outgoing);

    outgoing.clear();
  }

  @Test
  void analysisData_TypeAWS() throws Exception {
    File file = new ClassPathResource("dataTestSendMivisuResponseTypeAWS.xml").getFile();

    BufferedReader br = new BufferedReader(new FileReader(file));

    String st;
    StringBuilder tmp = new StringBuilder();
    while ((st = br.readLine()) != null) {
      tmp.append(st);
    }

    Queue<byte[]> outgoing = ResponseQueue.getResponseQueue();

    outgoing.add(tmp.toString().getBytes());

    ssilmBehaviour.analysisData(outgoing);

    outgoing.clear();
  }

  @Test
  void analysisData_TypeATC() throws Exception {
    File file = new ClassPathResource("dataTestSendMivisuResponseTypeATC.xml").getFile();

    BufferedReader br = new BufferedReader(new FileReader(file));

    String st;
    StringBuilder tmp = new StringBuilder();
    while ((st = br.readLine()) != null) {
      tmp.append(st);
    }

    Queue<byte[]> outgoing = ResponseQueue.getResponseQueue();

    outgoing.add(tmp.toString().getBytes());

    ssilmBehaviour.analysisData(outgoing);

    outgoing.clear();
  }

  @Test
  void analysisData_TypeC() throws Exception {
    File file = new ClassPathResource("dataTestSendMivisuResponseTypeC.xml").getFile();

    BufferedReader br = new BufferedReader(new FileReader(file));

    String st;
    StringBuilder tmp = new StringBuilder();
    while ((st = br.readLine()) != null) {
      tmp.append(st);
    }

    Queue<byte[]> outgoing = ResponseQueue.getResponseQueue();

    outgoing.add(tmp.toString().getBytes());

    ssilmBehaviour.analysisData(outgoing);

    outgoing.clear();
  }

  @Test
  void analysisData_TypeX() throws Exception {
    File file = new ClassPathResource("dateTestSendMivisuResponseTypeX.xml").getFile();

    BufferedReader br = new BufferedReader(new FileReader(file));

    String st;
    StringBuilder tmp = new StringBuilder();
    while ((st = br.readLine()) != null) {
      tmp.append(st);
    }

    Queue<byte[]> outgoing = ResponseQueue.getResponseQueue();

    outgoing.add(tmp.toString().getBytes());

    ssilmBehaviour.analysisData(outgoing);

    outgoing.clear();
  }

  @Test
  void analysisData_typeX_null() throws Exception {

    File file = new ClassPathResource("dateTestSendMivisuResponseTypeNull.xml").getFile();

    BufferedReader br = new BufferedReader(new FileReader(file));

    String st;
    StringBuilder tmp = new StringBuilder();
    while ((st = br.readLine()) != null) {
      tmp.append(st);
    }

    Queue<byte[]> outgoing = ResponseQueue.getResponseQueue();

    outgoing.add(tmp.toString().getBytes());

    ssilmBehaviour.analysisData(outgoing);

    outgoing.clear();
  }

  @Test
  void analysisData_typeC_crValEqual_0() throws Exception {

    File file = new ClassPathResource("dateTestSendMivisuResponseTypeXCrVal0.xml").getFile();

    BufferedReader br = new BufferedReader(new FileReader(file));

    String st;
    StringBuilder tmp = new StringBuilder();
    while ((st = br.readLine()) != null) {
      tmp.append(st);
    }

    Queue<byte[]> outgoing = ResponseQueue.getResponseQueue();

    outgoing.add(tmp.toString().getBytes());

    ssilmBehaviour.analysisData(outgoing);

    outgoing.clear();
  }

  @Test
  void analysisData_typeA_centralNull() throws Exception {

    File file = new ClassPathResource("dataTestSendMivisuResponseTypeACentralNull.xml").getFile();

    BufferedReader br = new BufferedReader(new FileReader(file));

    String st;
    StringBuilder tmp = new StringBuilder();
    while ((st = br.readLine()) != null) {
      tmp.append(st);
    }

    Queue<byte[]> outgoing = ResponseQueue.getResponseQueue();

    outgoing.add(tmp.toString().getBytes());

    ssilmBehaviour.analysisData(outgoing);

    outgoing.clear();
  }

  @Test
  void analysisData_typeA_throwException() {

    Queue<byte[]> outgoing = ResponseQueue.getResponseQueue();

    outgoing.add("null".getBytes());

    ssilmBehaviour.analysisData(outgoing);

    outgoing.clear();
  }
}
