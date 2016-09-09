package org.gameontext.iotboard.registration;

import java.util.List;

public class DeviceRegistrationResponse {
    @Override
    public String toString() {
        return "DeviceRegistrationResponse [iotMessagingOrgAndHost=" + iotMessagingOrgAndHost + ", iotMessagingHost="
                + iotMessagingHost + ", iotMessagingPort=" + iotMessagingPort + ", deviceId=" + deviceId
                + ", deviceAuthToken=" + deviceAuthToken + ", iotClientId=" + iotClientId + ", eventTopic=" + eventTopic
                + ", cmdTopic=" + cmdTopic + ", reportedErrors=" + reportedErrors + "]";
    }
    private String iotMessagingOrgAndHost;
    private String iotMessagingHost;
    private int iotMessagingPort;
    private String deviceId;
    private String deviceAuthToken;
    private String iotClientId;
    private String eventTopic;
    private String cmdTopic;
    private List<String> reportedErrors;
    
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
    public void setReportedErrors(List<String> reportedErrors) {
        this.reportedErrors = reportedErrors;
        
    }
    public boolean hasReportedErrors() {
        return this.reportedErrors != null && this.reportedErrors.size() > 0;
    }
    
    public List<String> getViolations() {
        return this.reportedErrors;
    }
    public String getIotMessagingHost() {
        return iotMessagingHost;
    }
    public void setIotMessagingHost(String iotMessagingHost) {
        this.iotMessagingHost = iotMessagingHost;
    }
}
