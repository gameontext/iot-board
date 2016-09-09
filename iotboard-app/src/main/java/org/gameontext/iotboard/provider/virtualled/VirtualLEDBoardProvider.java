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
package org.gameontext.iotboard.provider.virtualled;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.gameontext.iotboard.iot.DeviceUtils;
import org.gameontext.iotboard.iot.IoTMessage;
import org.gameontext.iotboard.iot.MessageStack;
import org.gameontext.iotboard.models.devices.BoardControl;
import org.gameontext.iotboard.provider.CannotTranslateEventException;
import org.gameontext.iotboard.provider.DeviceHandler;
import org.gameontext.iotboard.provider.DeviceRequest;
import org.gameontext.iotboard.registration.DeviceRegistrationRequest;
import org.gameontext.iotboard.registration.DeviceRegistrationResponse;
import org.gameontext.iotboard.registration.RegistrationUtils;

import com.ibm.iotf.client.app.Event;

@ApplicationScoped
public class VirtualLEDBoardProvider implements DeviceHandler {
    
    @Inject
    MessageStack messageStack;
    
    @Inject
    RegistrationUtils regUtils;
    
    private static final String supportedDeviceType = "VirtualLEDBoard";

    @Override
    public void process(BoardControl msg) {
        System.out.println("Processing control message : " + msg);
    }

    public DeviceRegistrationResponse registerDevice(DeviceRegistrationRequest registration) {
        DeviceUtils.assignDeviceId(registration);
        
        DeviceRegistrationResponse drr = regUtils.registerDevice(registration);
        
        return drr;
    }

    @Override
    public String getSupportedDeviceType() {
        return supportedDeviceType;
    }

    @Override
    public DeviceRequest translateRequest(Event event) throws CannotTranslateEventException {
        // The virtual board should never have to process a request
        // as it cannot originate an event. So this should trigger a failure
        throw new CannotTranslateEventException(supportedDeviceType);
    }

    @Override
    public void handleRequest(DeviceRequest dr) {
        System.out.println("VirtualLedBoard: Handling request: " + dr);
        String lightAddress = dr.getLightAddress();
        String[] devicePinPair = lightAddress.split(":", 2);
        if (devicePinPair.length != 2) {
            System.out.println("Address was " + lightAddress);
            return;
        }
        String requestedDevice = devicePinPair[0];
        String requestedPin = devicePinPair[1];
        PinUpdate pinUpdate = new PinUpdate();
        pinUpdate.setPin(requestedPin);
        pinUpdate.setState(dr.getState());
        
        IoTMessage r = new IoTMessage();
        r.setCommand("update");
        r.setDeviceId(requestedDevice);
        r.setDeviceType(supportedDeviceType);
        r.setEvent(pinUpdate);
        System.out.println("VirtualLEDBoard: About to add message to queue: " + r);
        messageStack.addRequest(r);
    }

}
