package com.mindhash.MindhashApp.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Recording {
	private int recordindID;
	private double latitude;
	private double longitude;
	private int totalObjects;
	private int totalTwoWheelers;
	private int totalPedestrians;
	private int totalVehicles;
	private String date;
	private double pedestrians_min_velocity;
	private double pedestrians_max_velocity;
	private double wheelers_min_velocity;
	private double wheelers_max_velocity;
	private double vehicles_min_velocity;
	private double vehicles_max_velocity;
	private String startTime;
	private String endTime;
	private String resolution;
	private int frameRate;
	private double vehiclesAvgVelocity;
	private double pedestriansAvgVelocity;
	private double wheelersAvgVelocity;
	
	public Recording() {}
	
	public Recording(int recordindID, double latitude, double longitude, int totalObjects, 
			int totalTwoWheelers, int totalPedestrians, int totalVehicles, String date, double pedestrians_min_velocity, double pedestrians_max_velocity, double wheelers_min_velocity, double wheelers_max_velocity, double vehicles_min_velocity, double vehicles_max_velocity, String startTime, String endTime, String resolution, int frameRate, double vehiclesAvgVelocity, double pedestriansAvgVelocity, double wheelersAvgVelocity ) {
		this.recordindID = recordindID;
		this.latitude = latitude;
		this.longitude = longitude;
		this.totalObjects = totalObjects;
		this.totalTwoWheelers = totalTwoWheelers;
		this.totalPedestrians = totalPedestrians;
		this.totalVehicles = totalVehicles;
		this.date = date;
		this.pedestrians_min_velocity = pedestrians_min_velocity;
		this.pedestrians_max_velocity = pedestrians_max_velocity;
		this.wheelers_min_velocity = wheelers_min_velocity;
		this.wheelers_max_velocity = wheelers_max_velocity;
		this.vehicles_min_velocity = vehicles_min_velocity;
		this.vehicles_max_velocity = vehicles_max_velocity;
		this.startTime = startTime;
		this.endTime = endTime;
		this.resolution = resolution;
		this.frameRate = frameRate;
		this.vehiclesAvgVelocity = vehiclesAvgVelocity;
		this.pedestriansAvgVelocity = pedestriansAvgVelocity;
		this.wheelersAvgVelocity = wheelersAvgVelocity;
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

	public double getPedestrians_max_velocity() {
		return pedestrians_max_velocity;
	}

	public double getPedestrians_min_velocity() {
		return pedestrians_min_velocity;
	}

	public double getVehicles_max_velocity() {
		return vehicles_max_velocity;
	}

	public double getVehicles_min_velocity() {
		return vehicles_min_velocity;
	}

	public double getWheelers_max_velocity() {
		return wheelers_max_velocity;
	}

	public double getWheelers_min_velocity() {
		return wheelers_min_velocity;
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

	public double getVehiclesAvgVelocity() {
		return vehiclesAvgVelocity;
	}

	public double getPedestriansAvgVelocity() {
		return pedestriansAvgVelocity;
	}

	public double getWheelersAvgVelocity() {
		return wheelersAvgVelocity;
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

	public void setPedestrians_max_velocity(double pedestrians_max_velocity) {
		this.pedestrians_max_velocity = pedestrians_max_velocity;
	}

	public void setPedestrians_min_velocity(double pedestrians_min_velocity) {
		this.pedestrians_min_velocity = pedestrians_min_velocity;
	}

	public void setVehicles_max_velocity(double vehicles_max_velocity) {
		this.vehicles_max_velocity = vehicles_max_velocity;
	}

	public void setVehicles_min_velocity(double vehicles_min_velocity) {
		this.vehicles_min_velocity = vehicles_min_velocity;
	}

	public void setWheelers_max_velocity(double wheelers_max_velocity) {
		this.wheelers_max_velocity = wheelers_max_velocity;
	}

	public void setWheelers_min_velocity(double wheelers_min_velocity) {
		this.wheelers_min_velocity = wheelers_min_velocity;
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

	public void setVehiclesAvgVelocity(double vehiclesAvgVelocity) {
		this.vehiclesAvgVelocity = vehiclesAvgVelocity;
	}

	public void setPedestriansAvgVelocity(double pedestriansAvgVelocity) {
		this.pedestriansAvgVelocity = pedestriansAvgVelocity;
	}

	public void setWheelersAvgVelocity(double wheelersAvgVelocity) {
		this.wheelersAvgVelocity = wheelersAvgVelocity;
	}
}
