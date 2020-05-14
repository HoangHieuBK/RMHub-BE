package rmhub.common.kafkaconnector.constant;

public interface KafkaConstant {

  String CLIENT_ID = "RmhubClient";

  String GROUP_ID_CONFIG = "rmhub";

  String OFFSET_RESET_LATEST = "latest";

  String OFFSET_RESET_EARLIER = "earliest";

  Integer MAX_POLL_RECORDS = 1;
}
