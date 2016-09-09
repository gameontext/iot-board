package org.gameontext.iotboard.provider;

public class CannotTranslateEventException extends Exception {

    private static final long serialVersionUID = -4309915388801665053L;

    public CannotTranslateEventException(String supporteddevicetype) {
        super(supporteddevicetype);
    }

}
