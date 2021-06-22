package com.mindhash.MindhashApp.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import com.mindhash.MindhashApp.dao.UserDao;
import com.mindhash.MindhashApp.model.ResMsg;
import com.mindhash.MindhashApp.model.User;

@Path("/user")
public class UsersResource {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ResMsg register(User user) {
		return UserDao.register(user);
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ResMsg login(User user) {
		return UserDao.login(user);
	}
	
	@POST
	@Path("/login/auto")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ResMsg autoLogin(@Context ContainerRequestContext request) {
		return UserDao.autologin(request);
	}

	@POST
	@Path("/password-reset")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ResMsg resetPassword(User user) {
		return new UserDao().resetPasssword(user);
	}

}
