package com.cloudmachine.bean;

import java.io.Serializable;

public class ScanningAlarmInfo implements Serializable{
	
	
	private long deviceId;//设备ID
	private String gatewayId;//网关ID
	private long collectionDate;//数据采集时间
	private String systemDate;//数据入库时间
	private String warning;//预警
	private int id;//编号
	
	
	public long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}
	
	public String getGatewayId() {
		return gatewayId;
	}
	public void setGatewayId(String gatewayId) {
		this.gatewayId = gatewayId;
	}
	public long getCollectionDate() {
		return collectionDate;
	}
	public void setCollectionDate(long collectionDate) {
		this.collectionDate = collectionDate;
	}
	public String getSystemDate() {
		return systemDate;
	}
	public void setSystemDate(String systemDate) {
		this.systemDate = systemDate;
	}
	
	public String getWarning() {
		return warning;
	}
	public void setWarning(String warning) {
		this.warning = warning;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
}
