package com.mindhash.MindhashApp.dao;

import com.mindhash.MindhashApp.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class UserDao {
    public static final String SALT = "AEgdqp2w3hZJZTuFvfMc";

    public boolean newMail(String email) {
        Connection conn = null;
        conn = DBConnectivity.createConnection();

        try {
            String query = "SELECT users.email FROM dab_di20212b_11.users";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                String dbmail = rs.getString("email");
                conn.close();
                if (email.equals(dbmail)) {
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    public boolean registerUser(User user) throws SQLException {
        String email = user.getEmail();
        String password = user.getPassword();

        Connection conn = null;
        conn = DBConnectivity.createConnection();

        try {
            String query = "insert into users(email, password) values (?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, email);
            String saltPass = SALT + password;
            String hashedPassword = generateHash(saltPass);
            preparedStatement.setString(2, hashedPassword);
            int i = preparedStatement.executeUpdate();

            conn.close();

            if (i > 0) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;

    }

    private String generateHash(String password) {
        StringBuilder hash = new StringBuilder();

        try {
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] hashedBytes = sha.digest(password.getBytes());
            char[] digits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
            for (int idx = 0; idx < hashedBytes.length; ++idx) {
                byte b = hashedBytes[idx];
                hash.append(digits[(b & 0xf0) >> 4]);
                hash.append(digits[b & 0x0f]);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hash.toString();

    }

    public User checkLogin(String email, String password) throws SQLException {
        String saltPass = SALT + password;
        String hashedPass = generateHash(saltPass);

        Connection conn = null;
        conn = DBConnectivity.createConnection();

        String sql = "SELECT * FROM users WHERE email = ? and password = ?";

        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        preparedStatement.setString(1, email);
        preparedStatement.setString(2, hashedPass);
        ResultSet result = preparedStatement.executeQuery();

        User user = null;
        if(result.next()) {
            user = new User();
            user.setEmail(email);
        }
        conn.close();
        return user;

    }
}
