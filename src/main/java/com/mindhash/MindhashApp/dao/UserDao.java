package com.mindhash.MindhashApp.dao;

import com.mindhash.MindhashApp.model.User;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

public class UserDao {


    public boolean registerUser(User user) {
        String email = user.getEmail();
        String password = user.getPassword();

        Connection conn = null;

        try {
            Class.forName("org.postgresql.Driver");
            try {
                conn = DriverManager.getConnection("jdbc:postgresql://bronto.ewi.utwente.nl/" + MindhashDao.username + "?currentSchema=dab_di20212b_11", MindhashDao.username, MindhashDao.password);
            } catch (SQLException e) {
                System.err.println("Oops: " + e.getMessage());
                System.err.println("SQLState: " + e.getSQLState());
            }
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC driver not loaded");
        }

        try {
            String query = "insert into users(email, password) values (?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, email);
            String hashedPassword = generateHash(password);
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

}
