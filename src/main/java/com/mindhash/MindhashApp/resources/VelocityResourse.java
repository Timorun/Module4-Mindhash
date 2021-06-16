package com.mindhash.MindhashApp.resources;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.mindhash.MindhashApp.DBConnectivity;
import com.mindhash.MindhashApp.model.Velocity;

@Path("/velocity")
public class VelocityResourse {
	
	@GET
	@Path("{recordingId}/{date}")
	@Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
	public Velocity getRecordingResource(@PathParam("recordingId") int recordingId,
										 @PathParam("date") String date) {
		Connection conn = DBConnectivity.createConnection();
    	PreparedStatement st = null;
    	ResultSet resultSet = null;
    	Velocity vel = new Velocity();
        try {
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            conn.setAutoCommit(false);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        try {
            //get average max & min velocity per object type
            String velocity = "select max(measurement.velocity) as max_vel, min(measurement.velocity) as min_vel, " +
            				  "avg(measurement.velocity) as avg_vel from measurement, object " + 
            		          "where measurement.velocity > 0 and measurement.object_id = object.object_id " + 
            				  "and object.object_type = ? and object.recording_id = ? and measurement.date = ?";
            st = conn.prepareStatement(velocity);
            st.setString(1, "vehicle");
            st.setInt(2, recordingId);
            st.setString(3, date);
            resultSet = st.executeQuery();
            while (resultSet.next()) {
                double vehiclesMax = resultSet.getDouble("max_vel");
                vel.setVehicles_max_velocity(vehiclesMax);
                double vehiclesMin = resultSet.getDouble("min_vel");
                vel.setVehicles_min_velocity(vehiclesMin);
                double vehiclesAvg = resultSet.getDouble("avg_vel");
                vel.setVehiclesAvgVelocity(vehiclesAvg);
            }
            resultSet.close();

            st.setString(1, "pedestrian");
            resultSet = st.executeQuery();
            while (resultSet.next()) {
                double pedestriansMax = resultSet.getDouble("max_vel");
                vel.setPedestrians_max_velocity(pedestriansMax);
                double pedestriansMin = resultSet.getDouble("min_vel");
                vel.setPedestrians_min_velocity(pedestriansMin);
                double pedestriansAvg = resultSet.getDouble("avg_vel");
                vel.setPedestriansAvgVelocity(pedestriansAvg);
            }
            st.setString(1, "two-wheeler");
            resultSet.close();
            
            resultSet = st.executeQuery();
            while (resultSet.next()) {
                double wheelersMax = resultSet.getDouble("max_vel");
                vel.setWheelers_max_velocity(wheelersMax);
                double wheelersMin = resultSet.getDouble("min_vel");
                vel.setWheelers_min_velocity(wheelersMin);
                double wheelersAvg = resultSet.getDouble("avg_vel");
                vel.setWheelersAvgVelocity(wheelersAvg);
            }
            resultSet.close();
            st.close();
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
        return vel;
	}
}
