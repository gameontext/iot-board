package org.gameontext.iotboard;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Response;

import org.gameontext.iotboard.iot.DeviceUtils;
import org.gameontext.iotboard.models.DeviceRegistrationRequest;
import org.gameontext.iotboard.provider.virtual.DeviceRegistrationResponse;
import org.gameontext.iotboard.provider.virtual.IoTConfiguration;
import org.gameontext.iotboard.provider.virtual.IoTReg;
import org.gameontext.iotboard.provider.virtual.IoTRegistrationResponse;
import org.gameontext.iotboard.provider.virtual.RegistrationResponseReader;

@ApplicationScoped
public class RegistrationUtils {

    @Inject
    IoTConfiguration ioTConfig;
    
    public DeviceRegistrationResponse registerDevice(DeviceRegistrationRequest registration) {
        IoTRegistrationResponse rr = registerIntoIoTF(registration);
        
        DeviceRegistrationResponse drr = new DeviceRegistrationResponse();

        if (rr.hasReportedErrors())  {
            drr.setReportedErrors(rr.getreportedErrors());
            return drr;
        }
        
        drr.setIotMessagingOrgAndHost(ioTConfig.getiotMessagingOrgAndHost());
        drr.setIotMessagingPort(ioTConfig.getIotMessagingPort());
        drr.setDeviceId(rr.getDeviceId());
        drr.setDeviceAuthToken(rr.getAuthToken());
        drr.setIotClientId(rr.getClientId());
        drr.setEventTopic("iot-2/evt/request/fmt/json");
        drr.setCmdTopic("iot-2/cmd/+/fmt/json");
        return drr;
    }

    private IoTRegistrationResponse registerIntoIoTF(DeviceRegistrationRequest registration) {
        Client client = ClientBuilder.newClient().register(RegistrationResponseReader.class);
        
        WebTarget target = client.target(ioTConfig.getDeviceRegistrationUrl(registration.getDeviceType()));
        Builder requestBuilder = target.request();
        
        requestBuilder.header("Authorization", ioTConfig.getAuthHeader());
        System.out.println("Registering device with ID " + registration.getDeviceId());
        
        IoTReg iotReg = new IoTReg(registration.getDeviceId(), DeviceUtils.generateAlphaNum());
        
        Response response = requestBuilder.post(Entity.json(iotReg));
        System.out.println("Got back: " + response.getStatus());
        
        IoTRegistrationResponse rr = response.readEntity(IoTRegistrationResponse.class);
        
        System.out.println("Returned payload: " + rr);
        return rr;
    }
    
}
