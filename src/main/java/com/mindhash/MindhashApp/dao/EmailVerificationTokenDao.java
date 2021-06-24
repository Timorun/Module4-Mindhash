package com.mindhash.MindhashApp.dao;

import com.mindhash.MindhashApp.DBConnectivity;

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
}
