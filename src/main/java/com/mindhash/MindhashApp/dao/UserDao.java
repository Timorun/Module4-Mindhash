package com.mindhash.MindhashApp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;

import com.mindhash.MindhashApp.DBConnectivity;
import com.mindhash.MindhashApp.Integration.Sendgrid;
import com.mindhash.MindhashApp.Security.EncryptPassword;
import com.mindhash.MindhashApp.Security.TokenUtils;
import com.mindhash.MindhashApp.model.*;

public class UserDao {

	public static User getDetails(String token)  {
		Connection conn = DBConnectivity.createConnection();
		System.out.println(token);

		try {
			String infoquery = "SELECT email, isadmin, session_expire_time FROM users WHERE sessionToken LIKE ? ";
			PreparedStatement st = conn.prepareStatement(infoquery);
			st.setString(1, token);
			ResultSet rs = st.executeQuery();
			rs.next();
			String email = rs.getString(1);
			boolean isadmin = rs.getBoolean(2);
			String sessionexpire = rs.getString(3);

			return new User(email, sessionexpire, isadmin);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
			return new User();
		}
	}

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
		//generate password reset token
		String token = new TokenUtils().generatePasswordResetToken();

		PasswordResetToken passwordResetToken = new PasswordResetToken();
		passwordResetToken.setToken(token);
		passwordResetToken.setUser(user);
		PasswordResetTokenDao.instance.getModel().put(token, passwordResetToken);

		result = new Sendgrid().sendPasswordRequest(user.getEmail(), token, res);
		return result;
	}


	public ResMsg confirmNewPassword(NewPassword newPassword) {
		ResMsg res = new ResMsg();
		res.setRes(false);
		res.setMsg("Unable to update password at this moment.");
		boolean operationResult = requestNewPassword(newPassword.getToken(), newPassword.getPassword(), res);
		if (operationResult) {
			res.setRes(true);
		}
		return res;
	}

	private boolean requestNewPassword(String token, String password, ResMsg res) {
		boolean result = false;
		//check that the password token hasn't expired i.e. less than 3600s passed
		if (TokenUtils.isTokenExpired(token, res)) {
			res.setMsg("The password reset token has expired. Please try again.");
			return result;
		}
		//check that the token exists in the hashmap
		PasswordResetToken passwordResetToken = PasswordResetTokenDao.instance.getModel().get(token);
		if (passwordResetToken == null) {
			return result;
		}
		//The password token is valid, new hashed password can be generated
		User user = passwordResetToken.getUser();

		Connection conn = DBConnectivity.createConnection();
		try {
			String sql = "SELECT * FROM users WHERE email = ? LIMIT 1";
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, user.getEmail());
			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				String updatePass = "update users " + "set password = ?, salt =? " + "where email = ?";
				PreparedStatement preparedStatement = conn.prepareStatement(updatePass);
				byte[] salt = EncryptPassword.generateSalt();
				byte[] hashedPassword = EncryptPassword.HashPassStr(password, salt);
				preparedStatement.setBytes(1, hashedPassword);
				preparedStatement.setBytes(2, salt);
				preparedStatement.setString(3, user.getEmail());
				preparedStatement.executeUpdate();
				result = true;
			}
			conn.close();
		} catch (SQLException e) {
			return result;
		}
		//delete token after it has been used
		PasswordResetTokenDao.instance.getModel().remove(token);
		return result;
	}
}
