package org.gameontext.iotboard.provider.virtual;

import java.util.List;

public class DeviceRegistrationResponse {
    private String iotMessagingOrgAndHost; //"pmoxqf" + ".messaging.internetofthings.ibmcloud.com";
    private int iotMessagingPort; // 1883;
    private String deviceId; //"RossPhone";
    private String deviceAuthToken; //"12345678";
    private String iotClientId; //"d:"+"pmoxqf"+":iot-phone:"+window.deviceId;
    private String eventTopic;
    private String cmdTopic;
    private List<String> violations;
    
    public String getIotMessagingOrgAndHost() {
        return iotMessagingOrgAndHost;
    }
    public void setIotMessagingOrgAndHost(String iotMessagingOrgAndHost) {
        this.iotMessagingOrgAndHost = iotMessagingOrgAndHost;
    }
    public int getIotMessagingPort() {
        return iotMessagingPort;
    }
    public void setIotMessagingPort(int iotMessagingPort) {
        this.iotMessagingPort = iotMessagingPort;
    }
    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public String getDeviceAuthToken() {
        return deviceAuthToken;
    }
    public void setDeviceAuthToken(String deviceAuthToken) {
        this.deviceAuthToken = deviceAuthToken;
    }
    public String getIotClientId() {
        return iotClientId;
    }
    public void setIotClientId(String iotClientId) {
        this.iotClientId = iotClientId;
    }
    public String getEventTopic() {
        return eventTopic;
    }
    public void setEventTopic(String eventTopic) {
        this.eventTopic = eventTopic;
    }
    public String getCmdTopic() {
        return cmdTopic;
    }
    public void setCmdTopic(String cmdTopic) {
        this.cmdTopic = cmdTopic;
    }
    public void setViolations(List<String> violations) {
        this.violations = violations;
        
    }
    public boolean hasViolations() {
        return this.violations != null && this.violations.size() > 0;
    }
    
    public List<String> getViolations() {
        return this.violations;
    }
}
