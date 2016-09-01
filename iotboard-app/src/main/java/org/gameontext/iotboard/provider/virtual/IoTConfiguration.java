package org.gameontext.iotboard.provider.virtual;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IoTConfiguration {
    private String iotOrg = "pmoxqf";
    private String iotApiHost = "internetofthings.ibmcloud.com";
    private String iotMessagingHost = "messaging.internetofthings.ibmcloud.com";
    private int iotMessagingPort = 1883;
    private String deviceType = "VirtualDevice";
    
    public String getIotOrg() {
        return iotOrg;
    }
    public String getIotApiHost() {
        return iotApiHost;
    }
    
    public String deviceRegistartionUrl() {
        return "https://"+iotOrg + "." + iotApiHost+ "/api/v0002/device/types/"+ deviceType + "/devices";
    }
    
    public String getiotMessagingOrgAndHost() {
        return iotOrg + "." + iotMessagingHost;
    }

    public String getApiKey() {
                return "a-pmoxqf-g2wyv0orrf";
    }
    public String getApiToken() {
        return "qz(8+qbVNdLDfED0si";
    }
    public String getIotMessagingHost() {
        return iotMessagingHost;
    }
    public int getIotMessagingPort() {
        return iotMessagingPort;
    }
}
