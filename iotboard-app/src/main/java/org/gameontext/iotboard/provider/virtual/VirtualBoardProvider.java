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
package org.gameontext.iotboard.provider.virtual;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;

import org.gameontext.iotboard.MessageStack;
import org.gameontext.iotboard.RegistrationUtils;
import org.gameontext.iotboard.Request;
import org.gameontext.iotboard.iot.DeviceUtils;
import org.gameontext.iotboard.models.DeviceRegistrationRequest;
import org.gameontext.iotboard.models.devices.BoardControl;
import org.gameontext.iotboard.provider.BoardProvider;
import org.gameontext.iotboard.provider.DeviceRequest;

import com.ibm.iotf.client.app.Event;

@ApplicationScoped
public class VirtualBoardProvider implements BoardProvider {
    
    
    @Inject
    MessageStack messageStack;
    
    
    private static final String supportedDeviceType = "VirtualBoard";

    Map<String, Devices> devicesByPlayer = new HashMap<String, Devices>();
    
    @Inject
    RegistrationUtils regUtils;
    
    @Override
    public void process(BoardControl msg) {
        System.out.println("Processing control message : " + msg);
    }

    public DeviceRegistrationResponse registerDevice(DeviceRegistrationRequest registration) {
        DeviceUtils.assignDeviceId(registration);
        
        Devices devices = devicesByPlayer.get(registration.getPlayerId());
        if (devices == null) {
            devices = new Devices();
            devicesByPlayer.put(registration.getPlayerId(), devices);
        }

        DeviceRegistrationResponse drr = regUtils.registerDevice(registration);
           
        if (!drr.hasReportedErrors()) {
            devices.add(drr.getDeviceId());
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
        JsonObject payload = DeviceUtils.marshallData(event.getPayload());
        System.out.println("Marshalling complete");
        
        
        String siteId = payload.getString("siteId");
        System.out.println("Site Id is " + siteId);
        dr.setSiteId(siteId);
        

        String playerId = payload.getString("playerId");
        System.out.println("Player Id is " + playerId);
        dr.setPlayerId(playerId);
        
        String roomId = payload.getString("roomId");
        System.out.println("Room Id is " + roomId);
        dr.setRoomId(roomId);
        
        System.out.println("Getting data");
        JsonObject data = payload.getJsonObject("data");
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
        
        IotVirtualBoardPayload payload = new IotVirtualBoardPayload();
        payload.setGid(dr.getPlayerId());
        payload.setSid(dr.getSiteId());
        payload.setName("Test name");
        VirtualBoardData data = new VirtualBoardData();
        data.setLight("player");
        data.setStatus(dr.getState());
        payload.setData(data);
        
        Devices devices = devicesByPlayer.get(dr.getPlayerId());
        for (String deviceId : devices.getDevices()) {
            Request r = new Request();
            r.setCommand("update");
            r.setDeviceId(deviceId);
            r.setDeviceType(supportedDeviceType);
            r.setEvent(payload);
            messageStack.addRequest(r);
        }
        
    }

}
