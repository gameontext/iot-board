package org.gameontext.iotboard.iot;

import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.ibm.iotf.client.app.ApplicationClient;

@ApplicationScoped
public class IoTAppClient {
    
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    @Inject
    MessageStack messageStack;
    
    
    @Inject
    IoTConfiguration iotConfig;
    
    @Inject
    DeviceCallback callback;
    
    private ApplicationClient appclient;
    
    @PostConstruct
    public void init() {
        System.out.println("Creating application client");
        try {
            Properties props = new Properties();
            props.setProperty("id", "iot-webapp");
            props.setProperty("Organization-ID", iotConfig.getIotOrg());
            props.setProperty("Authentication-Method", "apikey");
            props.setProperty("API-Key", iotConfig.getApiKey());
            props.setProperty("Authentication-Token", iotConfig.getApiToken());
            System.out.println("props: " + props);
            appclient = new ApplicationClient(props);
            appclient.connect(1);
            appclient.setEventCallback(callback);
            appclient.subscribeToDeviceEvents();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Application client created");


        scheduler.schedule(messageStack, 0, TimeUnit.NANOSECONDS);
    }

    @PreDestroy
    public void tearDown() {
        messageStack.shutDown();
        appclient.disconnect();
    }
    
    public void sendCommand(String deviceType, String deviceId, String commandId, Object event) {
        System.out.println("Publishing as " + deviceId);
        System.out.println("Did it deliver? " + appclient.publishCommand(deviceType, deviceId, commandId, event));
        System.out.println("Done");
    }
}
