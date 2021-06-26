package com.mindhash.MindhashApp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.mindhash.MindhashApp.DBConnectivity;
import com.mindhash.MindhashApp.model.User;

public class SessionTokenDao {
	private final static int EXPIRY = 60 * 60;
	private final static Object lock = new Object();
	
	public static void setUserToken(String sessionToken, String email) {
		try {
			Date currentTime = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			Connection conn = DBConnectivity.createConnection();
			String query = "update users set session_expire_time = ?, sessiontoken = ? where email = ?";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, sdf.format(addSecondsToDate(currentTime, EXPIRY)));
			st.setString(2, sessionToken);
			st.setString(3, email);
			st.executeUpdate();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean checkTokenExist(String token) {
		boolean exist = false;
		try {
			Connection conn = DBConnectivity.createConnection();
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			String query = "select * from users where sessiontoken = ? limit 1";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, token);
			st.execute();
			ResultSet resultSet = st.executeQuery();
			conn.commit();
			
			if (resultSet.next()) {
				exist = true;
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return exist;
	}
	
	/*
	 * if the token doesn't expire, return the user.
	 */
	public static User getUserByToken(String token) {
		User user = new User();
		try {
			Date currentTime = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			Connection conn = DBConnectivity.createConnection();
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			String query = "select id, email, isadmin, session_expire_time from users where sessiontoken = ? limit 1";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, token);
			st.execute();
			ResultSet resultSet = st.executeQuery();
			conn.commit();
			
			if (resultSet.next()) {
				Date expireTime = sdf.parse(resultSet.getString("session_expire_time"));
				if (currentTime.before(expireTime)) {
					user.setId(resultSet.getInt("id"));
					user.setEmail(resultSet.getString("email"));
					user.setIsadmin(resultSet.getBoolean("isadmin"));
				}
			}
			conn.close();
		} catch (SQLException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return user;
	}
	
	/*public static Response getUserByTokenAndCheckAccess(String token, int rid) {
		User user = new User();
		try {
			Date currentTime = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			Connection conn = DBConnectivity.createConnection();
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			String query = "select id, email, isadmin, session_expire_time from users where sessiontoken = ? limit 1";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, token);
			st.execute();
			ResultSet resultSet = st.executeQuery();
			conn.commit();
			
			if (resultSet.next()) {
				Date expireTime = sdf.parse(resultSet.getString("session_expire_time"));
				if (currentTime.before(expireTime)) {
					if (accessDao.getRecordingById(token, rid)) {
						user.setId(resultSet.getInt("id"));
						user.setEmail(resultSet.getString("email"));
						user.setIsadmin(resultSet.getBoolean("isadmin"));
					} else {
						return Response
								.status(Response.Status.UNAUTHORIZED)
								.entity("UNAUTHORIZED")
								.build();
					}
				} else {
					return Response
							.status(Response.Status.NETWORK_AUTHENTICATION_REQUIRED)
							.entity("NETWORK AUTHENTICATION REQUIRED")
							.build();
				}
			}
			conn.close();
		} catch (SQLException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Response
					.status(Response.Status.INTERNAL_SERVER_ERROR)
					.entity("INTERNAL SERVER ERROR")
					.build();
		}
		return Response
				.status(Response.Status.OK)
				.entity(user)
				.build();

	}*/
	
	/*
	 * used for logout
	 */
	public static String setTokenExpired(String token) {
		String res = null;
		try {
			Date currentTime = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
			Connection conn = DBConnectivity.createConnection();
			String query = "update users set session_expire_time = ? where sessiontoken = ?";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, sdf.format(currentTime));
			st.setString(2, token);
			int update = st.executeUpdate();
			if (update == 1) {
				res = "logout";
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	
	public static Boolean checkUserByTokenAndUpdate(String token) {
		Boolean res = false;
		Connection conn = DBConnectivity.createConnection();
		
		try {
			Date currentTime = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH); 
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			String query = "select email, session_expire_time, isadmin from users where sessiontoken = ? limit 1";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, token);
			st.execute();
			ResultSet resultSet = st.executeQuery();
			conn.commit();
			
			if (resultSet.next()) {
				synchronized (lock){
					Date expireTime = sdf.parse(resultSet.getString("session_expire_time"));
					if (currentTime.before(expireTime)) {
						query = "update users set session_expire_time = ? where sessionToken = ?";
						st = conn.prepareStatement(query);
						st.setString(1, sdf.format(addSecondsToDate(currentTime, EXPIRY)));
						st.setString(2, token);
						st.execute();
						conn.commit();
						conn.setAutoCommit(true);
						conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
						res = true;
					}
				}
			}
			
			conn.close();
		} catch (SQLException | ParseException e) {
			try {
				conn.rollback();
				e.printStackTrace();
			} catch(SQLException e1) {
				e1.printStackTrace();
			}
		}
		return res;
	}
	
	public static Date addSecondsToDate(Date currentTime, Integer expiry) {
		return new Date(currentTime.getTime() + expiry * 1000);
	}
}
