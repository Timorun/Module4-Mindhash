package com.mindhash.MindhashApp.resources;

import com.mindhash.MindhashApp.dao.ObjectDao;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Map;

@Path("/objects")
public class ObjectsResource {

    @GET
    @Path("{recordingId}/{date}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Map<String, Integer>  getObjects(@PathParam("recordingId") int recordingId,
    										@PathParam("date") String date) {
        return ObjectDao.getObjectNum(recordingId, date);
    }
    
}
