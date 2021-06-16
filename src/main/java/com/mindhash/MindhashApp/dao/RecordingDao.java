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
                Recording recording = new Recording();
                int recordingId = resultSet.getInt(1);
                recording.setRecordingID(resultSet.getInt(1));
                recording.setLatitude(resultSet.getDouble(2));
                recording.setLongitude(resultSet.getDouble(3));
                recording.setDate(resultSet.getString(4));
                recording.setStartTime(resultSet.getString(5));
                recording.setEndTime(resultSet.getString(6));
                recording.setResolution(resultSet.getString(7));
                recording.setFrameRate(resultSet.getInt(8));

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
