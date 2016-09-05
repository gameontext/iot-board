package org.gameontext.iotboard.provider.virtual;

import java.util.ArrayList;
import java.util.List;

public class Devices {

    private List<String> devices = new ArrayList<String>();

    public void add(String deviceId) {  
        devices.add(deviceId);
        
    }

    public List<String> getDevices() {
        return devices;
    }

}
