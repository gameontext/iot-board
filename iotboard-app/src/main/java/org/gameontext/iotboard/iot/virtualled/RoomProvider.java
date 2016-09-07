/*******************************************************************************
 * Copyright (c) 2016 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.gameontext.iotboard.iot.virtualled;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.gameontext.iotboard.MessageStack;
import org.gameontext.iotboard.RegistrationUtils;
import org.gameontext.iotboard.Request;
import org.gameontext.iotboard.iot.DeviceUtils;
import org.gameontext.iotboard.models.DeviceRegistrationRequest;
import org.gameontext.iotboard.models.devices.BoardControl;
import org.gameontext.iotboard.provider.BoardProvider;
import org.gameontext.iotboard.provider.DeviceRequest;
import org.gameontext.iotboard.provider.virtual.DeviceRegistrationResponse;
import org.gameontext.iotboard.provider.virtual.IoTConfiguration;
import org.gameontext.iotboard.provider.virtual.IoTReg;
import org.gameontext.iotboard.provider.virtual.IoTRegistrationResponse;
import org.gameontext.iotboard.provider.virtual.RegistrationResponseReader;

import com.ibm.iotf.client.app.Event;

@ApplicationScoped
public class RoomProvider implements BoardProvider {
    
    
    @Inject
    MessageStack messageStack;
    
    
    public static class Room {
        private String siteId;
        private String playerId;
        private String roomId;
        
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

    }

    private static final String supportedDeviceType = "GameOnRoom";

    Map<String, Room> roomsByDeviceId = new HashMap<String, Room>();
    
    @Inject
    IoTConfiguration iotConfig;
    
    @Inject
    RegistrationUtils regUtils;
    
    @Override
    public void process(BoardControl msg) {
        System.out.println("Processing control message : " + msg);
    }

    public DeviceRegistrationResponse registerDevice(DeviceRegistrationRequest registration) {
        DeviceUtils.assignDeviceId(registration);
        
        Room room = roomsByDeviceId.get(registration.getDeviceId());
        if (room == null) {
            room = new Room();
            roomsByDeviceId.put(registration.getDeviceId(), room);
        }

        DeviceRegistrationResponse drr = regUtils.registerDevice(registration);
        if (drr.hasReportedErrors()) {
            roomsByDeviceId.remove(registration.getDeviceId());
        } else {
            room.setSiteId(registration.getSiteId());
            room.setPlayerId(registration.getPlayerId());
            room.setRoomId(registration.getRoomId());
        }
        return drr;
    }

    @Override
    public String getSupportedDeviceType() {
        return supportedDeviceType;
    }

    @Override
    public DeviceRequest translateRequest(Event event) {
        System.out.println("Translating Request");
        String deviceType = event.getDeviceType();
        if (!deviceType.equals(supportedDeviceType)) {
            System.out.println("Throwing Exception");
            throw new RuntimeException("Cannot marshal data for event type " + deviceType);
        }
        DeviceRequest dr = new DeviceRequest();
        System.out.println("Marshalling data");
        javax.json.JsonObject payload = DeviceUtils.marshallData(event.getPayload());
        System.out.println("Marshalling complete");
        String deviceId = event.getDeviceId();
        Room room = roomsByDeviceId.get(deviceId);
        System.out.println("Room is " + room);
        dr.setSiteId(room.getSiteId());
        dr.setPlayerId(room.getPlayerId());
        dr.setRoomId(room.getRoomId());
        System.out.println("Getting data");
        javax.json.JsonObject data = payload.getJsonObject("data");
        System.out.println("Data object is " + data);
        String lightId = data.getString("lightId");
        System.out.println("Light ID is  " + lightId);
        Boolean lightState = data.getBoolean("lightState");
        System.out.println("Light ID is  " + lightState);
        dr.setStatus(lightState);
        System.out.println("Returning");
        return dr;
    }

    @Override
    public void handleRequest(DeviceRequest dr) {
        System.out.println("Got device request for room");
        
        IoTRoomPayload payload = new IoTRoomPayload();
        Data data = new Data();
        data.setLightId("userLight");
        data.setLightState(dr.getState());
        payload.setData(data);
        payload.setPlayerId(dr.getPlayerId());
        payload.setSiteId(dr.getSiteId());
        
        System.out.println("Sending payload: " + payload);
        for (Entry<String, Room> room : roomsByDeviceId.entrySet()) {
            if (room.getValue().getSiteId().equals(dr.getSiteId())) {
                Request r = new Request();
                r.setCommand("update");
                r.setDeviceId(room.getKey());
                r.setDeviceType(supportedDeviceType);
                r.setEvent(payload);
                messageStack.addRequest(r);
            }
        }
        
        
    }
    
    

}
