package com.mindhash.MindhashApp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectivity {

    public static Connection createConnection() {
        String username= "dab_di20212b_11";
        String password = "FGqxQ1cJXYxvmPsW";
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
        return conn;
    }
}
