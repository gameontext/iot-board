package org.gameontext.iotboard.models;

public class DeviceRegistrationRequest {

    private String deviceId;
    private String playerId;
    private String roomId;
    private String deviceType;

    public String getPlayerId() {
        return playerId;
    }
    
    public void setPlayerId(String playerid) {
        this.playerId = playerid;
    }

    @Override
    public String toString() {
        return "DeviceRegistrationRequest [deviceId=" + deviceId + ", playerId=" + playerId + ", deviceType="
                + deviceType + "]";
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

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

}
