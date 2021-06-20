package com.mindhash.MindhashApp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindhash.MindhashApp.model.Measurement;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.Iterator;
import java.util.List;


@MultipartConfig
@WebServlet ("/uploadServlet")
public class FileUploadDBServlet extends HttpServlet {


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        //obtain the uploaded file
        Part filePart = request.getPart("file");
        //get values of latitude, longitude, framerate and resolution from user input
        double latitude = Double.parseDouble(request.getParameter("latitude"));
        double longitude = Double.parseDouble(request.getParameter("longitude"));
        String resolution = request.getParameter("resolution");
        int framerate = Integer.parseInt(request.getParameter("framerate"));
        //get input stream of the uploaded file
        InputStream stream = filePart.getInputStream();
        StringBuilder sb = new StringBuilder(stream.available());
        char[] buffer = new char[4096];
        try {
            InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            for (int cnt; (cnt = reader.read(buffer)) > 0; )
                sb.append(buffer, 0, cnt);
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
            String sql = "select coalesce(max(testmindhash.recording_id), 0) as recording_id " + "from testmindhash";
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
            preparedStatement = conn.prepareStatement("insert into testmindhash(object_id, object_type, points, length, width, x, y, velocity, ma_velocity, measurement, time, recording_id) values ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
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
            String sql2 = "select max(testmindhash.time) as end_time, " +
                    "min(testmindhash.time) as start_time from testmindhash " + "where testmindhash.recording_id = ?";
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
            preparedStatement = conn.prepareStatement("insert into testrecording(latitude, longitude, date, start_time, end_time, resolution, framerate, recording_id) values (?, ?, ?, ?, ?, ?, ?, ?)");
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
            String sql3 = "Select distinct object_id, object_type,  points, length, width from testmindhash "  + "where testmindhash.recording_id = ?";;
            PreparedStatement st = conn.prepareStatement(sql3);
            st.setInt(1, recording_id);
            resultSet = st.executeQuery();

            /*Insert records into
            (test)object table */
            preparedStatement = conn.prepareStatement("insert into testobject(object_id, recording_id, object_type, points, length, width, date) values (?, ?, ?, ?, ?, ?, ?)");

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
            preparedStatement = conn.prepareStatement("insert into testmeasurement(recording_id, object_id, timestamp, x, y, velocity, ma_velocity, date, time) values (?, ?, ?, ?, ?, ?, ?, ?, ?)");
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

            //Insert records into test(measurement) table
            /*String timestamp = null;
            double x = 0;
            double y = 0;
            double velocity = 0;
            double ma_velocity = 0;
            String time = null;
            String sql4 = "Select object_id, time, x, y, velocity, ma_velocity from testmindhash";
            st = conn.createStatement();
            resultSet = st.executeQuery(sql4); */



            /*while (resultSet.next()) {
                object_id = resultSet.getInt("object_id");
                timestamp = resultSet.getString("time");
                x = resultSet.getDouble("x");
                y = resultSet.getDouble("y");
                velocity = resultSet.getDouble("velocity");
                ma_velocity = resultSet.getDouble("ma_velocity");
                time = timestamp.split("T")[1];

                preparedStatement.setInt(1, recording_id);
                preparedStatement.setInt(2, object_id);
                preparedStatement.setString(3, timestamp);
                preparedStatement.setDouble(4, x);
                preparedStatement.setDouble(5, y);
                preparedStatement.setDouble(6, velocity);
                preparedStatement.setDouble(7, ma_velocity);
                preparedStatement.setString(8, date);
                preparedStatement.setString(9, time);
                preparedStatement.executeUpdate();
            } */
            /*preparedStatement.close();
            st.close();
            resultSet.close();  */

            conn.commit();
            conn.close();

            message = "Records inserted successfully";
        } catch (SQLException e) {
            message = "Error " + e.getMessage();
        }
        request.setAttribute("Message", message);
        request.getRequestDispatcher("message.jsp").forward(request, response);
     }

}
