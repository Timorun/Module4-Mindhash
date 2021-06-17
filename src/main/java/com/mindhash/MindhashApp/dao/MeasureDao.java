package com.mindhash.MindhashApp.dao;

import com.mindhash.MindhashApp.DBConnectivity;
import com.mindhash.MindhashApp.model.Measure;
import com.mindhash.MindhashApp.model.MeasureRes;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MeasureDao {

    public static MeasureRes getMeasurement(Integer recordingId, String date) {
        Connection conn = DBConnectivity.createConnection();
        MeasureRes res = new MeasureRes();
        res.setRecordingId(recordingId);
        res.setDate(date);
        try {
            String query = "select object_id, time, x, y, velocity, time_without_date from measurement where recording_id = ? and date = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, recordingId);
            st.setString(2, date);
            ResultSet resultSet = st.executeQuery();
            ArrayList<Measure> mLi = new ArrayList<Measure>();
            while (resultSet.next()) {
                Measure measurement = new Measure();
                //measurement.setRecordingId(resultSet.getInt("recording_id"));
                measurement.setObjectId(resultSet.getInt("object_id"));
                measurement.setTime(resultSet.getString("time"));
                measurement.setX(resultSet.getDouble("x"));
                measurement.setY(resultSet.getDouble("y"));
                measurement.setVelocity(resultSet.getDouble("velocity"));
                measurement.setTimeWithoutDate(resultSet.getString("time_without_date"));
                /*measurement.setMeasurementId(resultSet.getInt("measurement_id"));
                contentProvider.put(resultSet.getInt("measurement_id"), measurement);*/
                mLi.add(measurement);
            }
            res.setMeasureList(mLi);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    
}
