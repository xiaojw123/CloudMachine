package com.cloudmachine.bean;

import java.io.Serializable;

public class McDeviceScanningInfo implements Serializable{
	private boolean ocdpermission;
	private boolean edpermission;
	private boolean hsdpermission;
	private int oilLave;
	private int score;
	private int workStatus;
	private ScanningOcdInfo[] ocdList;//油耗
	private ScanningWTInfo[] whdList;//工作时间
	private ScanningAlarmInfo[] wdList;//故障预警
	private ScanningOilLevelInfo[] oilLevel;//当日油位
	private ScanningOilLevelInfo lastLevel;//最后一次油位
	
	
	public boolean getOcdpermission() {
		return ocdpermission;
	}
	public void setOcdpermission(boolean ocdpermission) {
		this.ocdpermission = ocdpermission;
	}
	public boolean getEdpermission() {
		return edpermission;
	}
	public void setEdpermission(boolean edpermission) {
		this.edpermission = edpermission;
	}
	public boolean getHsdpermission() {
		return hsdpermission;
	}
	public void setHsdpermission(boolean hsdpermission) {
		this.hsdpermission = hsdpermission;
	}
	public int getOilLave() {
		return oilLave;
	}
	public void setOilLave(int oilLave) {
		this.oilLave = oilLave;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public ScanningWTInfo[] getWhdList() {
		return whdList;
	}
	public void setHsdList(ScanningWTInfo[] whdList) {
		this.whdList = whdList;
	}
	public ScanningAlarmInfo[] getWdList() {
		return wdList;
	}
	public void setWdList(ScanningAlarmInfo[] wdList) {
		this.wdList = wdList;
	}
	public ScanningOcdInfo[] getOcdList() {
		return ocdList;
	}
	public void setOcdList(ScanningOcdInfo[] ocdList) {
		this.ocdList = ocdList;
	}
	public ScanningOilLevelInfo[] getOilLevel() {
		return oilLevel;
	}
	public void setOilLevel(ScanningOilLevelInfo[] oilLevel) {
		this.oilLevel = oilLevel;
	}
	public ScanningOilLevelInfo getLastLevel() {
		return lastLevel;
	}
	public void setLastLevel(ScanningOilLevelInfo lastLevel) {
		this.lastLevel = lastLevel;
	}
	public int getWorkStatus() {
		return workStatus;
	}
	public void setWorkStatus(int workStatus) {
		this.workStatus = workStatus;
	}
	
	
}
