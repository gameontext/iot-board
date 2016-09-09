package org.gameontext.iotboard.iot;

import java.util.Collection;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.gameontext.iotboard.provider.BoardProviders;
import org.gameontext.iotboard.provider.CannotTranslateEventException;
import org.gameontext.iotboard.provider.DeviceHandler;
import org.gameontext.iotboard.provider.DeviceRequest;
import org.gameontext.iotboard.provider.room.InvalidRequestException;
import org.gameontext.iotboard.provider.room.RoomProvider;

import com.ibm.iotf.client.app.Command;
import com.ibm.iotf.client.app.Event;
import com.ibm.iotf.client.app.EventCallback;

@ApplicationScoped
public class DeviceCallback implements EventCallback {
    
    @Inject
    BoardProviders boardProviders;
    
    @Inject
    RoomProvider roomProvider;

    @Override
    public void processCommand(Command command) {
        System.out.println("Got process copmmand: " + command.getCommand());
    }

    @Override
    public void processEvent(Event event) {
        try {
        System.out.println("Got event: " + event.getDeviceType() + "/" + event.getDeviceId() + ": " + event.getPayload());
        
        String deviceType = event.getDeviceType();
        DeviceHandler handlingProvider = boardProviders.getProvider(deviceType);
        DeviceRequest dr;
        try {
            dr = handlingProvider.translateRequest(event);
            // Validate that:
            // - The room still supports the notion that this light is for this room
            roomProvider.validateRequest(dr);
            
            // Other players interested in the light you are interested in are:
            Collection<DeviceRequest> allInterestedParties = roomProvider.getInterestedRooms(dr);
            for (DeviceRequest deviceRequest : allInterestedParties) {
                for (DeviceHandler boardProvider : boardProviders.getProviders()) {
                    boardProvider.handleRequest(deviceRequest);
                }    
            }
            
            
        } catch (CannotTranslateEventException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        }
        
        System.out.println("Event handled");
        } catch (Exception e) {
            System.out.println("Callback threw an exception");
            e.printStackTrace();
            throw e;
        }
    }

}
