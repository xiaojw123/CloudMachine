package com.cloudmachine.struc;

import java.io.Serializable;

public class ScanningEdInfo implements Serializable{
	
	
	private long deviceId;//设备ID
	private String gatewayId;//网关ID
	private String collectionDate;//数据采集时间
	private String systemDate;//数据入库时间
	private float airTemper;//进风口温度
	private float oilPress;//机油压力
	private int workstatus;//工作状态
	private float waterTemper;//冷却水温度
	private float shockDegree;//振动量
	private String suggestions;//建议
	private int score;//分数
	private int waterTemperStatus;//冷却水温度状态
	private int oilPressStatus; //机油压力状态


	
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
	public String getCollectionDate() {
		return collectionDate;
	}
	public void setCollectionDate(String collectionDate) {
		this.collectionDate = collectionDate;
	}
	public String getSystemDate() {
		return systemDate;
	}
	public void setSystemDate(String systemDate) {
		this.systemDate = systemDate;
	}
	
	public float getAirTemper() {
		return airTemper;
	}
	public void setAirTemper(float airTemper) {
		this.airTemper = airTemper;
	}
	public float getOilPress() {
		return oilPress;
	}
	public void setOilPress(float oilPress) {
		this.oilPress = oilPress;
	}
	public int getWorkstatus() {
		return workstatus;
	}
	public void setWorkstatus(int workstatus) {
		this.workstatus = workstatus;
	}
	public float getWaterTemper() {
		return waterTemper;
	}
	public void setWaterTemper(float waterTemper) {
		this.waterTemper = waterTemper;
	}
	public float getShockDegree() {
		return shockDegree;
	}
	public void setShockDegree(float shockDegree) {
		this.shockDegree = shockDegree;
	}
	
	public String getSuggestions() {
		return suggestions;
	}
	public void setSuggestions(String suggestions) {
		this.suggestions = suggestions;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	
	public int getWaterTemperStatus() {
		return waterTemperStatus;
	}
	public void setWaterTemperStatus(int waterTemperStatus) {
		this.waterTemperStatus = waterTemperStatus;
	}
	
	public int getOilPressStatus() {
		return oilPressStatus;
	}
	public void setOilPressStatus(int oilPressStatus) {
		this.oilPressStatus = oilPressStatus;
	}
	
	
}
