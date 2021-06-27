package com.mindhash.MindhashApp.model;

public class Obj {
    private int objectId;
    private String objectType;
    private int points;
    private double length;
    private double width;
    private double x;
    private double y;

    public Obj() {}

    public Obj(int objectId, String objectType, int points, double length, double width, double x, double y) {
        this.objectId = objectId;
        this.objectType = objectType;
        this.points = points;
        this.length = length;
        this.width = width;
        this.x = x;
        this.y = y;
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
    
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
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
    
    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }
}

