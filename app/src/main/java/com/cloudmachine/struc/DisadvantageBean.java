package com.cloudmachine.struc;

import java.io.Serializable;

public class DisadvantageBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7378899558237807783L;
	private String code_NAME;
	private String code;
	private boolean isChecked;
	
	

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getCode_NAME() {
		return code_NAME;
	}

	public void setCode_NAME(String code_NAME) {
		this.code_NAME = code_NAME;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
