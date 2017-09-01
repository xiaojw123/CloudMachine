package com.cloudmachine.bean;

import java.io.Serializable;

public class LocationXY implements Serializable{
	private double  x;//lng	  117.375938
	private double y ;//lat   31.921795
	
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
}
