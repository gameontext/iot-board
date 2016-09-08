package org.gameontext.iotboard.iot;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.gameontext.iotboard.BoardProviders;
import org.gameontext.iotboard.models.devices.DeviceHandler;

import com.ibm.iotf.client.app.Command;
import com.ibm.iotf.client.app.Event;
import com.ibm.iotf.client.app.EventCallback;

@ApplicationScoped
public class DeviceCallback implements EventCallback {
    
    @Inject
    BoardProviders boardProviders;

    @Override
    public void processCommand(Command command) {
        System.out.println("Got process copmmand: " + command.getCommand());

    }

    @Override
    public void processEvent(Event event) {
        System.out.println("Got event: " + event.getDeviceType() + "/" + event.getDeviceId() + ": " + event.getPayload());
        
        String deviceType = event.getDeviceType();
        DeviceHandler handlingProvider = boardProviders.getProvider(deviceType);
        DeviceRequest dr = handlingProvider.translateRequest(event);
        
        for (DeviceHandler boardProvider : boardProviders.getProviders()) {
            boardProvider.handleRequest(dr);
        }
        
        System.out.println("Event handled");
        
    }

}
