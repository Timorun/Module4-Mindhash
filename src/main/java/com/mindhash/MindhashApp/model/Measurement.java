package com.mindhash.MindhashApp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Measurement {
    private String measurement;
    private Field fields;
    private Tag tags;
    private String time;

    public Measurement() {}

    public Measurement(String measurement, Field fields, Tag tags, String time) {
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

    @JsonProperty("fields")
    public Field getField() {
        return fields;
    }

    public void setField(Field fields) {
        this.fields = fields;
    }

    @JsonProperty("tags")
    public Tag getTag() { return tags; }

    public void setTag(Tag tags) { this.tags = tags; }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }
}
