package com.mindhash.MindhashApp.model;

public class Field {
    private int points;
    private double length;
    private double width;
    private double x;
    private double y;
    private double velocity;
    private double ma_velocity;

   public Field() {}

    public Field(int points, double length, double width, double x, double y, double velocity, double ma_velocity) {
       this.points = points;
       this.length = length;
       this.width = width;
       this.x = x;
       this.y = y;
       this.velocity = velocity;
       this.ma_velocity = ma_velocity;
    }
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width= width;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getMa_velocity() {
        return ma_velocity;
    }

    public void setMa_velocity(double ma_velocity) {
        this.ma_velocity = ma_velocity;
    }


}
