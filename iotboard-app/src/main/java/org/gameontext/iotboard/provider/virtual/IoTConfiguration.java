package org.gameontext.iotboard.provider.virtual;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IoTConfiguration {
    private String iotOrg = "pmoxqf";
    private String iotApiHost = "internetofthings.ibmcloud.com";
    private String iotMessagingHost = "messaging.internetofthings.ibmcloud.com";
    private int iotMessagingPort = 1883;

    public String getIotOrg() {
        return iotOrg;
    }

    public String getIotApiHost() {
        return iotApiHost;
    }

    public String getDeviceRegistrationUrl(String deviceType) {
        return "https://" + iotOrg + "." + iotApiHost + "/api/v0002/device/types/" + deviceType + "/devices";
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

    public String getAuthHeader() {
        try {
            String authString = getApiKey() + ":" + getApiToken();
            String basicAuthEncoded = Base64.getEncoder().encodeToString(authString.getBytes("UTF-8"));
            return "Basic " + basicAuthEncoded;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
