package com.mindhash.MindhashApp.resources;

import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.*;

import com.mindhash.MindhashApp.dao.SessionTokenDao;
import com.mindhash.MindhashApp.dao.UserDao;
import com.mindhash.MindhashApp.model.*;

import java.util.ArrayList;
import java.util.List;

@Path("/user")
public class UsersResource {
	
	@GET
	@Path("/info")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response userinfo(@Context ContainerRequestContext request) {
		String sessionToken = request.getHeaderString(HttpHeaders.AUTHORIZATION);
		User user = SessionTokenDao.getUserByToken(sessionToken);
		if (user.getEmail() == null) {
			System.out.println("Token not valid");
			return Response
					.status(Response.Status.NETWORK_AUTHENTICATION_REQUIRED)
					.entity("NETWORK AUTHENTICATION REQUIRED")
					.build();
		} else {
			System.out.println("Token validated sending user details");
			return Response
					.status(Response.Status.OK)
					.entity(user)
					.build();
		}
	}

	@GET
	@Path("/mails")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Object mails(@Context ContainerRequestContext request) {
		String token = request.getHeaderString(HttpHeaders.AUTHORIZATION);
		// only allow admin
		if (!SessionTokenDao.getUserByToken(token).getIsadmin()) {
			return Response
					.status(Response.Status.NETWORK_AUTHENTICATION_REQUIRED)
					.entity("NETWORK AUTHENTICATION REQUIRED")
					.build();
		} else {
			System.out.println("Token of admin, can get mails");
			List<String> mails = new ArrayList<>(UserDao.getMails());
			return Response.status(Response.Status.OK).entity(mails).build();
		}
	}


	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ResMsg register(UserRegJAXB user) {
		return UserDao.register(user);
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ResMsg login(UserJAXB user) {
		if (UserDao.checkVerified(user.getEmail()) == false){
			//User not verified
			ResMsg response = new ResMsg();
			response.setMsg("Account not verified, please check your email");
			return response;
		}
		return UserDao.login(user);
	}
	
	@POST
	@Path("/isLoggedIn")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response autoLogin(@Context ContainerRequestContext request) {
		return UserDao.autologin(request);
	}
	
	@POST
	@Path("/logout")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Response logout(@Context ContainerRequestContext request) {
		return Response
				.status(Response.Status.OK)
				.entity(UserDao.logout(request))
				.build();
	}

	@POST
	@Path("/password-reset")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ResMsg resetPassword(UserJAXB user) { 
		return new UserDao().resetPasssword(user); 
	}

	@POST
	@Path("/new-password")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ResMsg newPassword(NewPassword newPassword) { 
		return new UserDao().confirmNewPassword(newPassword); 
	}

	@POST
	@Path("/verify-email")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public ResMsg verifyEmail(EmailTokenJAXB emailToken) {
		return new UserDao().verifyEmail(emailToken);
	}

}
