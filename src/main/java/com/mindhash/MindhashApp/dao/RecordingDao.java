package com.mindhash.MindhashApp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.mindhash.MindhashApp.DBConnectivity;
import com.mindhash.MindhashApp.model.Recording;

public enum RecordingDao {
	instance;

    private Map<Integer, Recording> contentProvider = new HashMap<>();

    private RecordingDao() {
    	Connection conn = DBConnectivity.createConnection();
    	PreparedStatement st = null;
    	ResultSet resultSet = null;
        try {
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            conn.setAutoCommit(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            //get all recording table columns
            String query = "select * from recording";
            st = conn.prepareStatement(query);
            resultSet = st.executeQuery();
            while (resultSet.next()) {
            	System.out.println(resultSet.getDouble(2));
                Recording recording = new Recording();
                int recordingId = resultSet.getInt("recording_id");
                recording.setRecordingID(recordingId);
                recording.setLatitude(resultSet.getDouble("latitude"));
                recording.setLongitude(resultSet.getDouble("longitude"));
                recording.setDate(resultSet.getString("date"));
                recording.setStartTime(resultSet.getString("start_time"));
                recording.setEndTime(resultSet.getString("end_time"));
                recording.setResolution(resultSet.getString("resolution"));
                recording.setFrameRate(resultSet.getInt("framerate"));

                contentProvider.put(recordingId, recording);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                resultSet.close();
                st.close();
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }
    
    public Map<Integer, Recording> getModel(){
        return contentProvider;
    }
}
