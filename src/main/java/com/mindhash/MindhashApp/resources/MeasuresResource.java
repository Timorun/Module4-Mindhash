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

import com.mindhash.MindhashApp.dao.MeasureDao;
import com.mindhash.MindhashApp.dao.SessionTokenDao;
import com.mindhash.MindhashApp.dao.AccessDao;
import com.mindhash.MindhashApp.model.User;

@Path("/measurements")
public class MeasuresResource {
    
    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getObjects(@QueryParam("rid") int recordingId,
    							@QueryParam("date") String date,
    							@QueryParam("time") String time,
    							@Context ContainerRequestContext request) {
    	String token = request.getHeaderString(HttpHeaders.AUTHORIZATION);
    	User user = SessionTokenDao.getUserByToken(token);
		if (user.getEmail() == null) {
			return Response
					.status(Response.Status.NETWORK_AUTHENTICATION_REQUIRED)
					.entity("NETWORK AUTHENTICATION REQUIRED")
					.build();
		} else {
			if (AccessDao.getRecordingById(token, recordingId) || user.getIsadmin()) {
				return Response
						.status(Response.Status.OK)
						.entity(MeasureDao.getMeasurementByTime(recordingId, date, time))
						.build();
			} else {
				return Response
						.status(Response.Status.UNAUTHORIZED)
						.entity("UNAUTHORIZED")
						.build();
			}
		}
    }
}

