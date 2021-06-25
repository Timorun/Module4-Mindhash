package com.mindhash.MindhashApp.dao;

import com.mindhash.MindhashApp.DBConnectivity;
import com.mindhash.MindhashApp.model.ResMsg;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmailVerificationTokenDao {

    //insert email token into the db
    public static void setEmailToken(String emailToken, String email) {
        int userId = 0;
        try {
            Connection conn = DBConnectivity.createConnection();
            String query = "select * from users where email = ? LIMIT 1";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, email);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("id");
            }
            st.close();

            String query2 = "insert into emailtoken(email_token, user_id) values(?, ?)";
            st = conn.prepareStatement(query2);
            st.setString(1, emailToken);
            st.setInt(2, userId);
            st.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //get user by email token
    public static String getUserByEmailToken(String emailToken) {
        String email=null;
        int userId = 0;
        try {
            Connection conn = DBConnectivity.createConnection();
            String query = "select * from emailtoken where email_token = ? limit 1";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, emailToken);
            st.execute();
            ResultSet resultSet = st.executeQuery();
            if (resultSet.next()) {
                userId = resultSet.getInt("user_id");
            }
            st.close();
            resultSet.close();

            String query2 = "select * from users where id = ?";
            st = conn.prepareStatement(query2);
            st.setInt(1, userId);
            resultSet = st.executeQuery();
            if (resultSet.next()) {
                email = resultSet.getString("email");
            }
            st.execute();
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return email;
    }

    public static void deleteEmailToken(String emailToken) {
        try {
            Connection conn = DBConnectivity.createConnection();
            String query = "delete from emailtoken where email_token = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, emailToken);
            st.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void setEmailVerifiedStatus(String email) {
        try {
            Connection conn = DBConnectivity.createConnection();
            String query = "update users set isverified=? where email = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setBoolean(1, true);
            st.setString(2, email);
            st.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
