package com.mindhash.MindhashApp.model;

public class Obj {
    private int objectId;
    private String objectType;
    private int points;
    private double length;
    private double width;

    public Obj() {}

    public Obj(int objectId, String objectType, int points, double length, double width) {
        this.objectId = objectId;
        this.objectType = objectType;
        this.points = points;
        this.length = length;
        this.width = width;
    }

    public int getObjectId() {
        return objectId;
    }
    public String getObjectType() {
        return objectType;
    }

    public int getPoints() {
        return points;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setWidth(double width) {
        this.width = width;
    }
}

