package com.cloudmachine.bean;

import java.io.Serializable;


public class FaultWarnDitailsListInfo  implements Serializable{
	private String time;  
	private float data; 
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public float getData() {
		return data;
	}
	public void setData(float data) {
		this.data = data;
	}
	
	
}
