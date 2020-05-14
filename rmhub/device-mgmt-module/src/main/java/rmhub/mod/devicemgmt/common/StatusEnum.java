package rmhub.mod.devicemgmt.common;

public enum StatusEnum {

  ACTIVE(1), INACTIVE(0), DELETE(2);

  StatusEnum(Integer value) {
    this.value = value;
  }

  private Integer value;

  public Integer getValue() {
    return value;
  }
}
