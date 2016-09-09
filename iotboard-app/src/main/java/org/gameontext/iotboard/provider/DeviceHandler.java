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
package org.gameontext.iotboard.provider;

import org.gameontext.iotboard.models.devices.BoardControl;
import org.gameontext.iotboard.registration.DeviceRegistrationRequest;
import org.gameontext.iotboard.registration.DeviceRegistrationResponse;

import com.ibm.iotf.client.app.Event;

/**
 * Implemented by board providers
 *
 */
public interface DeviceHandler {
    
    /**
     * Process a control message to set the status
     * of a device.
     * 
     * @param msg control message to process
     */
    public void process(BoardControl msg);

    public String getSupportedDeviceType();

    public DeviceRegistrationResponse registerDevice(DeviceRegistrationRequest registration);

    public DeviceRequest translateRequest(Event event) throws CannotTranslateEventException;

    public void handleRequest(DeviceRequest dr);
}
