package org.gameontext.iotboard.provider;

public class DeviceRequest {
    
    @Override
    public String toString() {
        return "DeviceRequest [siteId=" + siteId + ", playerId=" + playerId + ", roomName=" + roomName + ", status="
                + status + ", lightAddress=" + lightAddress + ", lightId=" + lightId + "]";
    }

    private String siteId;
    private String playerId;
    private String roomName;
    private Boolean status;
    private String lightAddress;
    private String lightId;
    
    
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public void setRoomName(String roomId) {
        this.roomName = roomId;
    }

    public String getSiteId() {
        return siteId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setLightState(Boolean status) {
        this.status = status;
        
    }

    public Boolean getState() {
        return status;
    }

    public String getLightId() {
        return lightId;
    }

    public String getLightAddress() {
        return lightAddress;
    }

    public void setLightAddress(String lightAddress) {
        this.lightAddress = lightAddress;
    }

    public void setLightId(String lightId) {
        this.lightId = lightId;
    }

}
