package org.gameontext.iotboard.provider.room;

public class Data {
    
    private String lightId;
    private boolean lightState;

    public boolean getLightState() {
        return lightState;
    }

    public void setLightState(boolean lightState) {
        this.lightState = lightState;
    }

    public String getLightId() {
        return lightId;
    }

    public void setLightId(String lightId) {
        this.lightId = lightId;
    }

}
