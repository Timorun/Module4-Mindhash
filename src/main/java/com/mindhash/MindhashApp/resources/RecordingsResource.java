package com.mindhash.MindhashApp.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mindhash.MindhashApp.dao.RecordingDao;
import com.mindhash.MindhashApp.dao.SessionTokenDao;
import com.mindhash.MindhashApp.model.Recording;

@Path("/recordings")
public class RecordingsResource {
	
	/*@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<Recording> getRecordings(@Context ContainerRequestContext request) {
		String token = request.getHeaderString(HttpHeaders.AUTHORIZATION);
		List<Recording> recoridings = new ArrayList<Recording>();
		if (SessionTokenDao.checkUserByToken(token) == null) {
			
		}
		recoridings.addAll(RecordingDao.instance.getModel().values());
		return recoridings;
	}*/
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response getRecordings(@Context ContainerRequestContext request) {
		
		String token = request.getHeaderString(HttpHeaders.AUTHORIZATION);
		if (SessionTokenDao.checkUserByToken(token) == null) {
			return Response
					.status(Response.Status.NETWORK_AUTHENTICATION_REQUIRED)
					.entity("NETWORK AUTHENTICATION REQUIRED")
					.build();
		} else {
			List<Recording> recoridings = new ArrayList<Recording>();
			recoridings.addAll(RecordingDao.instance.getModel().values());
			return Response
					.status(Response.Status.OK)
					.entity(recoridings)
					.build();
		}
		
	}

}

