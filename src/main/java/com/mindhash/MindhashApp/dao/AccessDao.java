package com.mindhash.MindhashApp.dao;

import com.mindhash.MindhashApp.DBConnectivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccessDao {

    public static boolean getRecordingById(String token, int id) {
        Connection conn = DBConnectivity.createConnection();
        boolean res = false;
        try {
            String query = "SELECT * FROM recordingaccess ra, users u " +
                    	"WHERE u.sessiontoken = ? AND ra.recording_id = ? AND u.email = ra.email LIMIT 1";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, token);
            st.setInt(2, id);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                res = true;
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void giveAccess(String email, Integer id) {
        Connection conn = DBConnectivity.createConnection();

        try {
            String accessquery = "INSERT INTO recordingaccess (recording_id, email) VALUES (?, ?)";
            PreparedStatement st = conn.prepareStatement(accessquery);
            st.setInt(1, id);
            st.setString(2, email);
            st.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
