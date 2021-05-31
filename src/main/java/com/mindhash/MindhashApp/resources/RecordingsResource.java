package com.mindhash.MindhashApp.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import com.mindhash.MindhashApp.dao.RecordingDao;
import com.mindhash.MindhashApp.model.Recording;

@Path("/recordings")
public class RecordingsResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public List<Recording> getBikes() {
		List<Recording> bikes = new ArrayList<Recording>();
		bikes.addAll(RecordingDao.instance.getModel().values());
		return bikes;
	}
}

