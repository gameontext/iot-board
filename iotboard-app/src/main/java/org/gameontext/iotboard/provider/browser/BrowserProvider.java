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
package org.gameontext.iotboard.provider.browser;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonObject;

import org.gameontext.iotboard.iot.DeviceUtils;
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
public class BrowserProvider implements DeviceHandler {

    @Inject
    MessageStack messageStack;

    private static final String supportedDeviceType = "VirtualBoard";

    ConcurrentMap<String, DeviceList> devicesByPlayer = new ConcurrentHashMap<String, DeviceList>();

    @Inject
    RegistrationUtils regUtils;

    @Override
    public void process(BoardControl msg) {
        System.out.println("Processing control message : " + msg);
    }

    public DeviceRegistrationResponse registerDevice(DeviceRegistrationRequest registration) {
        DeviceUtils.assignDeviceId(registration);

        DeviceList devices = devicesByPlayer.get(registration.getPlayerId());
        if (devices == null) {
            devices = new DeviceList();
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
        String lightId = data.getString("lightId");
        dr.setLightId(lightId);

        Boolean lightState = data.getBoolean("lightState");
        dr.setLightState(lightState);

        String lightAddress = data.getString("lightAddress");
        dr.setLightAddress(lightAddress);

        System.out.println("Finished processing request for " + supportedDeviceType);
        return dr;
    }

    @Override
    public void handleRequest(DeviceRequest dr) {
        System.out.println("VirtualBoard: Handling request: " + dr);
        IoTPayload payload = new IoTPayload();
        payload.setPlayerId(dr.getPlayerId());
        payload.setSiteId(dr.getSiteId());
        payload.setRoomName(dr.getRoomName());
        Data data = new Data();
        data.setLightId(dr.getLightId());
        data.setLightState(dr.getState());
        data.setLightAddress(dr.getLightAddress());
        payload.setData(data);

        DeviceList devices = devicesByPlayer.get(dr.getPlayerId());
        if (devices != null) {
            for (String deviceId : devices.getDevices()) {
                IoTMessage r = new IoTMessage();
                r.setCommand("update");
                r.setDeviceId(deviceId);
                r.setDeviceType(supportedDeviceType);
                r.setEvent(payload);
                System.out.println("VirtualBoard: About to add message to queue: " + r);
                messageStack.addRequest(r);
            }
        }

    }

}
