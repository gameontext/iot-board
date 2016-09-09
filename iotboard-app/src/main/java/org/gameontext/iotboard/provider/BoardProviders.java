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

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.gameontext.iotboard.provider.browser.VirtualBoardProvider;
import org.gameontext.iotboard.provider.ledboard.LEDBoardProvider;
import org.gameontext.iotboard.provider.ledboard.Virtual;
import org.gameontext.iotboard.provider.room.RoomProvider;


/**
 * Management for all the different board providers.
 * This service provides a virtual board service, others may be configured
 * at a later point in time.
 */

@ApplicationScoped
public class BoardProviders {
    
    @Inject
    VirtualBoardProvider vbp;
    
    @Inject @Virtual
    LEDBoardProvider velbp;
    
    @Inject
    RoomProvider rp;
    
    private final ConcurrentMap<String, DeviceHandler> providers = new ConcurrentHashMap<>();
    
    public Collection<DeviceHandler> getProviders() {
        return providers.values();
    }
    
    //TODO make thread safe and lock access to the sites under this registration
    public void deleteRegistration(DeviceHandler provider) {
        providers.remove(provider.getSupportedDeviceType());
    }
    
    //add a new site registration or update an existing one
    public void addRegistration(DeviceHandler provider) {
        providers.putIfAbsent(provider.getSupportedDeviceType(), provider);
    }
    
    public DeviceHandler getProvider(String deviceType) {
        DeviceHandler provider = providers.get(deviceType); 
        return provider;
    }
    
    @PostConstruct
    public void init() {
        //pre-load with a virtual board services
        addRegistration(vbp);
        addRegistration(velbp);
        addRegistration(rp);
    }
}
