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
package org.gameontext.iotboard.models.devices;

//control the state of lights on the IoT board
public class DeviceData {
    private String light;
    private boolean state;
    
    public DeviceData(){}

    public DeviceData(String light, boolean state) {
        super();
        this.light = light;
        this.state = state;
    }

    public String getLight() {
        return light;
    }
    public void setLight(String light) {
        this.light = light;
    }
    public boolean isState() {
        return state;
    }
    public void setState(boolean state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "DeviceData [light=" + light + ", state=" + state + "]";
    }
}
