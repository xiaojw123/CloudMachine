package com.cloudmachine.bean;

import java.io.Serializable;

public class ScanningOilLevelInfo implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3228331376777603797L;
	/**
	 * 
	 */
	private String time;//时间
	private int level;//油位


	
	
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	
	
}
