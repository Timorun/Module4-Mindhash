package com.mindhash.MindhashApp.resources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import com.mindhash.MindhashApp.DBConnectivity;

@Path("checkemail/{email}")
public class CheckEmailTaken {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	String email;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public CheckEmailRes check(@PathParam("email") String email) {
		this.email = email;
		CheckEmailRes res = new CheckEmailRes();
		Connection conn = DBConnectivity.createConnection();

        try {
            String query = "SELECT COUNT(*) AS count FROM users WHERE email = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            conn.close();
            rs.next();
            int count = rs.getInt("count");
            if (count > 0) {
            	res.setIsTaken(true);
            } else {
            	res.setIsTaken(false);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            res.setErrMsg(e.getMessage());
        }
        return res;
	}
	
	private class CheckEmailRes {
		private boolean isTaken;
		private String errMsg;
		
		public CheckEmailRes() {
			
		}
		
		@SuppressWarnings("unused")
		public boolean getIsTaken() {
			return isTaken;
		}
		
		@SuppressWarnings("unused")
		public String getErrMsg() {
			return errMsg;
		}
		
		public void setIsTaken(boolean isTaken) {
			this.isTaken = isTaken;
		}
		
		public void setErrMsg(String errMsg) {
			this.errMsg = errMsg;
		}
	}
}
