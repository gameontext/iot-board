package org.gameontext.iotboard.registration;

public class IoTReg {

    private String authToken;
    private String deviceId;

    public IoTReg(String deviceId, String authToken) {
        this.setDeviceId(deviceId);
        this.setAuthToken(authToken);
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

}
