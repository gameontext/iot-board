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
package org.gameontext.iotboard;

import java.net.HttpURLConnection;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import org.gameontext.iotboard.models.devices.DeviceHandler;
import org.gameontext.iotboard.registration.DeviceRegistrationRequest;
import org.gameontext.iotboard.registration.DeviceRegistrationResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Root for controlling the state of any registered boards
 */
@Path("/devices")
@Api( tags = {"iotboard"})
@Produces(MediaType.APPLICATION_JSON)
public class DeviceControlEndpoint {

//    @Inject
//    protected VirtualBoardProvider provider;
    
    @Inject
    protected BoardProviders providers;

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
            @ApiParam(value = "Details of registering device", required = true) DeviceRegistrationRequest registration, @Context HttpServletRequest request) {
        System.out.println("New device registration request: " + registration);
        // Register Device
        // Add subscription to the list
        
        
        System.out.println("Registering device: " + registration.getDeviceId());
        DeviceHandler provider = providers.getProvider(registration.getDeviceType());
        DeviceRegistrationResponse drr = provider.registerDevice(registration);
        if (drr.hasReportedErrors()) {
            return Response.status(400).entity(drr).build();
        }
        return Response.ok(drr).build();
    }
}
