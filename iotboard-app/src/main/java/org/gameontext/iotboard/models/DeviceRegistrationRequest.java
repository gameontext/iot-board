package org.gameontext.iotboard.models;

public class DeviceRegistrationRequest {

    private String deviceId;
    private String playerId;
    private String deviceType;

    public String getPlayerId() {
        return playerId;
    }
    
    public void setPlayerId(String playerid) {
        this.playerId = playerid;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        
    }
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
    public String getDeviceType() {
        return this.deviceType;
    }

}
