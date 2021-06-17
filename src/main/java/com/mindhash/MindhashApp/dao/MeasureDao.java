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
            while (resultSet.next()) {
                Measure measurement = new Measure();
                measurement.setRecordingId(resultSet.getInt("recording_id"));
                measurement.setObjectId(resultSet.getInt("object_id"));
                measurement.setTime(resultSet.getString("time"));
                measurement.setX(resultSet.getDouble("x"));
                measurement.setY(resultSet.getDouble("y"));
                measurement.setVelocity(resultSet.getDouble("velocity"));
                measurement.setTimeWithoutDate(resultSet.getString("time_without_date"));
                measurement.setMeasurementId(resultSet.getInt("measurement_id"));
                contentProvider.put(resultSet.getInt("measurement_id"), measurement);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Map<Integer, Measure> getModel() {
        return contentProvider;
    }
}
