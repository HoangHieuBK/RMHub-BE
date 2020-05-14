package rmhub.mod.devicemgmt.common;

import java.util.Arrays;
import java.util.List;

public interface PhysicalDeviceConst {

  String NAME = "name";
  String IS_REGISTERED = "isRegistered";
  String LAST_MODIFIED_DATE = "lastModifiedDate";
  String STATUS = "status";
  String DEVICE_TYPE = "deviceType";
  String DEPLOYMENT_ID = "deploymentId";

  List<Integer> VALID_STATUS = Arrays.asList(StatusEnum.ACTIVE.getValue(), StatusEnum.INACTIVE.getValue());
}
