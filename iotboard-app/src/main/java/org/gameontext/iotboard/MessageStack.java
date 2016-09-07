package org.gameontext.iotboard;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.gameontext.iotboard.provider.virtual.IoTAppClient;

@ApplicationScoped
public class MessageStack implements Runnable {
    
    @Inject
    IoTAppClient appClient;
    
    private BlockingQueue<Request> requests = new ArrayBlockingQueue<Request>(20);
    private volatile boolean running = false;

    @Override
    public void run() {
        running = true;
        while (running ) {
            try {
                Request request = requests.take();
                appClient.sendCommand(request.getDeviceType(), request.getDeviceId(), request.getCommand(), request.getEvent());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    
    public void addRequest(Request r) {
        try {
            while (!requests.offer(r, 10, TimeUnit.SECONDS));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void shutDown() {
        running = false;
        
    }

}
