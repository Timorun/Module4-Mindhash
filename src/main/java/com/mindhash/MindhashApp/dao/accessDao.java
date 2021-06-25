package com.mindhash.MindhashApp.dao;

import com.mindhash.MindhashApp.DBConnectivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class accessDao {

    public static List<Integer> getRecordings(String token) {
        Connection conn = DBConnectivity.createConnection();
        ArrayList<Integer> accessibleids = new ArrayList<>();

        try {
            String idquery = "SELECT a.recording_id " +
                    "FROM recordingaccess a, users u " +
                    "WHERE u.sessiontoken = ? AND u.email = a.email";
            PreparedStatement st = conn.prepareStatement(idquery);
            st.setString(1, token);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                accessibleids.add(rs.getInt(1));
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accessibleids;
    }

    public static void giveAccess(String email, Integer id) {
        Connection conn = DBConnectivity.createConnection();

        try {
            String accessquery = "INSERT INTO recordingaccess (recording_id,email)\n" +
                    "VALUES (?, ?)";
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
