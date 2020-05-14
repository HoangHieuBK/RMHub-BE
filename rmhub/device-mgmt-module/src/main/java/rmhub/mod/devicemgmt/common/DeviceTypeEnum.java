package rmhub.mod.devicemgmt.common;

public enum DeviceTypeEnum {

  WEATHER_STATION(1L), TRAFFIC_COUNTING(2L), ALL(0L);

  private Long value;

  DeviceTypeEnum(Long value) {
    this.value = value;
  }

  public Long getValue() {
    return value;
  }
}
