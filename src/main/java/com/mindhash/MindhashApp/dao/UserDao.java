package com.mindhash.MindhashApp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;

import com.auth0.jwt.*;
import com.auth0.jwt.algorithms.Algorithm;
import com.mindhash.MindhashApp.DBConnectivity;
import com.mindhash.MindhashApp.EncryptPassword;
import com.mindhash.MindhashApp.Integration.Sendgrid;
import com.mindhash.MindhashApp.Security.SecurityConstants;
import com.mindhash.MindhashApp.model.PasswordResetToken;
import com.mindhash.MindhashApp.model.ResMsg;
import com.mindhash.MindhashApp.model.SessionToken;
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
            	res.setMsg(user.getEmail() + " has already been taken.");
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
        	res.setMsg(e.getMessage());
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
                byte[] hashedPassword = EncryptPassword.HashPassStr(user.getPassword(), result.getBytes("salt"));
                if (Arrays.equals(hashedPassword, result.getBytes("password"))) {
                	String sessionToken = new SessionToken(user.getEmail()).sessiontoken;
                	SessionTokenDao.setUserToken(sessionToken, user.getEmail());
                	res.setRes(true);
                	res.setMsg(sessionToken);
                } else {
                	res.setRes(false);
                	res.setMsg("Incorrect email address or password.");
                }
	        } else {
	        	res.setRes(false);
	        	res.setMsg("Incorrect email address or password.");
	        }
	        conn.close();
		} catch (SQLException e) {
			res.setRes(false);
        	res.setMsg(e.getMessage());
		}
		return res;
	}

	public static ResMsg autologin(ContainerRequestContext request) {
		ResMsg res = new ResMsg();
		String token = request.getHeaderString(HttpHeaders.AUTHORIZATION);
		if (SessionTokenDao.checkUserByToken(token) == null) {
			res.setRes(false);
		} else {
			res.setRes(true);
		}
		return res;
	}

	public ResMsg resetPasssword(User user) {
		ResMsg res = new ResMsg();
		res.setRes(false);

		try {
			Connection conn = DBConnectivity.createConnection();
			String sql = "SELECT * FROM users WHERE email = ? LIMIT 1";
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, user.getEmail());
			ResultSet result = st.executeQuery();

			if (result.next()) {
				boolean operationResult = requestPasswordReset(user, res);
				if (operationResult) {
					res.setRes(true);
				}
			}
			conn.close();
		} catch (SQLException e) {
			res.setMsg(e.getMessage());
		}
		return res;
	}

	private boolean requestPasswordReset(User user, ResMsg res) {
		boolean result = false;
		String token = generatePasswordResetToken();

		PasswordResetToken passwordResetToken = new PasswordResetToken();
		passwordResetToken.setToken(token);
		passwordResetToken.setUser(user);
		PasswordResetDao.instance.getModel().put(passwordResetToken.getTokenId(), passwordResetToken);

		result = new Sendgrid().sendPasswordRequest(user.getEmail(), token, res);
		return result;
	}


	/**
	 * @return a secure password reset token
	 */
	private String generatePasswordResetToken() {
		Algorithm algorithm = Algorithm.HMAC512("secret");
		String token = JWT.create()
				.withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstants.PASSWORD_RESET_EXPIRATION_TIME))
				.sign(algorithm);
		return token;
	}
	
}
