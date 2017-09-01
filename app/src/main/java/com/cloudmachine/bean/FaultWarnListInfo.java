package com.cloudmachine.bean;

import java.io.Serializable;


public class FaultWarnListInfo  implements Serializable{
	private String item;  
	private String referRange;  
	private String testResult;  
	private boolean bexcess;
	private int id;
	
	
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	
	public String getReferRange() {
		return referRange;
	}
	public void setReferRange(String referRange) {
		this.referRange = referRange;
	}
	
	public String getTestResult() {
		return testResult;
	}
	public void setTestResult(String testResult) {
		this.testResult = testResult;
	}
	
	public boolean getBexcess() {
		return bexcess;
	}
	public void setBexcess(boolean bexcess) {
		this.bexcess = bexcess;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
