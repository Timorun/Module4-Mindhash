package com.mindhash.MindhashApp.model;

public class Measure {
    private int recordingId;
    private int objectId;
    private String time;
    private double x;
    private double y;
    private double velocity;
    private double maVelocity;
    private String timeWithoutDate;
    private int measurementId;

    public Measure(){}

    public Measure(int recordingId, int objectId, String time, double x, double y, double velocity, double maVelocity, String timeWithoutDate, int measurementId) {
        this.recordingId = recordingId;
        this.objectId = objectId;
        this.time = time;
        this.x = x;
        this.y = y;
        this.velocity = velocity;
        this.maVelocity = maVelocity;
        this.timeWithoutDate = timeWithoutDate;
        this.measurementId = measurementId;
    }

    public int getObjectId() { return objectId; }

    public int getRecordingId() { return recordingId; }

    public String getTime() { return time; }

    public double getX() { return x; }

    public double getY() { return y; }

    public double getVelocity() { return velocity; }

    public double getMaVelocity() { return maVelocity; }

    public String getTimeWithoutDate() { return timeWithoutDate; }

    public int getMeasurementId() { return measurementId; }

    public void setObjectId(int objectId) { this.objectId = objectId; }

    public void setRecordingId(int recordingId) { this.recordingId = recordingId; }

    public void setTime(String time) { this.time = time; }

    public void setX(double x) { this.x = x; }

    public void setY(double y) { this.y = y; }

    public void setVelocity(double velocity) { this.velocity = velocity; }

    public void setMaVelocity(double maVelocity) { this.maVelocity = maVelocity; }

    public void setTimeWithoutDate(String timeWithoutDate) { this.timeWithoutDate = timeWithoutDate; }

    public void setMeasurementId(int measurementId) { this.measurementId = measurementId; }
}
