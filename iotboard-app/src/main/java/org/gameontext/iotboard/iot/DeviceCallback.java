package org.gameontext.iotboard.iot;

import java.io.ByteArrayInputStream;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonStructure;

import com.ibm.iotf.client.app.Command;
import com.ibm.iotf.client.app.Event;
import com.ibm.iotf.client.app.EventCallback;

public class DeviceCallback implements EventCallback {

    @Override
    public void processCommand(Command arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void processEvent(Event event) {
        System.out.println("Got event: " + event.getDeviceType() + "/" + event.getDeviceId() + ": " + event.getPayload());
        ByteArrayInputStream bais = new ByteArrayInputStream (event.getPayload().getBytes());
        JsonReader rdr = Json.createReader(bais);
        JsonStructure json = rdr.read();
        System.out.println("Got payload " + json);
        JsonObject data = ((JsonObject) json).getJsonObject("d");
        
        
        
        
        
    }

}
