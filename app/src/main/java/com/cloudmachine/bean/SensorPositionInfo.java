package com.cloudmachine.bean;

import java.io.Serializable;

public class SensorPositionInfo implements Serializable{
	private int id;
	private String location;
	private boolean isClick;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public boolean getIsClick(){
		return isClick;
	}
	public void setIsClick(boolean isClick){
		this.isClick = isClick;
	}
	
}
