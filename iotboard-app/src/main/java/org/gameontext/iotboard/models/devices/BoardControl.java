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

//top level element for controlling the board state
public class BoardControl {
    private String gameonId;
    private String siteId;
    private DeviceData data;
    
    public BoardControl(){}
       
    public BoardControl(String gameonId, String siteId) {
        super();
        this.gameonId = gameonId;
        this.siteId = siteId;
    }

    public String getGameonId() {
        return gameonId;
    }
    public void setGameonId(String gameonId) {
        this.gameonId = gameonId;
    }
    public String getSiteId() {
        return siteId;
    }
    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
    public DeviceData getData() {
        return data;
    }
    public void setData(DeviceData data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BoardControl [gameonId=" + gameonId + ", siteId=" + siteId + ", data=" + data + "]";
    }
}
