package com.mindhash.MindhashApp;

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
                conn = DriverManager.getConnection("jdbc:postgresql://bronto.ewi.utwente.nl/" + username + "?currentSchema=dab_di20212b_11", username, password);
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
