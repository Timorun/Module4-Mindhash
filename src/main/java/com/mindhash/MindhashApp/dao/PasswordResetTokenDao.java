package com.mindhash.MindhashApp.dao;

import com.mindhash.MindhashApp.DBConnectivity;
import com.mindhash.MindhashApp.model.ResMsg;

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
            String query = "select * from token where password_token = ? limit 1";
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

    public static void setPasswordToken(String token, String email, ResMsg res) {
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

            String query1 = "select * from token where user_id = ? LIMIT 1";
            st = conn.prepareStatement(query1);
            st.setInt(1, userId);
            rs = st.executeQuery();
            if (rs.next()) {
                String query2 = "update token set password_token = ? where user_id = ?";
                st = conn.prepareStatement(query2);
                st.setString(1, token);
                st.setInt(2, userId);
                st.executeUpdate();
            } else {
                String query3 = "insert into token(password_token, user_id) values(?, ?)";
                st = conn.prepareStatement(query3);
                st.setString(1, token);
                st.setInt(2, userId);
                st.executeUpdate();
            }
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void deletePassToken(String token, ResMsg res) {
        try {
            Connection conn = DBConnectivity.createConnection();
            String query = "delete from token where password_token = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, token);
            st.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
