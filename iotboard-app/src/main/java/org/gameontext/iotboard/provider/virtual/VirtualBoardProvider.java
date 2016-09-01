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
import java.util.Properties;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.gameontext.iotboard.models.DeviceRegistration;
import org.gameontext.iotboard.models.devices.BoardControl;
import org.gameontext.iotboard.provider.BoardProvider;

import com.ibm.iotf.client.app.ApplicationClient;

@ApplicationScoped
public class VirtualBoardProvider implements BoardProvider {

    Map<String, Device> devices = new HashMap<String, Device>();
    @Inject
    IoTConfiguration iotConfig;
    
    ApplicationClient appclient = null;

    String deviceId  = null;
    @Override
    public String getProviderId() {
        return "VirtualBoardProvider";
    }

    @Override
    public void process(BoardControl msg) {
        System.out.println("Processing control message : " + msg);
    }

    public DeviceRegistrationResponse registerDevice(DeviceRegistration registration) {
        String deviceId = getOrCreateDeviceId(registration);

        Device device = devices.get(deviceId);
        if (device == null) {
            device = new Device();
        }

        String associatedPlayer = device.getAssociatedPlayer();
        if (associatedPlayer != null && !associatedPlayer.equals(registration.getPlayerId())) {
            throw new RuntimeException("Cannot reassign a device");
        }
        
        RegistrationResponse rr = registerIntoIoTF(registration);

        DeviceRegistrationResponse drr = new DeviceRegistrationResponse();
        
        
        drr.setIotMessagingOrgAndHost(iotConfig.getiotMessagingOrgAndHost());
        drr.setIotMessagingPort(iotConfig.getIotMessagingPort());
        drr.setDeviceId(rr.getDeviceId());
        drr.setDeviceAuthToken(rr.getAuthToken());
        drr.setIotClientId(rr.getClientId());
        drr.setEventTopic("iot-2/type/"+ rr.getTypeId() +"/id/"+ rr.getDeviceId() + "/evt/+/fmt/json");
        drr.setCmdTopic("iot-2/cmd/test/fmt/json");

        
        try {
            Properties props = new Properties();
            props.setProperty("id", "iot-webapp");
            props.setProperty("Organization-ID", iotConfig.getIotOrg());
            props.setProperty("Authentication-Method", "apikey");
            props.setProperty("API-Key", iotConfig.getApiKey());
            props.setProperty("Authentication-Token", iotConfig.getApiToken());
            System.out.println("props: " + props);
            appclient = new ApplicationClient(props);
            deviceId = drr.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drr;
    }

    private RegistrationResponse registerIntoIoTF(DeviceRegistration registration) {
        Client client = ClientBuilder.newClient().register(RegistrationResponseReader.class);
        
        WebTarget target = client.target(iotConfig.deviceRegistartionUrl());
        Builder requestBuilder = target.request();
        requestBuilder.header("Content-Type", "application/json");
        try {
            String authString = iotConfig.getApiKey() + ":" + iotConfig.getApiToken();
            String basicAuthEncoded = Base64.getEncoder().encodeToString(authString.getBytes("UTF-8"));
            requestBuilder.header("Authorization", "Basic " + basicAuthEncoded);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("Registering device with ID " + registration.getDeviceId());
        IoTReg iotReg = new IoTReg(registration.getDeviceId(), generateAlphaNum());
        
        Response response = requestBuilder.post(Entity.json(iotReg));
        System.out.println("Got back: " + response.getStatus());
        
        RegistrationResponse rr = response.readEntity(RegistrationResponse.class);
        
        System.out.println("Returned payload: " + rr);
        return rr;
        
    }

    private String getOrCreateDeviceId(DeviceRegistration registration) {
        String deviceId = registration.getDeviceId();
        if (deviceId == null) {
            deviceId = generateAlphaNum();
        }

        registration.setDeviceId(deviceId);
        return deviceId;
    }
    
    private String generateAlphaNum() {
        return UUID.randomUUID().toString().replaceAll("[^A-Za-z0-9]", "");
    }

    public void trigger() {
        System.out.println("Triggering");
        try {
            appclient.connect(1);
//            System.out.println("Did it deliver? " + appclient.publishCommand("VirtualDevice", deviceId, "test", "data"));
            System.out.println("Did it deliver? " + appclient.publishEvent("VirtualDevice", deviceId, "test", "data"));
            appclient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

}
