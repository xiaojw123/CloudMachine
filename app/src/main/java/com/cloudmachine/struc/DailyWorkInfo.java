package com.cloudmachine.struc;

import java.io.Serializable;


public class DailyWorkInfo  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1071131036199077539L;
	private long deviceId; 
	private float workTime; 
	private String endTime; 
	private String startTime; 
	private String date;
	public long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}
	public float getWorkTime() {
		return workTime;
	}
	public void setWorkTime(float workTime) {
		this.workTime = workTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
