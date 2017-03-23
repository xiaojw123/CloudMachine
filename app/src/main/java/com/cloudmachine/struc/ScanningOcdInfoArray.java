package com.cloudmachine.struc;

import java.io.Serializable;

public class ScanningOcdInfoArray implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7164648887989003701L;
	private ScanningOcdInfo[] ocdList;
	
	public ScanningOcdInfo[] getOcdList() {
		return ocdList;
	}
	public void setOcdList(ScanningOcdInfo[] ocdList) {
		this.ocdList = ocdList;
	}
	
	
}
