package org.gameontext.iotboard.models;

public class DeviceRegistration {

    private String deviceId;
    private String playerId;

    public String getPlayerId() {
        return playerId;
    }
    
    public void setPlayerId(String playerid) {
        this.playerId = playerid;
    }

    public String getDeviceId() {
        // TODO Auto-generated method stub
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        
    }

}
