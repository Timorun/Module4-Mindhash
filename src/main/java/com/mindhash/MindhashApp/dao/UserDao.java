package com.mindhash.MindhashApp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import com.mindhash.MindhashApp.DBConnectivity;
import com.mindhash.MindhashApp.EncryptPassword;
import com.mindhash.MindhashApp.model.ResMsg;
import com.mindhash.MindhashApp.model.User;

public class UserDao {
	public static ResMsg register(User user) {
		ResMsg res = new ResMsg();
		
		Connection conn = DBConnectivity.createConnection();
		try {
			String checkTaken = "SELECT * FROM users WHERE email = ? LIMIT 1";
            PreparedStatement st = conn.prepareStatement(checkTaken);
            st.setString(1, user.getEmail());
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
            	res.setRes(false);
            	res.setErrMsg(user.getEmail() + " has already been taken.");
            } else {
            	String register = "insert into users(email, password, salt) values (?, ?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(register);
                preparedStatement.setString(1, user.getEmail());
                byte[] salt = EncryptPassword.generateSalt();
                byte[] hashedPassword = EncryptPassword.HashPassStr(user.getPassword(), salt);
                preparedStatement.setBytes(2, hashedPassword);
                preparedStatement.setBytes(3, salt);
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
	
	public static ResMsg login(User user) {
		ResMsg res = new ResMsg();
		
		try {
			Connection conn = DBConnectivity.createConnection();
	        String sql = "SELECT * FROM users WHERE email = ? LIMIT 1";
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, user.getEmail());
	        ResultSet result = st.executeQuery();
	        
	        if (result.next()) {
	        	System.out.println(result.getBytes("salt"));
                System.out.println(result.getBytes("password"));
                byte[] hashedPassword = EncryptPassword.HashPassStr(user.getPassword(), result.getBytes("salt"));
                if (Arrays.equals(hashedPassword, result.getBytes("password"))) {
                	res.setRes(true);
                } else {
                	res.setRes(false);
                	res.setErrMsg("Incorrect email address or password.");
                }
	        } else {
	        	res.setRes(false);
	        	res.setErrMsg("Incorrect email address or password.");
	        }
	        conn.close();
		} catch (SQLException e) {
			res.setRes(false);
        	res.setErrMsg(e.getMessage());
		}
		return res;
	}
}
