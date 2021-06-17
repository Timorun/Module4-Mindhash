package com.mindhash.MindhashApp.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.mindhash.MindhashApp.dao.MeasureDao;
import com.mindhash.MindhashApp.model.MeasureRes;

@Path("/measurements")
public class MeasuresResource {

    @GET
	@Path("{recordingId}/{date}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public MeasureRes getObjects(@PathParam("recordingId") int recordingId,
    								@PathParam("date") String date) {
        return MeasureDao.getMeasurement(recordingId, date);
    }
}

