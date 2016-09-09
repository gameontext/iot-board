package org.gameontext.iotboard.iot;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class MessageStack implements Runnable {
    
    @Inject
    IoTAppClient appClient;
    
    private BlockingQueue<IoTMessage> requests = new ArrayBlockingQueue<IoTMessage>(20);
    private volatile boolean running = false;

    @Override
    public void run() {
        running = true;
        while (running ) {
            try {
                IoTMessage request = requests.take();
                appClient.sendCommand(request.getDeviceType(), request.getDeviceId(), request.getCommand(), request.getEvent());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    
    public void addRequest(IoTMessage r) {
        try {
            while (!requests.offer(r, 10, TimeUnit.SECONDS)) { 
             
                System.out.println("Failed to add message to the queue. Message: " + r);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutDown() {
        running = false;
        
    }

}
