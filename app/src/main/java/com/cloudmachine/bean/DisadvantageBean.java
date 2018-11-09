package com.cloudmachine.bean;

import java.io.Serializable;

public class DisadvantageBean implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7378899558237807783L;
	private String tagName;
	private String tagCode;
	private boolean isChecked;
	
	

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public String getTagCode() {
		return tagCode;
	}

	public void setTagCode(String tagCode) {
		this.tagCode = tagCode;
	}
}
