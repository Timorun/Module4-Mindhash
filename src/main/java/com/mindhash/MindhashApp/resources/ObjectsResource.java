package com.mindhash.MindhashApp.resources;

import com.mindhash.MindhashApp.dao.ObjectDao;
import com.mindhash.MindhashApp.dao.RecordingDao;
import com.mindhash.MindhashApp.model.Objectt;
import com.mindhash.MindhashApp.model.Recording;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;

@Path("/objects")
public class ObjectsResource {
    @Context
    UriInfo uriInfo;
    @Context
    Request request;

    @GET
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Objectt> getObjects() {
        List<Objectt> objects = new ArrayList<Objectt>();
        objects.addAll(ObjectDao.instance.getModel().values());
        return objects;
    }
}
