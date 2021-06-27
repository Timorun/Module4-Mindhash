package com.mindhash.MindhashApp.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mindhash.MindhashApp.DBConnectivity;
import com.mindhash.MindhashApp.model.Recording;
import com.mindhash.MindhashApp.model.User;

public class RecordingDao {
	private static final int pageSize = 10;

    public static List<Recording> getRecordings(String token, int pageNum) {
    	Connection conn = DBConnectivity.createConnection();
    	PreparedStatement st = null;
    	ResultSet resultSet = null;
    	List<Recording> contentProvider = new ArrayList<>();
        try {
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            conn.setAutoCommit(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            //getlist of accessible recordingIDs
            //ArrayList<Integer> accessibleIDs = (ArrayList<Integer>) accessDao.getRecordings(token);

            //get all recording table columns
        	User user = SessionTokenDao.getUserByToken(token);
        	String query = "";
        	/*The admin has access to all the recordings*/
        	if (user.getIsadmin()) {
        		query = "select * from recording limit ? offset ?";
        		st = conn.prepareStatement(query);
                st.setInt(1, pageSize);
                st.setInt(2, pageSize * (pageNum - 1));
        	} else {
        		query = "select distinct * from recording r, recordingaccess ra where ra.email = ? and ra.recording_id = r.recording_id limit ? offset ?";
        		st = conn.prepareStatement(query);
                st.setString(1, user.getEmail());
                st.setInt(2, pageSize);
                st.setInt(3, pageSize * (pageNum - 1));
        	}
            resultSet = st.executeQuery();
            while (resultSet.next()) {
                //if (accessibleIDs.contains(resultSet.getInt("recording_id"))){
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

                    contentProvider.add(recording);
                //}
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
        return contentProvider;
    }
    
    public static List<Recording> getAllRecordings(String token) {
    	List<Recording> contentProvider = new ArrayList<>();
    	if (!SessionTokenDao.getUserByToken(token).getIsadmin()) {
    		//return empty list
    		return contentProvider;
    	}
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
            //getlist of accessible recordingIDs
            //ArrayList<Integer> accessibleIDs = (ArrayList<Integer>) accessDao.getRecordings(token);

            //get all recording table columns
            String query = "select * from recording";
            st = conn.prepareStatement(query);
            resultSet = st.executeQuery();
            while (resultSet.next()) {
                //if (accessibleIDs.contains(resultSet.getInt("recording_id"))){
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

                    contentProvider.add(recording);
                //}
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
        return contentProvider;
    }

    public static void deleteRecording(int recordingid) {
        Connection conn = DBConnectivity.createConnection();
        PreparedStatement st = null;

        String delete = "DELETE FROM measurement WHERE recording_id = ?;" +
                "DELETE FROM mindhash WHERE recording_id = ?;" +
                "DELETE FROM object WHERE recording_id = ?;" +
                "DELETE FROM recording WHERE recording_id = ?;" +
                "DELETE FROM recordingaccess WHERE recording_id = ?;";
        try {
            st = conn.prepareStatement(delete);
            for (int i = 1; i <= 5; i++) {
                st.setInt(i, recordingid);
            }
            st.executeUpdate();
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    
}
