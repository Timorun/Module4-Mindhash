package com.mindhash.MindhashApp.resources;

import com.mindhash.MindhashApp.dao.ObjectDao;
import com.mindhash.MindhashApp.dao.SessionTokenDao;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/objects")
public class ObjectsResource {

    @GET
    @Path("{recordingId}/{date}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getObjects(@PathParam("recordingId") int recordingId,
    							@PathParam("date") String date,
    							@Context ContainerRequestContext request) {
    	
    	String token = request.getHeaderString(HttpHeaders.AUTHORIZATION);
		if (SessionTokenDao.checkUserByToken(token) == null) {
			return Response
					.status(Response.Status.NETWORK_AUTHENTICATION_REQUIRED)
					.entity("NETWORK AUTHENTICATION REQUIRED")
					.build();
		} else {
			return Response
					.status(Response.Status.OK)
					.entity(ObjectDao.getObjectNum(recordingId, date))
					.build();
		}
		
    }
    
}
