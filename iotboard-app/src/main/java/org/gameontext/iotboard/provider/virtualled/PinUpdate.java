package org.gameontext.iotboard.provider.virtualled;

public class PinUpdate {

    private String pin;
    private boolean state;
    public boolean isState() {
        return state;
    }
    public void setState(boolean state) {
        this.state = state;
    }
    public String getPin() {
        return pin;
    }
    public void setPin(String pin) {
        this.pin = pin;
    }
}
