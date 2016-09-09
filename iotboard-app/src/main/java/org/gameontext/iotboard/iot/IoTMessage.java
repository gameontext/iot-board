package org.gameontext.iotboard.iot;

public class IoTMessage {

    @Override
    public String toString() {
        return "IoTMessage [event=" + event + ", command=" + command + ", deviceId=" + deviceId + ", deviceType="
                + deviceType + "]";
    }
    private Object event;
    private String command;
    private String deviceId;
    private String deviceType;
    public Object getEvent() {
        return event;
    }
    public void setEvent(Object event) {
        this.event = event;
    }
    public String getCommand() {
        return command;
    }
    public void setCommand(String command) {
        this.command = command;
    }
    public String getDeviceId() {
        return deviceId;
    }
    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
    public String getDeviceType() {
        return deviceType;
    }
    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }


}
