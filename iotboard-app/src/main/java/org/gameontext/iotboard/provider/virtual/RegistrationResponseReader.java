package org.gameontext.iotboard.provider.virtual;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class RegistrationResponseReader implements MessageBodyReader<RegistrationResponse> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type.equals(RegistrationResponse.class);
    }

    @Override
    public RegistrationResponse readFrom(Class<RegistrationResponse> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {
        JsonReader rdr = null; 
        try {
             rdr = Json.createReader(entityStream);
             JsonObject attendeeJson = rdr.readObject();
             JsonString authToken = attendeeJson.getJsonString("authToken");
             JsonString typeId = attendeeJson.getJsonString("typeId");
             JsonString deviceId = attendeeJson.getJsonString("deviceId");
             JsonString clientId = attendeeJson.getJsonString("clientId");
             RegistrationResponse attendee = new RegistrationResponse();
             attendee.setAuthToken(authToken.getString());
             attendee.setClientId(clientId.getString());
             attendee.setDeviceId(deviceId.getString());
             attendee.setTypeId(typeId.getString());
             return attendee;
        } finally {
            if (rdr != null) {
                rdr.close();
            }
        }
    }

}
