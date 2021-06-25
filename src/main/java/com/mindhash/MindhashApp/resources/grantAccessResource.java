package com.mindhash.MindhashApp.resources;

import com.mindhash.MindhashApp.dao.SessionTokenDao;
import com.mindhash.MindhashApp.dao.UserDao;
import com.mindhash.MindhashApp.dao.accessDao;
import com.mindhash.MindhashApp.model.User;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/access")
public class grantAccessResource {

    @POST
    @Path("/{email}/{recordingid}")
    public static Response giveAccess(@Context ContainerRequestContext request,@PathParam("email") String email, @PathParam("recordingid") int recordingId) {
        String token = request.getHeaderString(HttpHeaders.AUTHORIZATION);
        // only allow admin
        if (SessionTokenDao.checkUserByToken(token) == null | !SessionTokenDao.checkUserByToken(token)) {
            return Response.status(Response.Status.NETWORK_AUTHENTICATION_REQUIRED).entity("NETWORK AUTHENTICATION REQUIRED").build();
        } else {
            accessDao.giveAccess(email, recordingId);
            boolean succesful = true;
            return Response.status(Response.Status.OK).entity(succesful).build();
        }
    }

}
