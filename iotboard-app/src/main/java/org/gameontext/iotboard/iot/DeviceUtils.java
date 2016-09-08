package org.gameontext.iotboard.iot;

import java.io.ByteArrayInputStream;
import java.util.UUID;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;

import org.gameontext.iotboard.registration.DeviceRegistrationRequest;

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
    
    public static JsonObject marshallData(String payload) {
        ByteArrayInputStream bais = new ByteArrayInputStream (payload.getBytes());
        JsonReader rdr = Json.createReader(bais);
        JsonStructure json = rdr.read();
        JsonObject data = ((JsonObject) json).getJsonObject("d");
        return data;
    }
    
}
