package com.mindhash.MindhashApp.dao;

import com.mindhash.MindhashApp.DBConnectivity;
import com.mindhash.MindhashApp.model.Obj;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public enum ObjectDao {
    instance;

    private Map<Integer, Obj> contentProvider = new HashMap<>();

    private ObjectDao() {
        Connection conn = DBConnectivity.createConnection();

        try {
            String query = "select * from object";
            PreparedStatement st = conn.prepareStatement(query);
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
    }

    public Map<Integer, Obj> getModel(){
        return contentProvider;
    }
}

