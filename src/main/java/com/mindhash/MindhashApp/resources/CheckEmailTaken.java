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

import com.mindhash.MindhashApp.dao.DBConnectivity;
import com.mindhash.MindhashApp.model.CheckEmailRes;

@Path("checkemail/{email}")
public class CheckEmailTaken {
	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	String email;
	
	@GET
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public CheckEmailRes check(@PathParam("email") String email) {
		this.email = email;
		CheckEmailRes res = new CheckEmailRes();
		Connection conn = DBConnectivity.createConnection();

        try {
            String query = "SELECT COUNT(*) AS count FROM users WHERE email = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                int count = rs.getInt("count");
                conn.close();
                if (count > 0) {
                	res.setIsTaken(true);
                }
            }
            res.setIsTaken(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
	}

}
