package rmhub.common.constant;

import java.util.Arrays;
import java.util.List;

public interface MivisuMessageConstant {

  String XML_END_TAGE_MIVISU = "</MIVISU>";
  String IEM_MES_EXP_DEM_STA_EQT = "C"; //type status of technical equipment
  String IEM_MES_EXP_CPR_CPT_EQT = "A"; // recode of rereading of measurements
  String IEM_MES_EXP_CPR_POS_EQT = "x"; // Mivisu Acknowledgment Message
  int TC_EQT_MES_TYPE = 0; // Traffic logger
  int WS_EQT_MES_TYPE = 1; // Weather station
  String EQT_MES_TYPE = "Eqt_Mes_Type";

  String IDEXT = "ID_EXT";

  String DEFAULT_STRING_VALUE = "";
  String DEFAULT_INT_VALUE = "0";

  String NUM = "NUM";
  String EQT_DT_MES = "Eqt_Dt_Mes";
  String EQT_MES_PER = "Eqt_Mes_Per";
  String EQT_MES_LG_ID = "Eqt_Mes_Lg_Id";
  String EQT_MES_ID = "Eqt_Mes_Id";
  String EQT_MES_LG_TYPE = "Eqt_Mes_Lg_Type";
  String EQT_MES_NB_VAL = "Eqt_Mes_Nb_Val";
  String EQT_MES_VAL_ = "Eqt_Mes_Val_";
  String EQT_MES_KLIF_ = "Eqt_Mes_Klif_";
  String EQT_MES_VAL_1 = "Eqt_Mes_Val_1";
  String EQT_MES_KLIF_1 = "Eqt_Mes_Klif_1";
  String TRAFFIC_MES_ID = "traffic_measurement_id";
  String WEATHER_MES_ID = "weather_measurement_id";
  String EQT_TC_TYPE = "TC";
  String EQT_WS_TYPE = "WS";
  String EQT_ALL_TYPE = "ALL";
  List<String> LIST_DEVICE_TYPE = Arrays.asList("TC", "WS");

  String REQUEST_KEY = "requestId";
  String DEVICE_TYPE = "deviceType";
  String DEPLOYMENT_ID = "deploymentId";
}
