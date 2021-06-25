package com.mindhash.MindhashApp.resources;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.*;

import com.mindhash.MindhashApp.dao.SessionTokenDao;
import com.mindhash.MindhashApp.dao.UserDao;
import com.mindhash.MindhashApp.model.NewPassword;
import com.mindhash.MindhashApp.model.ResMsg;
import com.mindhash.MindhashApp.model.User;

@Path("/user")
public class UsersResource {
	/*@Context
	UriInfo uriInfo;
	@Context
	Request request;*/

	@GET
	@Path("{sessionToken}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response userinfo(@PathParam("sessionToken") String sessionToken) {
		if (SessionTokenDao.checkUserByToken(sessionToken) == null) {
			System.out.println("Token not valid");
			return Response.status(Response.Status.NETWORK_AUTHENTICATION_REQUIRED).entity("NETWORK AUTHENTICATION REQUIRED").build();
		} else {
			System.out.println("Token validated sending user details");
			User user = UserDao.getDetails(sessionToken);
			return Response.status(Response.Status.OK).entity(user).build();
		}
	}

	
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

	@POST
	@Path("/new-password")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ResMsg newPassword(NewPassword newPassword) { 
		return new UserDao().confirmNewPassword(newPassword); 
	}

}
