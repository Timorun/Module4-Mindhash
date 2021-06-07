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
import com.mindhash.MindhashApp.model.ResponseMsg;

@Path("checkemail/{email}")
public class CheckEmailTaken {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	
	String email;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ResponseMsg check(@PathParam("email") String email) {
		this.email = email;
		ResponseMsg res = new ResponseMsg();
		Connection conn = DBConnectivity.createConnection();

        try {
            String query = "SELECT COUNT(*) AS count FROM users WHERE email = ? LIMIT 1";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            conn.close();
            rs.next();
            int count = rs.getInt("count");
            if (count > 0) {
            	res.setRes(true);
            } else {
            	res.setRes(false);
            }
        } catch (SQLException e) {
            res.setErrMsg(e.getMessage());
        }
        return res;
	}
}
