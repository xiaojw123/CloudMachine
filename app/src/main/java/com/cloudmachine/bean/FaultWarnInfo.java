package com.cloudmachine.bean;

import java.io.Serializable;
import java.util.List;


public class FaultWarnInfo  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4370618045792938926L;
	private List<FaultWarnListInfo> reList;
	private long checkDate;
	private String pastTime;
	public String getPastTime() {
		return pastTime;
	}
	public void setPastTime(String pastTime) {
		this.pastTime = pastTime;
	}
	public long getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(long checkDate) {
		this.checkDate = checkDate;
	}
	
	public List<FaultWarnListInfo> getReList() {
		return reList;
	}
	public void setReList(List<com.cloudmachine.bean.FaultWarnListInfo> reList) {
		this.reList = reList;
	}
	
	
	
}
