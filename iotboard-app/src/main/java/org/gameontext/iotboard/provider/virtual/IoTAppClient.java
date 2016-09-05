package org.gameontext.iotboard.provider.virtual;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.paho.client.mqttv3.MqttException;

import com.ibm.iotf.client.app.ApplicationClient;

@ApplicationScoped
public class IoTAppClient {

    @Inject
    IoTConfiguration iotConfig;
    private ApplicationClient appclient;
    
    @PostConstruct
    public void init() {
        
        try {
            Properties props = new Properties();
            props.setProperty("id", "iot-webapp");
            props.setProperty("Organization-ID", iotConfig.getIotOrg());
            props.setProperty("Authentication-Method", "apikey");
            props.setProperty("API-Key", iotConfig.getApiKey());
            props.setProperty("Authentication-Token", iotConfig.getApiToken());
            System.out.println("props: " + props);
            appclient = new ApplicationClient(props);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendCommand(String deviceType, String deviceId, String commandId, Object event) {
        try {
            appclient.connect(1);
            System.out.println("Publishing as " + deviceId);
            System.out.println("Did it deliver? " + appclient.publishCommand(deviceType, deviceId, commandId, event));
            appclient.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
        
        
    }
}
