package org.gameontext.iotboard.provider.virtual;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.json.JsonValue;
import javax.ws.rs.Consumes;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

@ApplicationScoped
@Provider
@Consumes(MediaType.APPLICATION_JSON)
public class RegistrationResponseReader implements MessageBodyReader<IoTRegistrationResponse> {

    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return type.equals(IoTRegistrationResponse.class);
    }

    @Override
    public IoTRegistrationResponse readFrom(Class<IoTRegistrationResponse> type, Type genericType, Annotation[] annotations,
            MediaType mediaType, MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
            throws IOException, WebApplicationException {
        JsonReader rdr = null; 
        try {
             rdr = Json.createReader(entityStream);
             JsonStructure json = rdr.read();
             JsonObject returnedJson = (JsonObject) json;
             
             JsonArray reportedErrors = returnedJson.getJsonArray("violations");
             
             IoTRegistrationResponse attendee = processReportedErrors(reportedErrors);
             
             JsonString authToken = returnedJson.getJsonString("authToken");
             JsonString typeId = returnedJson.getJsonString("typeId");
             JsonString deviceId = returnedJson.getJsonString("deviceId");
             JsonString clientId = returnedJson.getJsonString("clientId");
             attendee.setAuthToken(sanitiseNull(authToken));
             attendee.setClientId(sanitiseNull(clientId));
             attendee.setDeviceId(sanitiseNull(deviceId));
             attendee.setTypeId(sanitiseNull(typeId));
             
             return attendee;
        } finally {
            if (rdr != null) {
                rdr.close();
            }
        }
    }

    private String sanitiseNull(JsonString jsonString) {
        return (jsonString == null) ? null : jsonString.getString();
    }

    private IoTRegistrationResponse processReportedErrors(JsonArray reportedErrors) {
        IoTRegistrationResponse response = new IoTRegistrationResponse();
        if (reportedErrors != null) {
            for (JsonValue jsonValue : reportedErrors) {
                JsonObject violation = (JsonObject) jsonValue;
                response.addReportedError(violation.getString("message"));
            }
        }
        return response;
        
    }

}
