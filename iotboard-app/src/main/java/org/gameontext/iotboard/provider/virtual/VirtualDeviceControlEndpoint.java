/*******************************************************************************
 * Copyright (c) 2016 IBM Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.gameontext.iotboard.provider.virtual;

import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.gameontext.iotboard.models.DeviceRegistration;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Root for controlling the state of any registered boards
 */
@Path("/devices")
@Api( tags = {"iotboard"})
@Produces(MediaType.APPLICATION_JSON)
public class VirtualDeviceControlEndpoint {

    @Inject
    protected VirtualBoardProvider provider;

    @POST
    @Path("test")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response check(String deviceToHit) throws MqttException {
        provider.trigger(deviceToHit);
        return Response.ok(deviceToHit).build();
    }
    
	/**
     * POST /iotboard/v1/control
     * @throws JsonProcessingException
     */
    @POST
    //@SignedRequest
    @ApiOperation(value = "Control a board",
        notes = "Creates an initial status for a light on any boards associated with this GameOn and Site ID",
        code = HttpURLConnection.HTTP_OK)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerDevice(
            @ApiParam(value = "Details of registering device", required = true) DeviceRegistration registration, @Context HttpServletRequest request) {

        HttpSession session = request.getSession();
        System.out.println("Registering device: " + registration.getDeviceId());
        DeviceRegistrationResponse drr = provider.registerDevice(registration);
        return Response.ok(drr).build();
    }
}
