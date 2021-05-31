package com.mindhash.MindhashApp.model;

public class Recording {
	private int recordindID;
	private double latitude;
	private double longitude;
	private int totalObjects;
	private int totalTwoWheelers;
	private int totalPedestrians;
	private int totalVehicles;
	private String date;
	
	public Recording() {}
	
	public Recording(int recordindID, double latitude, double longitude, int totalObjects, int totalTwoWheelers,
			int totalPedestrians, int totalVehicles, String date) {
		this.recordindID = recordindID;
		this.latitude = latitude;
		this.longitude = longitude;
		this.totalObjects = totalObjects;
		this.totalTwoWheelers = totalTwoWheelers;
		this.totalPedestrians = totalPedestrians;
		this.totalVehicles = totalVehicles;
		this.date = date;
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
	
	public int getTotalObjects() {
		return totalObjects;
	}
	
	public int getTotalTwoWheelers() {
		return totalTwoWheelers;
	}
	
	public int getTotalPedestrians() {
		return totalPedestrians;
	}
	
	public int getTotalVehicles() {
		return totalVehicles;
	}
	
	public String getDate() {
		return date;
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
	
	public void setTotalObjects(int totalObjects) {
		this.totalObjects = totalObjects;
	}
	
	public void setTotalTwoWheelers(int totalTwoWheelers) {
		this.totalTwoWheelers = totalTwoWheelers;
	}
	
	public void setTotalPedestrians(int totalPedestrians) {
		this.totalPedestrians = totalPedestrians;
	}
	
	public void setTotalVehicles(int totalVehicles) {
		this.totalVehicles = totalVehicles;
	}
	
	public void setDate(String date) {
		this.date = date;
	}
}
