package com.mindhash.MindhashApp.dao;

import com.mindhash.MindhashApp.DBConnectivity;
import com.mindhash.MindhashApp.ObjTypeNum;
import com.mindhash.MindhashApp.model.Measure;
import com.mindhash.MindhashApp.model.MeasureRes;
import com.mindhash.MindhashApp.model.Obj;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class MeasureDao {

    public static MeasureRes getMeasurement(Integer recordingId, String date) {
        Connection conn = DBConnectivity.createConnection();
        MeasureRes res = new MeasureRes();
        res.setRecordingId(recordingId);
        res.setDate(date);
        try {
            String query = "select distinct m.object_id, m.measurement_id, o.object_type, m.time, m.x, m.y, m.velocity, m.time_without_date \n"
            		+ "        from measurement m\n"
            		+ "        inner join object o \n"
            		+ "        on m.recording_id = ? and m.date = ? and m.recording_id = o.recording_id and m.date = o.date and m.object_id = o.object_id and m.time like '%T__:__:__.9%'\n"
            		+ "        order by m.measurement_id";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, recordingId);
            st.setString(2, date);
            ResultSet resultSet = st.executeQuery();
            ArrayList<Measure> mLi = new ArrayList<>();
            ArrayList<Obj> oLi = new ArrayList<>();
            HashSet<Integer> keySet = new HashSet<>();
            HashMap<String, Integer> objNum = ObjTypeNum.getObjTypeNumMap();
            while (resultSet.next()) {
                Measure m = new Measure();
                //measurement.setRecordingId(resultSet.getInt("recording_id"));
                int id = resultSet.getInt("object_id");
                String type = resultSet.getString("object_type");
                double x = resultSet.getDouble("x");
                double y = resultSet.getDouble("y");
                m.setObjectId(id);
                m.setObjectType(type);
                //measurement.setTime(resultSet.getString("time"));
                m.setX(x);
                m.setY(y);
                m.setY(resultSet.getDouble("y"));
                m.setVelocity(resultSet.getDouble("velocity"));
                m.setTimeWithoutDate(resultSet.getString("time_without_date"));
                /*measurement.setMeasurementId(resultSet.getInt("measurement_id"));
                contentProvider.put(resultSet.getInt("measurement_id"), measurement);*/
                if (keySet.add(id)) {
	                Obj obj = new Obj();
	                obj.setObjectId(id);
	                obj.setObjectType(type);
	                oLi.add(obj);
	                
	                if (objNum.containsKey(type)) {
	                	int num = objNum.get(type);
	                	objNum.put(type, num + 1);
	                }
                }
                mLi.add(m);
            }
            res.setMeasureList(mLi);
            res.setObjList(oLi);
            res.setObjNum(objNum);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    
    public static MeasureRes getMeasurementByTime(Integer recordingId, String date, String time) {
        Connection conn = DBConnectivity.createConnection();
        MeasureRes res = new MeasureRes();
        res.setRecordingId(recordingId);
        res.setDate(date);
        
        try {
            String query = "select distinct m.object_id, m.measurement_id, o.object_type, m.time, m.x, m.y, m.velocity, m.time_without_date \n"
            		+ "        from measurement m\n"
            		+ "        inner join object o \n"
            		+ "        on m.recording_id = ? and m.date = ? and m.recording_id = o.recording_id and m.date = o.date and m.object_id = o.object_id and m.time like '%T__:__:__.9%' and m.time like ? "
            		+ "        order by m.measurement_id";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, recordingId);
            st.setString(2, date);
            st.setString(3, "%T" + time + ":%");
            ResultSet resultSet = st.executeQuery();
            ArrayList<Measure> mLi = new ArrayList<>();
            ArrayList<Obj> oLi = new ArrayList<>();
            HashSet<Integer> keySet = new HashSet<>();
            HashMap<String, Integer> objNum = ObjTypeNum.getObjTypeNumMap();
            while (resultSet.next()) {
                Measure m = new Measure();
                //measurement.setRecordingId(resultSet.getInt("recording_id"));
                int id = resultSet.getInt("object_id");
                String type = resultSet.getString("object_type");
                m.setObjectId(id);
                m.setObjectType(type);
                //measurement.setTime(resultSet.getString("time"));
                m.setX(resultSet.getDouble("x"));
                m.setY(resultSet.getDouble("y"));
                m.setVelocity(resultSet.getDouble("velocity"));
                m.setTimeWithoutDate(resultSet.getString("time_without_date"));
                /*measurement.setMeasurementId(resultSet.getInt("measurement_id"));
                contentProvider.put(resultSet.getInt("measurement_id"), measurement);*/
                if (keySet.add(id)) {
	                Obj obj = new Obj();
	                obj.setObjectId(id);
	                obj.setObjectType(type);
	                oLi.add(obj);
	                
	                if (objNum.containsKey(type)) {
	                	int num = objNum.get(type);
	                	objNum.put(type, num + 1);
	                }
                }
                mLi.add(m);
            }
            res.setMeasureList(mLi);
            res.setObjList(oLi);
            res.setObjNum(objNum);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }
    
}
