package org.gameontext.iotboard.provider.virtual;

import org.gameontext.iotboard.provider.room.Data;

public class IotVirtualBoardPayload {

    
    private VirtualBoardData data;
    private String sid;
    private String gid;
    private String name;
    
    public VirtualBoardData getData() {
        return data;
    }
    public void setData(VirtualBoardData data) {
        this.data = data;
    }
    public String getSid() {
        return sid;
    }
    public void setSid(String siteId) {
        this.sid = siteId;
    }
    public String getGid() {
        return gid;
    }
    public void setGid(String playerId) {
        this.gid = playerId;
    }
    public String getName() {
        return name;
    }
    public void setName(String roomName) {
        this.name = roomName;
    }
    
    
}
