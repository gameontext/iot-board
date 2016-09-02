package org.gameontext.iotboard.provider.virtual;

public class IoTRegistrationResponse {
    private String clientId;
    private String typeId;
    private String deviceId;
    private String authToken;
    
    @Override
    public String toString() {
        return "RegistrationResponse [clientId=" + clientId + ", typeId=" + typeId + ", deviceId=" + deviceId
                + ", authToken=" + authToken + "]";
    }
    public String getClientId() {
        return clientId;
    }
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
    public String getTypeId() {
        return typeId;
    }
    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public String getAuthToken() {
        return authToken;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    

}
