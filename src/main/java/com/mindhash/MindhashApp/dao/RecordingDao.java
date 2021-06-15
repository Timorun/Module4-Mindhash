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
            while(resultSet.next()) {
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

                contentProvider.put(resultSet.getInt(1), recording);
                resultSet.close();
                st.close();

                //count number of objects of different types
                String countObjects = "select count(*) from object, recording where object_type = ? and object.recording_id = ?";
                st = conn.prepareStatement(countObjects);
                st.setInt(2, recordingId);
                st.setString(1, "vehicle");
                resultSet = st.executeQuery();
                while (resultSet.next()) {
                    recording.setTotalVehicles(resultSet.getInt(1));
                }
                st.setString(1, "pedestrian");
                resultSet = st.executeQuery();
                while (resultSet.next()) {
                    recording.setTotalPedestrians(resultSet.getInt(1));
                }
                st.setString(1, "two-wheeler");
                resultSet = st.executeQuery();
                while (resultSet.next()) {
                    recording.setTotalTwoWheelers(resultSet.getInt(1));
                }
                resultSet.close();
                st.close();

                //count total number of objects
                String countTotal = "select count(*) from object";
                st = conn.prepareStatement(countTotal);
                resultSet = st.executeQuery();
                while (resultSet.next()) {
                    recording.setTotalObjects(resultSet.getInt(1));
                }
                resultSet.close();
                st.close();

                //get average max & min velocity per object type
                String velocity = "select max(measurement.velocity) as max_velocity, min(measurement.velocity) as min_velocity, avg(measurement.velocity) as avg_velocity from measurement, object where measurement.object_id = object.object_id and object.object_type = ? and object.recording_id =?";
                st = conn.prepareStatement(velocity);
                st.setInt(2, recordingId);
                st.setString(1, "vehicle");
                resultSet = st.executeQuery();
                while (resultSet.next()) {
                    double vehiclesMax = resultSet.getDouble("max_velocity");
                    recording.setVehicles_max_velocity(vehiclesMax);
                    double vehiclesMin = resultSet.getDouble("min_velocity");
                    recording.setVehicles_min_velocity(vehiclesMin);
                    double vehiclesAvg = resultSet.getDouble("avg_velocity");
                    recording.setVehiclesAvgVelocity(vehiclesAvg);
                }

                st.setString(1, "pedestrian");
                resultSet = st.executeQuery();
                while (resultSet.next()) {
                    double pedestriansMax = resultSet.getDouble("max_velocity");
                    recording.setPedestrians_max_velocity(pedestriansMax);
                    double pedestriansMin = resultSet.getDouble("min_velocity");
                    recording.setPedestrians_min_velocity(pedestriansMin);
                    double pedestriansAvg = resultSet.getDouble("avg_velocity");
                    recording.setPedestriansAvgVelocity(pedestriansAvg);
                }
                st.setString(1, "two-wheeler");
                resultSet = st.executeQuery();
                while (resultSet.next()) {
                    double wheelersMax = resultSet.getDouble("max_velocity");
                    recording.setWheelers_max_velocity(wheelersMax);
                    double wheelersMin = resultSet.getDouble("min_velocity");
                    recording.setWheelers_min_velocity(wheelersMin);
                    double wheelersAvg = resultSet.getDouble("avg_velocity");
                    recording.setWheelersAvgVelocity(wheelersAvg);
                }

                contentProvider.put(resultSet.getInt(1), recording);
            }
            conn.commit();
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
