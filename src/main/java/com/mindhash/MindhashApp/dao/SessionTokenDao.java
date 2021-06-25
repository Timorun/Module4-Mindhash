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
	
	public static String getUserByToken(String token) {
		String res = null;
		try {
			Connection conn = DBConnectivity.createConnection();
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			String query = "select email, session_expire_time from users where sessiontoken = ? limit 1";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, token);
			st.execute();
			ResultSet resultSet = st.executeQuery();
			conn.commit();
			
			if (resultSet.next()) {
				res = resultSet.getString(1);
			}
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return res;
	}
	
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
	
	public static String checkUserByToken(String token) {
		String res = null;
		Connection conn = DBConnectivity.createConnection();
		
		try {
			Date currentTime = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH); 
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			String query = "select email, session_expire_time from users where sessiontoken = ? limit 1";
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
						res = resultSet.getString("email");
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
