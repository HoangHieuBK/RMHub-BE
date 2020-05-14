package rmhub.mod.weatherstation.constant;

public interface AlertRuleCommon {

  String API_PATH = "/alert_rules";
  String EXISTS_RULE_CODE = "  Alert rule does exist in the system ";
  String RULE_CODE_CHANGE = " Alert Rule Code not change ";
  String AIR_OPERATION_NOT_ALLOW = " Don’t allow to setup the Wind Alert 2 with operator <= when Wind Alert 1 with operation >= ";
  String AIR_VALUE_NOT_ALLOW = " Value of ALR_WIND_LEVEL1 should not greater than ALR_WIND_LEVEL2 ";
  String ALERT_RULE_NOT_FOUND = " Alert rule not found ";
  String ALERT_OPERATION_NOT_ALLOW = " Alert rule not alow for operation ";
  String DUPLICATE_VALUE_NOT_ALLOW = " Duplicated values are not allowed ";
}
