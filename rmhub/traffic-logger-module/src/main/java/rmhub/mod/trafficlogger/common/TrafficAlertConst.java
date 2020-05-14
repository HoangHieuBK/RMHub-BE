package rmhub.mod.trafficlogger.common;

public interface TrafficAlertConst {

  int MIN_LEVEL = 1;
  int MAX_LEVEL = 5;
  int MAX_ALERT_RULE = 3;
  int DESCRIPTION_MAX_LENGTH = 50;
  int MIN_SPEED = 0;
  int MAX_SPEED = 1000;
  String COLOR_CODE_REGEX = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$";
  String TRAFFIC_ALERT_CACHE_NAME = "alertSettingFindCache";
  String API_ROOT_PATH = "/alert_rules";
}
