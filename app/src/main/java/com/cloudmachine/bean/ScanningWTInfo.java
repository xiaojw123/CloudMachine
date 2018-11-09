package com.cloudmachine.bean;

import java.io.Serializable;

public class ScanningWTInfo implements Serializable{

	private long deviceId;//设备ID
	private String deviceName;
	private float totalWorkTime;
	private String collectionDate;

	public long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public float getTotalWorkTime() {
		return totalWorkTime;
	}

	public void setTotalWorkTime(float totalWorkTime) {
		this.totalWorkTime = totalWorkTime;
	}

	public String getCollectionDate() {
		return collectionDate;
	}

	public void setCollectionDate(String collectionDate) {
		this.collectionDate = collectionDate;
	}
	//	private String gatewayId;//网关ID
//	private String collectionDate;//数据采集时间
//	private String systemDate;//数据入库时间
//	private float workTime;//工作时长
//	private int id;//编号
//
//
//	public long getDeviceId() {
//		return deviceId;
//	}
//	public void setDeviceId(long deviceId) {
//		this.deviceId = deviceId;
//	}
//
//	public String getGatewayId() {
//		return gatewayId;
//	}
//	public void setGatewayId(String gatewayId) {
//		this.gatewayId = gatewayId;
//	}
//	public String getCollectionDate() {
//		return collectionDate;
//	}
//	public void setCollectionDate(String collectionDate) {
//		this.collectionDate = collectionDate;
//	}
//	public String getSystemDate() {
//		return systemDate;
//	}
//	public void setSystemDate(String systemDate) {
//		this.systemDate = systemDate;
//	}
//
//	public float getDayWorkHour() {
//		return workTime;
//	}
//	public void setDayWorkHour(float dayWorkHour) {
//		this.workTime = dayWorkHour;
//	}
//	public int getId() {
//		return id;
//	}
//	public void setId(int id) {
//		this.id = id;
//	}
//
	
}
