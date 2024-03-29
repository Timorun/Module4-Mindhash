package com.mindhash.MindhashApp.dao;

import java.io.*;
import java.sql.*;
import java.util.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.mindhash.MindhashApp.DBConnectivity;
import com.mindhash.MindhashApp.model.Measurement;

/* Class adds JSON data to the database
 **/
public class MindhashDao {
    private static final String userHome = System.getProperty("user.home");

    public static void main (String[] args) {

        Connection conn = DBConnectivity.createConnection();

        try {
            conn.setAutoCommit(false);
            PreparedStatement preparedStatement = conn.prepareStatement("insert into mindhash(object_id, object_type, points, length, width, x, y, velocity, ma_velocity, measurement, time, recording_id) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");

            ObjectMapper objectMapper = new ObjectMapper();
            List<Measurement> list = objectMapper.readValue(new File(userHome + "/data2.json.txt"), new TypeReference<List<Measurement>>() {
            });

            Iterator<Measurement> it = list.iterator();
            while (it.hasNext()) {
                Measurement m = it.next();
                preparedStatement.setInt(1, m.getTag().getObject_id());
                preparedStatement.setString(2, m.getTag().getObject_type());
                preparedStatement.setInt(3, m.getField().getPoints());
                preparedStatement.setDouble(4, m.getField().getLength());
                preparedStatement.setDouble(5, m.getField().getWidth());
                preparedStatement.setDouble(6, m.getField().getX());
                preparedStatement.setDouble(7, m.getField().getY());
                preparedStatement.setDouble(8, m.getField().getVelocity());
                preparedStatement.setDouble(9, m.getField().getMa_velocity());
                preparedStatement.setString(10, m.getMeasurement());
                preparedStatement.setString(11, m.getTime());
                preparedStatement.setInt(12, 1);
                preparedStatement.addBatch();
            }

            preparedStatement.executeBatch();
            preparedStatement.close();

            conn.commit();
            conn.close();

            System.out.println("Records inserted.....");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
