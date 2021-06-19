package com.mindhash.MindhashApp.model;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MeasureRes {
	private Integer recordingId;
	private String date;
	private ArrayList<Measure> measureList;
	private HashMap<Integer, String> objectList;
	
	public MeasureRes() {}
	
	public MeasureRes(Integer recordingId, String date, ArrayList<Measure> measureList, HashMap<Integer, String> objectList) {
		this.recordingId = recordingId;
		this.date = date;
		this.measureList = measureList;
		this.objectList = objectList;
	}
	
	public Integer getRecordingId() {
		return this.recordingId;
	}
	
	public String getDate() {
		return this.date;
	}
	
	public ArrayList<Measure> getMeasureList() {
		return this.measureList;
	}
	
	public HashMap<Integer, String> getObjectList() {
		return this.objectList;
	}
	
	public void setRecordingId(Integer recordingId) {
		this.recordingId = recordingId;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
	
	public void setMeasureList(ArrayList<Measure> measureList) {
		this.measureList = measureList;
	}
	
	public void setObjectList(HashMap<Integer, String> objectList) {
		this.objectList = objectList;
	}
}
