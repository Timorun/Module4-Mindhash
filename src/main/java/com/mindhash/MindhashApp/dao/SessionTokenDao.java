package com.mindhash.MindhashApp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.mindhash.MindhashApp.DBConnectivity;

public class SessionTokenDao {
	private final static int EXPIRY = 60 * 60;
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
	public static String getUser(String token) {
		try {
			Date currentTime = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH); 
			Connection conn = DBConnectivity.createConnection();
			conn.setAutoCommit(false);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			String query = "select email, session_expire_time from users where sessiontoken = ?";
			PreparedStatement st = conn.prepareStatement(query);
			st.setString(1, token);
			st.execute();
			ResultSet resultSet = st.executeQuery();
			conn.commit();
			query = "update users set session_expire_time = ? where sessionToken = ?";
			st = conn.prepareStatement(query);
			st.setString(1, sdf.format(addSecondsToDate(currentTime, EXPIRY)));
			st.setString(2, token);
			st.execute();
			conn.commit();
			conn.setAutoCommit(true);
			conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
			conn.close();
			if (resultSet.next()) {
				Date expireTime = sdf.parse(resultSet.getString(2));
				if (expireTime.before(expireTime)) {
					return resultSet.getString(1);
				}
			}
		} catch (SQLException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Date addSecondsToDate(Date currentTime, Integer expiry) {
		currentTime.setTime(currentTime.getTime() + expiry * 1000);
		return currentTime;
	}
}
