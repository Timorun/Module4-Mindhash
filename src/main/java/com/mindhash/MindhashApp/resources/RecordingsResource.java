package com.mindhash.MindhashApp.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mindhash.MindhashApp.dao.RecordingDao;
import com.mindhash.MindhashApp.dao.SessionTokenDao;

@Path("/recordings")
public class RecordingsResource {
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getRecordings(@Context ContainerRequestContext request,
								@QueryParam("pageNum") int pageNum) {
		
		String token = request.getHeaderString(HttpHeaders.AUTHORIZATION);
		if (SessionTokenDao.getUserByToken(token).getEmail() == null) {
			return Response
					.status(Response.Status.NETWORK_AUTHENTICATION_REQUIRED)
					.entity("NETWORK AUTHENTICATION REQUIRED")
					.build();
		} else {
			return Response
					.status(Response.Status.OK)
					.entity(RecordingDao.getRecordings(token, pageNum))
					.build();
		}
		
	}
	
	@GET
	@Path("/all")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getAllRecordings(@Context ContainerRequestContext request) {
		
		String token = request.getHeaderString(HttpHeaders.AUTHORIZATION);
		return Response
				.status(Response.Status.OK)
				.entity(RecordingDao.getAllRecordings(token))
				.build();
	}

}

