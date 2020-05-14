package rmhub.mod.devicemgmt.dto;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import rmhub.mod.devicemgmt.common.DeviceConst;
import rmhub.mod.devicemgmt.entity.PhysicalDevice;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhysicalDeviceResponse {

    public PhysicalDeviceResponse(PhysicalDevice physicalDevice) {
        this.id = physicalDevice.getId();
        this.external_id = physicalDevice.getExternalId();
        this.name = physicalDevice.getName();
        this.status = physicalDevice.getStatus();
        this.registration = DeviceConst.UNREGISTERED;
        if (physicalDevice.getIsRegistered()) {
            this.registration = DeviceConst.REGISTERED;
        }
        this.lastUpdate = physicalDevice.getLastModifiedDate();
        this.latitude = physicalDevice.getLatitude();
        this.longitude = physicalDevice.getLongitude();
        this.atKilometers = physicalDevice.getAtKilometers();
        this.atMeters = physicalDevice.getAtMeters();
        this.motorway = "M44";
        this.physical_address = null;
        this.logical_address = null;
    }

    Long id;
    String external_id;
    String name;
    Integer status;
    String registration;
    Date lastUpdate;
    Double latitude;
    Double longitude;
    Integer atKilometers;
    Integer atMeters;
    // TODO: fixme
    String motorway = "M44";
    Long physical_address;
    Long logical_address;
}
