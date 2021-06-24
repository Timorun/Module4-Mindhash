package com.mindhash.MindhashApp.dao;

import com.mindhash.MindhashApp.DBConnectivity;
import com.mindhash.MindhashApp.ObjTypeNum;
import com.mindhash.MindhashApp.model.Obj;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ObjectDao {

    public static Map<Integer, Obj> getObject(int recordingId, String date) {
        Connection conn = DBConnectivity.createConnection();
        Map<Integer, Obj> contentProvider = new HashMap<>();
        try {
            String query = "select * from object where recording_id = ? and date = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, recordingId);
            st.setString(2, date);
            ResultSet resultSet = st.executeQuery();
            while (resultSet.next()) {
                Obj object = new Obj();
                object.setObjectId(resultSet.getInt("object_id"));
                object.setObjectType(resultSet.getString("object_type"));
                contentProvider.put(resultSet.getInt("object_id"), object);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contentProvider;
    }
    
    public static Map<String , Integer> getObjectNum(int recordingId, String date) {
        Connection conn = DBConnectivity.createConnection();
        Map<String, Integer> objNum = ObjTypeNum.getObjTypeNumMap();
        try {
            String query = "select * from object where recording_id = ? and date = ?";
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, recordingId);
            st.setString(2, date);
            ResultSet resultSet = st.executeQuery();
            while (resultSet.next()) {
                String type = resultSet.getString("object_type");
                if (objNum.containsKey(type)) {
                	int num = objNum.get(type);
                	objNum.put(type, num + 1);
                }
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return objNum;
    }

}

