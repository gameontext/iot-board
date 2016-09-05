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
package org.gameontext.iotboard;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.gameontext.iotboard.provider.BoardProvider;
import org.gameontext.iotboard.provider.virtual.VirtualBoardProvider;


/**
 * Management for all the different board providers.
 * This service provides a virtual board service, others may be configured
 * at a later point in time.
 */

@ApplicationScoped
public class BoardProviders {
    
    @Inject
    VirtualBoardProvider vbp;
    
    private final ConcurrentMap<String, BoardProvider> providers = new ConcurrentHashMap<>();
    
    public Collection<BoardProvider> getProviders() {
        return providers.values();
    }
    
    //TODO make thread safe and lock access to the sites under this registration
    public void deleteRegistration(BoardProvider provider) {
        providers.remove(provider.getSupportedDeviceType());
    }
    
    //add a new site registration or update an existing one
    public void addRegistration(BoardProvider provider) {
        providers.putIfAbsent(provider.getSupportedDeviceType(), provider);
    }
    
    public BoardProvider getProvider(String deviceType) {
        return providers.get(deviceType);
    }
    
    @PostConstruct
    public void init() {
        //pre-load with a virtual board services
        addRegistration(vbp);
    }
}
