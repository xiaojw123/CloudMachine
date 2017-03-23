package com.cloudmachine.struc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class KindsResult implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6944966699127714338L;
	public int getKinds() {
		return kinds;
	}
	public void setKinds(int kinds) {
		this.kinds = kinds;
	}
	public List<HashMap<String, String>> getData() {
		return data;
	}
	public void setData(List<HashMap<String, String>> data) {
		this.data = data;
	}
	private int kinds;
	private List<HashMap<String, String>> data;
}
