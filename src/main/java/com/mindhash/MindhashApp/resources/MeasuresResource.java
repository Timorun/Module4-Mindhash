package com.mindhash.MindhashApp.resources;

import com.mindhash.MindhashApp.dao.MeasureDao;
import com.mindhash.MindhashApp.model.Measure;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

@Path("/measurements")
public class MeasuresResource {
    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Measure> getObjects() {
        List<Measure> measurements = new ArrayList<Measure>();
        measurements.addAll(MeasureDao.instance.getModel().values());
        return measurements;
    }
}
