package com.mindhash.MindhashApp.dao;

public class MeasurementDao {
    private String measurement;
    private FieldDao fields;
    private TagDao tags;
    private String time;

    public MeasurementDao(){}

    public MeasurementDao(String measurement, FieldDao fields, TagDao tags, String time) {
        this.measurement = measurement;
        this.fields = fields;
        this.tags = tags;
        this.time = time;
    }

    public String getMeasurement() {
        return measurement;
    }

    public void setMeasurement(String measurement) {
        this.measurement = measurement;
    }
    public FieldDao getField() {
        return fields;
    }

    public void setField(FieldDao fields) {
        this.fields = fields;
    }

    public TagDao getTag() {
        return tags;
    }

    public void setTag(TagDao tags) {
        this.tags = tags;
    }

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }

}
