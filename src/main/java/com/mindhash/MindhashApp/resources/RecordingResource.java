package com.mindhash.MindhashApp.resources;

import com.mindhash.MindhashApp.dao.RecordingDao;
import com.mindhash.MindhashApp.model.Recording;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

public class RecordingResource {

    @Context
    UriInfo uriInfo;
    @Context
    Request request;
    int id;

    public RecordingResource(UriInfo uriInfo, Request request, int id) {
        this.uriInfo = uriInfo;
        this.request = request;
        this.id = id;
    }

    //return the details of a specific recording
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Recording getRecordingDetails() {
        Recording recording = RecordingDao.instance.getModel().get(id);
        if (recording == null) {
            throw new RuntimeException("Recording with id " + id + "not found");
        }
        return recording;
    }

    @Path("/times")
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Recording getRecordingTimes() {
        // to be implement get start time and end time as json
        Recording recording = RecordingDao.instance.getModel().get(id);
        if (recording == null) {
            throw new RuntimeException("Recording with id " + id + "not found");
        }
        return recording;
    }
}
