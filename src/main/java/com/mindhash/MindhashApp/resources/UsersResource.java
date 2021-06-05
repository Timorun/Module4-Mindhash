package com.mindhash.MindhashApp.resources;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Random;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

import com.mindhash.MindhashApp.DBConnectivity;
import com.mindhash.MindhashApp.model.ResponseMsg;
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
	public ResponseMsg register(User user) {
		ResponseMsg res = new ResponseMsg();
		
		Connection conn = DBConnectivity.createConnection();
		try {
			String checkTaken = "SELECT COUNT(*) AS count FROM users WHERE email = ? LIMIT 1";
            PreparedStatement st = conn.prepareStatement(checkTaken);
            st.setString(1, user.getEmail());
            ResultSet rs = st.executeQuery();
            rs.next();
            int count = rs.getInt("count");
            if (count > 0) {
            	res.setRes(false);
            	res.setErrMsg(user.getEmail() + " has already been taken.");
            } else {
            	String register = "insert into users(email, password, salt) values (?, ?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(register);
                preparedStatement.setString(1, user.getEmail());
                Random r = new SecureRandom();
                byte[] salt = new byte[20];
                r.nextBytes(salt);
                String saltStr = Base64.getEncoder().encodeToString(salt);
                String saltPass = saltStr + user.getPassword();
                String hashedPassword = generateHash(saltPass);
                preparedStatement.setString(2, hashedPassword);
                preparedStatement.setString(3, saltStr);
                int i = preparedStatement.executeUpdate();
                if (i > 0) {
                	res.setRes(true);
                } else {
                	res.setRes(false);
                }
            }
            conn.close();
        } catch (SQLException e) {
        	res.setRes(false);
        	res.setErrMsg(e.getMessage());
        }
		return res;
	}
	
	/*public ResponseMsg checkEmail(String email) {
		ResponseMsg res = new ResponseMsg();
		Connection conn = DBConnectivity.createConnection();

        try {
            String query = "SELECT COUNT(*) AS count FROM users WHERE email = ? LIMITE 1";
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
            	res.setErrMsg(email + " has already been taken.");
            }
        } catch (SQLException e) {
        	res.setRes(false);
            res.setErrMsg(e.getMessage());
        }
        return res;
	}*/
	
	private String generateHash(String password) {
        StringBuilder hash = new StringBuilder();
        
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = sha.digest(password.getBytes());
            char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
            for (int idx = 0; idx < hashedBytes.length; ++idx) {
                byte b = hashedBytes[idx];
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash.toString();
    }
}
