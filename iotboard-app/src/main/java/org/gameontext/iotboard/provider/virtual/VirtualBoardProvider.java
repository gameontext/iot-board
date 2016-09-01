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

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;

import org.gameontext.iotboard.models.DeviceRegistration;
import org.gameontext.iotboard.models.devices.BoardControl;
import org.gameontext.iotboard.provider.BoardProvider;

@ApplicationScoped
public class VirtualBoardProvider implements BoardProvider {

    Map<String, Device> devices = new HashMap<String, Device>();

    @Override
    public String getProviderId() {
        return "VirtualBoardProvider";
    }

    @Override
    public void process(BoardControl msg) {
        System.out.println("Processing control message : " + msg);
    }

    @Override
    public void registerDevice(DeviceRegistration registration) {
        String deviceId = getOrCreateDeviceId(registration);

        Device device = devices.get(deviceId);
        if (device == null) {
            device = new Device();
        }

        String associatedPlayer = device.getAssociatedPlayer();
        if (associatedPlayer != null && !associatedPlayer.equals(registration.getPlayerId())) {
            throw new RuntimeException("Cannot reassign a device");
        }
        
        registerIntoIoTF(registration);

    }

    private void registerIntoIoTF(DeviceRegistration registration) {
        Client client = ClientBuilder.newClient();
        
        
        WebTarget target = client.target("https://pmoxqf.internetofthings.ibmcloud.com/api/v0002/device/types/VirtualDevice/devices");
//        WebTarget target = client.target("http://127.0.0.1:9080/iotboard/v1/devices/test");
        Builder requestBuilder = target.request();
        requestBuilder.header("Content-Type", "application/json");
        try {
            String basicAuthEncoded = Base64.getEncoder().encodeToString("a-pmoxqf-g2wyv0orrf:qz(8+qbVNdLDfED0si".getBytes("UTF-8"));
            System.out.println("Encoded strign is | " + basicAuthEncoded + "|");
            requestBuilder.header("Authorization", "Basic " + basicAuthEncoded);
            
//            requestBuilder.header("Authorization", "BASIC " + DatatypeConverter.printBase64Binary("foo:barr".getBytes("UTF-8")));
            
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("Registering device with ID " + registration.getDeviceId());
        IoTReg iotReg = new IoTReg(registration.getDeviceId(), "790y3h9t80yh");
        Entity<IoTReg> iotentity = Entity.json(iotReg);
        System.out.println("Looks like: |"  + iotentity.toString() + "|");
        
        Response response = requestBuilder.post(Entity.json(iotReg));
        System.out.println("Got back: " + response.getStatus());
        
        RegistrationResponse rr = response.readEntity(RegistrationResponse.class);
        
        System.out.println("Returned payload: " + rr);
        
        
    }

    private String getOrCreateDeviceId(DeviceRegistration registration) {
        String deviceId = registration.getDeviceId();
        if (deviceId == null) {
            deviceId = UUID.randomUUID().toString();
            System.out.println("Geenrated " + deviceId);
            deviceId = deviceId.replaceAll("[^A-Za-z0-9]", "");
        }

        registration.setDeviceId(deviceId);
        return deviceId;
    }

}
