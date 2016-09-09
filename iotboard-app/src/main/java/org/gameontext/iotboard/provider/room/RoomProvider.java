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
package org.gameontext.iotboard.provider.room;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;

import org.gameontext.iotboard.iot.DeviceUtils;
import org.gameontext.iotboard.iot.IoTConfiguration;
import org.gameontext.iotboard.iot.IoTMessage;
import org.gameontext.iotboard.iot.MessageStack;
import org.gameontext.iotboard.models.devices.BoardControl;
import org.gameontext.iotboard.provider.CannotTranslateEventException;
import org.gameontext.iotboard.provider.Data;
import org.gameontext.iotboard.provider.DeviceHandler;
import org.gameontext.iotboard.provider.DeviceRequest;
import org.gameontext.iotboard.provider.IoTPayload;
import org.gameontext.iotboard.registration.DeviceRegistrationRequest;
import org.gameontext.iotboard.registration.DeviceRegistrationResponse;
import org.gameontext.iotboard.registration.RegistrationUtils;

import com.ibm.iotf.client.app.Event;

@ApplicationScoped
public class RoomProvider implements DeviceHandler {
    
    @Inject
    MessageStack messageStack;
    
    private static final String supportedDeviceType = "GameOnRoom";

    @Inject
    RoomList roomList;
    
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
        
        Room room = roomList.getRoomByDeviceId(registration.getDeviceId());
        if (room == null) {
            room = new Room();
        }

        room.setDeviceId(registration.getDeviceId());
        room.setSiteId(registration.getSiteId());
        room.setPlayerId(registration.getPlayerId());
        room.setRoomName(registration.getRoomName());
        
        DeviceRegistrationResponse drr = regUtils.registerDevice(registration);
        if (!drr.hasReportedErrors()) {
            roomList.addRoom(room);
        }

        return drr;
    }

    @Override
    public String getSupportedDeviceType() {
        return supportedDeviceType;
    }

    @Override
    public DeviceRequest translateRequest(Event event) throws CannotTranslateEventException {
        System.out.println("Translating request for " + event.getDeviceId());
        String deviceType = event.getDeviceType();
        if (!deviceType.equals(supportedDeviceType)) {
            throw new CannotTranslateEventException(supportedDeviceType);
        }
        DeviceRequest dr = new DeviceRequest();
        JsonObject payload = DeviceUtils.marshallData(event.getPayload());
        
        String siteId = payload.getString("siteId");
        dr.setSiteId(siteId);

        String playerId = payload.getString("playerId");
        dr.setPlayerId(playerId);
        
        String roomName = payload.getString("roomName");
        dr.setRoomName(roomName);
        
        JsonObject data = payload.getJsonObject("data");
        String lightId = "player";
        if (data.containsKey("lightId")) {
            lightId = data.getString("lightId");
        }
        dr.setLightId(lightId);
        
        Boolean lightState = data.getBoolean("lightState");
        dr.setLightState(lightState);
        
        String lightAddress = data.getString("lightAddress");
        dr.setLightAddress(lightAddress);
        System.out.println("Getting Room List");
        Room room = roomList.getRoomByDeviceId(event.getDeviceId());
        System.out.println("Found room");
        room.setLightAddress(lightAddress);
        
        System.out.println("Finished processing request for " + supportedDeviceType);
        return dr;
    }

    @Override
    public void handleRequest(DeviceRequest dr) {
        System.out.println("Room: handling request " + dr);
        
        IoTPayload payload = new IoTPayload();
        Data data = new Data();
        data.setLightId("userLight");
        data.setLightState(dr.getState());
        payload.setData(data);
        payload.setPlayerId(dr.getPlayerId());
        payload.setSiteId(dr.getSiteId());
        
        System.out.println("Sending payload: " + payload);
        for (Room room : roomList.allRooms()) {
            if (room.getSiteId().equals(dr.getSiteId())) {
                IoTMessage r = new IoTMessage();
                r.setCommand("update");
                r.setDeviceId(room.getDeviceId());
                r.setDeviceType(supportedDeviceType);
                r.setEvent(payload);
                System.out.println("Room: About to add message to queue: " + r);
                messageStack.addRequest(r);
            }
        }
        
        
    }

    public void validateRequest(DeviceRequest dr) throws InvalidRequestException {
        System.out.println("Validating request");
        String requestLightToChange = dr.getLightAddress();
        Room matchingRoom = roomList.getRoomBySiteId(dr.getSiteId());
        if (!matchingRoom.getLightAddress().equals(requestLightToChange)) {
            throw new InvalidRequestException("Light requested to change " + requestLightToChange + " does not match currently used light " + matchingRoom.getLightAddress());
        }
        System.out.println("Request is valid");
    }

    public Collection<DeviceRequest> getInterestedRooms(DeviceRequest dr) {
        System.out.println("Finding other interested rooms");
        List<DeviceRequest> deviceRequests = new ArrayList<DeviceRequest>();
        String requestLightToChange = dr.getLightAddress();
        for (Room room : roomList.allRooms()) {
            if (dr.getLightAddress().equals(room.getLightAddress())) {
                DeviceRequest newRequest = new DeviceRequest();
                String siteId = room.getSiteId();
                newRequest.setSiteId(siteId);

                String playerId = room.getPlayerId();
                newRequest.setPlayerId(playerId);
                
                String roomName = room.getRoomId();
                newRequest.setRoomName(roomName);
                
                String lightId = "player";
                newRequest.setLightId(lightId);
                
                Boolean lightState = dr.getState();
                newRequest.setLightState(lightState);
                
                String lightAddress = requestLightToChange;
                newRequest.setLightAddress(lightAddress);
                deviceRequests.add(newRequest);
            }
        }
        
        System.out.println("Returning " + deviceRequests);
        return deviceRequests;
    }
    
    

}
