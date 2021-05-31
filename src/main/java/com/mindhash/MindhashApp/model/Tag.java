package com.mindhash.MindhashApp.model;

public class Tag {
    private int object_id;
    private String object_type;

    public Tag() {}

    public Tag(int object_id, String object_type) {
        this.object_id = object_id;
        this.object_type = object_type;
    }
    public int getObject_id() {
        return object_id;
    }

    public void setObject_id(int object_id) {
        this.object_id = object_id;
    }

    public String getObject_type() {
        return object_type;
    }

    public void setObject_type(String object_type) {
        this.object_type= object_type;
    }

}
