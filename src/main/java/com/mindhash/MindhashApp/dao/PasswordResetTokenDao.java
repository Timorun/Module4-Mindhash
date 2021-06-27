package com.mindhash.MindhashApp.dao;

import com.mindhash.MindhashApp.DBConnectivity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PasswordResetTokenDao {

    public static String getUserByPassToken(String password_token) {
        String email=null;
        int userId = 0;
        try {
            Connection conn = DBConnectivity.createConnection();
            String query = "select * from passwordtoken where password_token = ? limit 1";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, password_token);
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

    public static void setPasswordToken(String token, String email) {
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
            rs.close();

            String query1 = "select * from passwordtoken where user_id = ? LIMIT 1";
            st = conn.prepareStatement(query1);
            st.setInt(1, userId);
            rs = st.executeQuery();
            if (rs.next()) {
                deletePassToken(userId);
            }
            st.close();

            String query2 = "insert into passwordtoken(password_token, user_id) values(?, ?)";
            st = conn.prepareStatement(query2);
            st.setString(1, token);
            st.setInt(2, userId);
            st.executeUpdate();

            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void deletePassToken(String token) {
        try {
            Connection conn = DBConnectivity.createConnection();
            String query = "delete from passwordtoken where password_token = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, token);
            st.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void deletePassToken(int userId) {
        try {
            Connection conn = DBConnectivity.createConnection();
            String query = "delete from passwordtoken where user_id = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, userId);
            st.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
