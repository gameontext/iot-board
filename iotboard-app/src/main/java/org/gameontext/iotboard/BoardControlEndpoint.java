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
import java.net.URI;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.gameontext.iotboard.models.devices.BoardControl;
import org.gameontext.iotboard.provider.BoardProvider;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Root for controlling the state of any registered boards
 */
@Path("/control")
@Api( tags = {"iotboard"})
@Produces(MediaType.APPLICATION_JSON)
public class BoardControlEndpoint {

    @Inject
    protected BoardProviders providers;

    @Context
    protected HttpServletRequest httpRequest;

    private enum AuthMode { AUTHENTICATION_REQUIRED, UNAUTHENTICATED_OK };

    /**
     * GET /iotboard/v1/control
     * @throws JsonProcessingException
     */
    @GET
    //TODO this should be authorised so that only the GameOn board can talk to these endpoints
    //@SignedRequest
    @ApiOperation(value = "List configuration information",
        notes = "Provide detailed information on boards that are currently registered",
        code = HttpURLConnection.HTTP_OK)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getConfig() {

        StringBuilder builder = new StringBuilder("[");
        for(BoardProvider provider : providers.getProviders()) {
            builder.append("{'name':'" + provider.getSupportedDeviceType() + "'}\n");
        }
        builder.append("]");
        return Response.ok(builder.toString()).build();
    }
    
	/**
     * POST /iotboard/v1/control
     * @throws JsonProcessingException
     */
    @POST
    //TODO this should be authorised so that only the GameOn board can talk to these endpoints
    //@SignedRequest
    @ApiOperation(value = "Control a board",
        notes = "Creates an initial status for a light on any boards associated with this GameOn and Site ID",
        code = HttpURLConnection.HTTP_OK)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeDevice(
            @ApiParam(value = "New board attributes", required = true) BoardControl control) {

        for(BoardProvider provider : providers.getProviders()) {
            provider.process(control);
        }
        return Response.ok().build();
    }

    /**
     * PUT /map/v1/sites/:id
     * @throws JsonProcessingException
     */
    @PUT
    //@SignedRequest
    @Path("{id}")
    @ApiOperation(value = "Update a specific device on a board")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRoom(
            @ApiParam(value = "Updated device attributes", required = true) BoardControl control) {

        for(BoardProvider provider : providers.getProviders()) {
            provider.process(control);
        }
        return Response.ok().build();
    }


    /**
     * DELETE /map/v1/sites/:id
     */
    @DELETE
    //@SignedRequest
    @Path("{id}")
    @ApiOperation(value = "Delete a room associated with a device on a board",
        notes = "",
        code = 204 )
    @ApiResponses(value = {
        @ApiResponse(code = 204, message = "Delete successful")
    })
    public Response deleteRoom(
            @ApiParam(value = "target room id", required = true) @PathParam("id") String roomId) {

        //mapRepository.deleteSite(getAuthenticatedId(AuthMode.AUTHENTICATION_REQUIRED), roomId);
        return Response.noContent().build();
    }

    private String getAuthenticatedId(AuthMode mode){
        // This attribute will be set by the auth filter when a user has made
        // an authenticated request.
        String authedId = (String) httpRequest.getAttribute("player.id");
        switch(mode){
            case AUTHENTICATION_REQUIRED:{
                if (authedId == null || authedId.isEmpty()) {
                    //else we don't allow unauthenticated, so if auth id is absent
                    //throw exception to prevent handling the request.
                    throw new BoardModificationException(Response.Status.BAD_REQUEST,
                             "Unauthenticated client", "Room owner could not be determined.");
                }
                break;
            }
            case UNAUTHENTICATED_OK:{
                //if we allow unauthenticated, we will clean up so null==unauthed.
                if(authedId!=null && authedId.isEmpty()){
                    authedId = null;
                }
                break;
            }
        }
        return authedId;

    }

}
