package com.mindhash.MindhashApp.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mindhash.MindhashApp.dao.MeasureDao;
import com.mindhash.MindhashApp.dao.SessionTokenDao;

@Path("/measurements")
public class MeasuresResource {

    /*@GET
	@Path("{recordingId}/{date}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public MeasureRes getObjects(@PathParam("recordingId") int recordingId,
    								@PathParam("date") String date) {
        return MeasureDao.getMeasurement(recordingId, date);
    }*/
    
    @GET
	@Path("{recordingId}/{date}/{time}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Response getObjects(@PathParam("recordingId") int recordingId,
    							@PathParam("date") String date,
    							@PathParam("time") String time,
    							@Context ContainerRequestContext request) {
    	String token = request.getHeaderString(HttpHeaders.AUTHORIZATION);
		if (SessionTokenDao.checkUserByToken(token) == null) {
			return Response.status(Response.Status.NETWORK_AUTHENTICATION_REQUIRED).entity("NETWORK AUTHENTICATION REQUIRED").build();
		} else {
			return Response.status(Response.Status.OK).entity(MeasureDao.getMeasurementByTime(recordingId, date, time)).build();
		}
    }
}

