package org.gameontext.iotboard.provider.room;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RoomList {

    private Map<String, Room> roomsByDeviceId = new HashMap<String, Room>();
    private Map<String, Room> roomsBySiteId = new HashMap<String, Room>();
    
    public void addRoom(Room room) {
        roomsByDeviceId.put(room.getDeviceId(), room);
        roomsBySiteId.put(room.getSiteId(), room);
    }

    public void removeRoom(Room room) {
        roomsByDeviceId.remove(room.getDeviceId());
        roomsBySiteId.remove(room.getSiteId());
    }


    public Room getRoomByDeviceId(String deviceId) {
        return roomsByDeviceId.get(deviceId);
    }


    public Collection<Room> allRooms() {
        return roomsByDeviceId.values();
    }

    public Room getRoomBySiteId(String siteId) {
        return roomsBySiteId.get(siteId);
    }
}
