package com.mindhash.MindhashApp.dao;

import com.mindhash.MindhashApp.DBConnectivity;
import com.mindhash.MindhashApp.model.Measure;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public enum MeasureDao {
    instance;

    private Map<Integer, Measure> contentProvider = new HashMap<>();

    private MeasureDao() {
        Connection conn = DBConnectivity.createConnection();

        try {
            String query = "select * from measurement";
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet resultSet = st.executeQuery();
            while(resultSet.next()) {
                Measure measurement = new Measure();
                measurement.setRecordingId(resultSet.getInt(1));
                measurement.setObjectId(resultSet.getInt(2));
                measurement.setTime(resultSet.getString(3));
                measurement.setX(resultSet.getDouble(4));
                measurement.setY(resultSet.getDouble(5));
                measurement.setVelocity(resultSet.getDouble(6));
                measurement.setMaVelocity(resultSet.getDouble(7));
                measurement.setTimeWithoutDate(resultSet.getString(8));
                contentProvider.put(resultSet.getInt(1), measurement);
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, Measure> getModel(){
        return contentProvider;
    }
}
