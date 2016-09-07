package org.gameontext.iotboard.provider;

import java.util.HashMap;
import java.util.Map;

public class DeviceRequest {
    
    private String siteId;
    private String playerId;
    private String roomId;
    Map<String, Boolean> requests = new HashMap<String, Boolean>();
    private Boolean status;
    
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getSiteId() {
        return siteId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setStatus(Boolean status) {
        this.status = status;
        
    }

    public Boolean getState() {
        return status;
    }

}
