package com.mindhash.MindhashApp.model;

import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class MeasureRes {
	private Integer recordingId;
	private String date;
	private ArrayList<Measure> measureList;
	private ArrayList<Obj> objList;
	private HashMap<String, Integer> objNum;
	
	public MeasureRes() {}
	
	public MeasureRes(Integer recordingId, String date, ArrayList<Measure> measureList, ArrayList<Obj> objList, HashMap<String, Integer> objNum) {
		this.recordingId = recordingId;
		this.date = date;
		this.measureList = measureList;
		this.objList = objList;
		this.objNum = objNum;
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
	
	public ArrayList<Obj> getObjList() {
		return this.objList;
	}
	
	public HashMap<String, Integer> getObjNum() {
		return this.objNum;
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
	
	public void setObjList(ArrayList<Obj> objList) {
		this.objList = objList;
	}
	
	public void setObjNum(HashMap<String, Integer> objNum) {
		this.objNum = objNum;
	}
}
