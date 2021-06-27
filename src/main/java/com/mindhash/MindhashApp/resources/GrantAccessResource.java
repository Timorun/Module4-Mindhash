package com.mindhash.MindhashApp.resources;

import com.mindhash.MindhashApp.dao.SessionTokenDao;
import com.mindhash.MindhashApp.dao.AccessDao;
import com.mindhash.MindhashApp.model.User;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

@Path("/access")
public class GrantAccessResource {

    @POST
    @Path("/{email}/{recordingid}")
    public static Response giveAccess(@Context ContainerRequestContext request, @PathParam("email") String email, @PathParam("recordingid") int recordingId) {
        String token = request.getHeaderString(HttpHeaders.AUTHORIZATION);
        // only allow admin
        if (SessionTokenDao.getUserByToken(token).getIsadmin()) {
        	AccessDao.giveAccess(email, recordingId);
            return Response
            		.status(Response.Status.OK)
            		.entity(true)
            		.build();
        } else {
            return Response
            		.status(Response.Status.NETWORK_AUTHENTICATION_REQUIRED)
            		.entity("NETWORK AUTHENTICATION REQUIRED")
            		.build();
        }
    }

    @GET
    @Path("/{recordingid}")
    public static Response checkAccess(@Context ContainerRequestContext request, @PathParam("recordingid") int recordingId) {
        String token = request.getHeaderString(HttpHeaders.AUTHORIZATION);
        User user = SessionTokenDao.getUserByToken(token);
        if (user.getEmail() == null) {
            return Response
            		.status(Response.Status.NETWORK_AUTHENTICATION_REQUIRED)
            		.entity("NETWORK AUTHENTICATION REQUIRED")
            		.build();
        } else {
            boolean succesful;
            if (AccessDao.getRecordingById(token, recordingId) || user.getIsadmin()) {
                succesful = true;
            } else {
                succesful = false;
            }
            return Response
            		.status(Response.Status.OK)
            		.entity(succesful)
            		.build();
        }
    }

}
