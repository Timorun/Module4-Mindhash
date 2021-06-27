package com.mindhash.MindhashApp.resources;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataParam;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindhash.MindhashApp.DBConnectivity;
import com.mindhash.MindhashApp.model.Measurement;

@Path("/upload")
public class DataUploadDB {

    @POST
    @Path("/json")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response fileUpload(
    						@FormDataParam("file") InputStream stream,
    						@FormDataParam("latitude") double latitude,
    						@FormDataParam("longitude") double longitude,
    						@FormDataParam("resolution") String resolution,
    						@FormDataParam("framerate") int framerate) throws IOException {

        StringBuilder sb = new StringBuilder(stream.available());
        char[] buffer = new char[4096];
        try {
            InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            for (int cnt; (cnt = reader.read(buffer)) > 0;) {
                sb.append(buffer, 0, cnt);
            }
        } finally {
            stream.close();
        }
        String singleString = sb.toString();

        ObjectMapper objectMapper = new ObjectMapper();
        //convert JSON to java object
        List<Measurement> list = objectMapper.readValue(singleString, new TypeReference<List<Measurement>>() {
        });

        Connection conn = DBConnectivity.createConnection();
        String message = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            conn.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
            conn.setAutoCommit(false);

            /*Create recording id for the records being
            inserted in (test)mindhash table */
            int recording_id = 0;
            //Get current maximum recording_id in (test)mindhash table, if null, set max recording_id to 0
            String sql = "select coalesce(max(mindhash.recording_id), 0) as recording_id " + "from mindhash";
            preparedStatement = conn.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                //set recording_id to "incremented by one" maximum recording_id
                recording_id = resultSet.getInt("recording_id") + 1;
            }
            resultSet.close();
            preparedStatement.close();

            /*Insert records into
            (test)mindhash table */
            preparedStatement = conn.prepareStatement("insert into mindhash(object_id, object_type, points, length, width, x, y, velocity, ma_velocity, measurement, time, recording_id) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
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
                preparedStatement.setInt(12, recording_id);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            preparedStatement.close();

            /*Query start, date and end time
            of the recording for test(recording) table*/
            String sql2 = "select max(mindhash.time) as end_time, " +
                    "min(mindhash.time) as start_time from mindhash " + "where mindhash.recording_id = ?";
            preparedStatement = conn.prepareStatement(sql2);
            preparedStatement.setInt(1, recording_id);
            resultSet = preparedStatement.executeQuery();
            String start_time = null, end_time = null, date;
            while (resultSet.next()) {
                start_time = resultSet.getString("start_time");
                end_time = resultSet.getString("end_time");
            }
            assert start_time != null;
            String[] arr1 = start_time.split("T");
            String[] arr2 = end_time.split("T");
            date = arr1[0];
            start_time = arr1[1];
            end_time = arr2[1];

            preparedStatement.close();
            resultSet.close();

            /*Insert records into
            (test)recording table*/
            preparedStatement = conn.prepareStatement("insert into recording(latitude, longitude, date, start_time, end_time, resolution, framerate, recording_id) values (?, ?, ?, ?, ?, ?, ?, ?)");
            //latitude, longitude, resolution and framerate attributes are taken from user input
            preparedStatement.setDouble(1, latitude);
            preparedStatement.setDouble(2, longitude);
            preparedStatement.setString(3, date);
            preparedStatement.setString(4, start_time);
            preparedStatement.setString(5, end_time);
            preparedStatement.setString(6, resolution);
            preparedStatement.setInt(7, framerate);
            preparedStatement.setInt(8, recording_id);
            preparedStatement.executeUpdate();

            preparedStatement.close();

            /*Query distinct object_id, object_type, points,
            length and width for (test)object table*/
            int object_id = 0;
            String object_type = null;
            int points = 0;
            double length = 0;
            double width = 0;
            String sql3 = "Select distinct object_id, object_type,  points, length, width from mindhash "  + "where mindhash.recording_id = ?";;
            PreparedStatement st = conn.prepareStatement(sql3);
            st.setInt(1, recording_id);
            resultSet = st.executeQuery();

            /*Insert records into
            (test)object table */
            preparedStatement = conn.prepareStatement("insert into object(object_id, recording_id, object_type, points, length, width, date) values (?, ?, ?, ?, ?, ?, ?)");

            while (resultSet.next()) {
                object_id = resultSet.getInt("object_id");
                object_type = resultSet.getString("object_type");
                points = resultSet.getInt("points");
                length = resultSet.getDouble("length");
                width = resultSet.getDouble("width");

                preparedStatement.setInt(1, object_id);
                preparedStatement.setInt(2, recording_id);
                preparedStatement.setString(3, object_type);
                preparedStatement.setInt(4, points);
                preparedStatement.setDouble(5, length);
                preparedStatement.setDouble(6, width);
                preparedStatement.setString(7, date);
                preparedStatement.executeUpdate();
            }
            preparedStatement.close();
            st.close();
            resultSet.close();

            /*Insert records into
            (test)measurement table*/
            it = list.iterator();
            preparedStatement = conn.prepareStatement("insert into measurement(recording_id, object_id, time, x, y, velocity, ma_velocity, date, time_without_date) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            while (it.hasNext()) {
                Measurement m = it.next();
                preparedStatement.setInt(1, recording_id);
                preparedStatement.setInt(2, m.getTag().getObject_id());
                preparedStatement.setString(3, m.getTime());
                preparedStatement.setDouble(4, m.getField().getX());
                preparedStatement.setDouble(5, m.getField().getY());
                preparedStatement.setDouble(6, m.getField().getVelocity());
                preparedStatement.setDouble(7, m.getField().getMa_velocity());
                preparedStatement.setString(8, date);
                preparedStatement.setString(9, m.getTime().split("T")[1]);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            preparedStatement.close();

            conn.commit();
            conn.close();

            message = "Records inserted successfully";
        } catch (SQLException e) {
            message = e.getMessage();
            return Response.
            		status(Response.Status.INTERNAL_SERVER_ERROR)
            		.entity(message)
            		.build();
        }
        
        return Response
        		.status(Response.Status.OK)
        		.entity(message)
        		.build();
     }

}
