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
package org.gameontext.iotboard.provider.ledboard.physical;

import javax.enterprise.context.ApplicationScoped;

import org.gameontext.iotboard.provider.ledboard.LEDBoardProvider;
import org.gameontext.iotboard.provider.ledboard.Physical;

@ApplicationScoped
@Physical
public class PhysicalLEDBoardProvider extends LEDBoardProvider {
    
    private static final String supportedDeviceType = "PhysicalLEDBoard";

    @Override
    public String getSupportedDeviceType() {
        return supportedDeviceType;
    }

}
