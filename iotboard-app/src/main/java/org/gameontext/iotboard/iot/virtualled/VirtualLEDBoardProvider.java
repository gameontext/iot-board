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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.gameontext.iotboard.MessageStack;
import org.gameontext.iotboard.RegistrationUtils;
import org.gameontext.iotboard.iot.DeviceUtils;
import org.gameontext.iotboard.models.DeviceRegistrationRequest;
import org.gameontext.iotboard.models.devices.BoardControl;
import org.gameontext.iotboard.provider.BoardProvider;
import org.gameontext.iotboard.provider.DeviceRequest;
import org.gameontext.iotboard.provider.virtual.DeviceRegistrationResponse;
import org.gameontext.iotboard.provider.virtual.Devices;

import com.ibm.iotf.client.app.Event;

@ApplicationScoped
public class VirtualLEDBoardProvider implements BoardProvider {
    
    @Inject
    RegistrationUtils regUtils;
    
    @Inject
    MessageStack messageStack;
    
    private static final String supportedDeviceType = "VirtualLEDBoard";

    Map<String, Devices> availableBoardsByDeviceId = new HashMap<String, Devices>();

    @Override
    public void process(BoardControl msg) {
        System.out.println("Processing control message : " + msg);
    }

    public DeviceRegistrationResponse registerDevice(DeviceRegistrationRequest registration) {
        DeviceUtils.assignDeviceId(registration);
        
        Devices devices = availableBoardsByDeviceId.get(registration.getPlayerId());
        if (devices == null) {
            devices = new Devices();
            availableBoardsByDeviceId.put(registration.getDeviceId(), devices);
        }

        DeviceRegistrationResponse drr = regUtils.registerDevice(registration);
        
        if (drr.hasReportedErrors()) {
            availableBoardsByDeviceId.remove(drr.getDeviceId());
        }
        return drr;
    }



    @Override
    public String getSupportedDeviceType() {
        return supportedDeviceType;
    }

    @Override
    public DeviceRequest translateRequest(Event event) {
        // TODO Auto-generated method stub
        
        // Handle the situation where a board emits an event to say its full
        return null;
    }

    @Override
    public void handleRequest(DeviceRequest dr) {
        //Find a slot on an available device, or reuse an existign slot
        
    }

}
