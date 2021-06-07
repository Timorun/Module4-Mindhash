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

    	try {
            String query = "select * from recording";
            PreparedStatement st = conn.prepareStatement(query);
            ResultSet resultSet = st.executeQuery();
            while(resultSet.next()) {
            	//System.out.println(resultSet.getString(1) + " " +resultSet.getFloat(2) + " "+resultSet.getDate(3));
            	Recording recording = new Recording();
            	recording.setRecordingID(resultSet.getInt(1));
            	recording.setLatitude(resultSet.getDouble(2));
            	recording.setLongitude(resultSet.getDouble(3));
            	recording.setTotalObjects(resultSet.getInt(4));
            	recording.setTotalTwoWheelers(resultSet.getInt(5));
            	recording.setTotalPedestrians(resultSet.getInt(6));
            	recording.setTotalVehicles(resultSet.getInt(7));
            	recording.setDate(resultSet.getString(8));
                recording.setStartTime(resultSet.getString(9));
                recording.setEndTime(resultSet.getString(10));
                recording.setResolution(resultSet.getString(11));
                recording.setFrameRate(resultSet.getInt(12));
                recording.setPedestrians_min_velocity(resultSet.getDouble(13));
                recording.setWheelers_max_velocity(resultSet.getDouble(14));
                recording.setWheelers_min_velocity(resultSet.getDouble(15));
                recording.setVehicles_max_velocity(resultSet.getDouble(16));
                recording.setVehicles_min_velocity(resultSet.getDouble(17));
                recording.setPedestrians_max_velocity(resultSet.getDouble(18));
                recording.setVehiclesAvgVelocity(resultSet.getDouble(19));
                recording.setPedestriansAvgVelocity(resultSet.getDouble(20));
                recording.setWheelersAvgVelocity(resultSet.getDouble(21));
            	contentProvider.put(resultSet.getInt(1), recording);
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Map<Integer, Recording> getModel(){
        return contentProvider;
    }
}
