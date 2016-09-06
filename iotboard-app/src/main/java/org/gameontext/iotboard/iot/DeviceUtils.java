package org.gameontext.iotboard.iot;

import java.util.UUID;

import org.gameontext.iotboard.models.DeviceRegistrationRequest;

public class DeviceUtils {

    
    public static String assignDeviceId(DeviceRegistrationRequest registration) {
        String deviceId = registration.getDeviceId();
        if (!isValidDeviceId(deviceId)) {
            deviceId = generateAlphaNum();
        }
        registration.setDeviceId(deviceId);
        return deviceId;
    }
    
    public static boolean isValidDeviceId(String deviceId) {
        return deviceId != null && deviceId.length() > 0;
    }
    

    public static String generateAlphaNum() {
        return UUID.randomUUID().toString().replaceAll("[^A-Za-z0-9]", "");
    }
    
}
