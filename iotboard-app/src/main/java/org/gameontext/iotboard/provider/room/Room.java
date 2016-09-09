package org.gameontext.iotboard.provider.room;

public class Room {
    private String siteId;
    private String playerId;
    private String roomName;
    private String lightAddress;
    private String deviceId;
    
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getSiteId() {
        return siteId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getRoomId() {
        return roomName;
    }

    public String getLightAddress() {
        return lightAddress;
    }

    public void setLightAddress(String lightAddress) {
        this.lightAddress = lightAddress;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

}
