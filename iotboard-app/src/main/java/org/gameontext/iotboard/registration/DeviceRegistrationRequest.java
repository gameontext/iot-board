package org.gameontext.iotboard.registration;

public class DeviceRegistrationRequest {

    private String deviceId;
    private String playerId;
    private String roomName;
    private String siteId;
    private String deviceType;

    public String getPlayerId() {
        return playerId;
    }
    
    public void setPlayerId(String playerid) {
        this.playerId = playerid;
    }

    @Override
    public String toString() {
        return "DeviceRegistrationRequest [deviceId=" + deviceId + ", playerId=" + playerId + ", roomName=" + roomName
                + ", siteId=" + siteId + ", deviceType=" + deviceType + "]";
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

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

}
