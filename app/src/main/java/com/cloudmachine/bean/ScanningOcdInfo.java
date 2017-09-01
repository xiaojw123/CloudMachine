package com.cloudmachine.bean;

import java.io.Serializable;

public class ScanningOcdInfo implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -6015070370062971635L;
	private long deviceId;//设备ID
	private String gatewayId;//网关ID
	private String collectionDate;//数据采集时间
	private float dayOilPay;    //当天花费       
 	private float hourOilPay;  //小时花费
	private float dayOilConsume;//当天油耗
	private float hourOilConsume; //小时油耗
	private String systemDate;//数据入库时间
	private String suggestions;//建议
	private int score;//分数


	
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
	
	public float getDayOilPay() {
		return dayOilPay;
	}
	public void setDayOilPay(float dayOilPay) {
		this.dayOilPay = dayOilPay;
	}
	public float getHourOilPay() {
		return hourOilPay;
	}
	public void setHourOilPay(float hourOilPay) {
		this.hourOilPay = hourOilPay;
	}
	public float getDayOilConsume() {
		return dayOilConsume;
	}
	public void setDayOilConsume(float dayOilConsume) {
		this.dayOilConsume = dayOilConsume;
	}
	public float getHourOilConsume() {
		return hourOilConsume;
	}
	public void setHourOilConsume(float hourOilConsume) {
		this.hourOilConsume = hourOilConsume;
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
	
	
}
