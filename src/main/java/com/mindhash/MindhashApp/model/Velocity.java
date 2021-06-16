package com.mindhash.MindhashApp.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Velocity {
	private double pedestrians_min_velocity;
	private double pedestrians_max_velocity;
	private double wheelers_min_velocity;
	private double wheelers_max_velocity;
	private double vehicles_min_velocity;
	private double vehicles_max_velocity;
	private double vehiclesAvgVelocity;
	private double pedestriansAvgVelocity;
	private double wheelersAvgVelocity;
	
	public Velocity() {}
	
	public Velocity(double pedestrians_min_velocity, double pedestrians_max_velocity, double wheelers_min_velocity, 
			double wheelers_max_velocity, double vehicles_min_velocity, double vehicles_max_velocity, 
			double vehiclesAvgVelocity, double pedestriansAvgVelocity, double wheelersAvgVelocity) {
		this.pedestrians_min_velocity = pedestrians_min_velocity;
		this.pedestrians_max_velocity = pedestrians_max_velocity;
		this.wheelers_min_velocity = wheelers_min_velocity;
		this.wheelers_max_velocity = wheelers_max_velocity;
		this.vehicles_min_velocity = vehicles_min_velocity;
		this.vehicles_max_velocity = vehicles_max_velocity;
		this.vehiclesAvgVelocity = vehiclesAvgVelocity;
		this.pedestriansAvgVelocity = pedestriansAvgVelocity;
		this.wheelersAvgVelocity = wheelersAvgVelocity;
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

	public double getVehiclesAvgVelocity() {
		return vehiclesAvgVelocity;
	}

	public double getPedestriansAvgVelocity() {
		return pedestriansAvgVelocity;
	}

	public double getWheelersAvgVelocity() {
		return wheelersAvgVelocity;
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
