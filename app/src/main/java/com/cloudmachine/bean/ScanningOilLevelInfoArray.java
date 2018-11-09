package com.cloudmachine.bean;

import java.io.Serializable;

public class ScanningOilLevelInfoArray implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7164648887989003701L;
	private ScanningOilLevelInfo[] oilLevelList;
	private ScanningOilLevelInfo lastLevel;


	
	public ScanningOilLevelInfo[] getOilLevel() {
		return oilLevelList;
	}
	public void setOilLevel(ScanningOilLevelInfo[] oilLevelList) {
		this.oilLevelList = oilLevelList;
	}
	public ScanningOilLevelInfo getLastLevel() {
		return lastLevel;
	}
	public void setLastLevel(ScanningOilLevelInfo lastLevel) {
		this.lastLevel = lastLevel;
	}
	
	
}
