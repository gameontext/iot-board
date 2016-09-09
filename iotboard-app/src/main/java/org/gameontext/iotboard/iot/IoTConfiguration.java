package org.gameontext.iotboard.iot;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class IoTConfiguration {
    private String iotOrg;
    private String iotApiHost;
    private String iotMessagingHost;
    private int iotMessagingPort;
    private boolean mandatoryConfigProvided = true;
    private String iotApiKey;
    private String iotApiToken;
    
    @PostConstruct
    public void init() {
        // Pull the config form the environment
        iotOrg = getenv("IOT_ORG");
        iotApiHost = getenv("IOT_API_HOST", "internetofthings.ibmcloud.com");
        iotMessagingHost = getenv("IOT_MESSAGING_HOST", "messaging.internetofthings.ibmcloud.com");
        iotMessagingPort = Integer.parseInt(getenv("IOT_MESSAGING_PORT", "1883"));
        iotApiKey = getenv("IOT_API_KEY");
        iotApiToken = getenv("IOT_API_TOKEN");
    }

    private String getenv(String envName, String defaultValue) {
        String returnedValue = System.getenv(envName);
        if (didntProvide(envName) && didntProvide(defaultValue)) {
            mandatoryConfigProvided = false;
        }
        return returnedValue;
    }

    private boolean didntProvide(String defaultValue) {
        return defaultValue == null || defaultValue.trim().length() == 0;
    }

    private String getenv(String envName) {
        return getenv(envName, null);
    }

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
        return iotApiKey;
    }

    public String getApiToken() {
        return iotApiToken;
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

    public boolean isMandatoryConfigProvided() {
        return mandatoryConfigProvided;
    }

}
