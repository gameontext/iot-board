package org.gameontext.iotboard.provider.virtual;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.gameontext.iotboard.iot.DeviceCallback;

import com.ibm.iotf.client.app.ApplicationClient;

@ApplicationScoped
public class IoTAppClient {

    @Inject
    IoTConfiguration iotConfig;
    
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
            appclient.setEventCallback(new DeviceCallback());
            appclient.subscribeToDeviceEvents();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Application client created");
    }

    @PreDestroy
    public void tearDown() {
        appclient.disconnect();
    }
    
    public void sendCommand(String deviceType, String deviceId, String commandId, Object event) {
        System.out.println("Publishing as " + deviceId);
        System.out.println("Did it deliver? " + appclient.publishCommand(deviceType, deviceId, commandId, event));
    }
}
