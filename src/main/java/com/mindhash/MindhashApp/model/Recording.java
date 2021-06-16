package com.mindhash.MindhashApp.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Recording {
	private int recordindID;
	private double latitude;
	private double longitude;
	private String date;
	private String startTime;
	private String endTime;
	private String resolution;
	private int frameRate;
	
	public Recording() {}
	
	public Recording(int recordindID, double latitude, double longitude, String date, 
			String startTime, String endTime, String resolution, int frameRate) {
		this.recordindID = recordindID;
		this.latitude = latitude;
		this.longitude = longitude;
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.resolution = resolution;
		this.frameRate = frameRate;
	}
	
	public int getRecordingID() {
		return recordindID;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public String getDate() {
		return date;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getResolution() {
		return resolution;
	}

	public int getFrameRate() {
		return frameRate;
	}

	public void setRecordingID(int recordindID) {
		this.recordindID = recordindID;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public void setDate(String date) {
		this.date = date;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setResolution(String resolution) {
		this.resolution = resolution;
	}

	public void setFrameRate(int frameRate) {
		this.frameRate = frameRate;
	}
}
