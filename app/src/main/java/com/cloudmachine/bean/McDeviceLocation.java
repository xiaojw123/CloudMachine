package com.cloudmachine.bean;

import android.text.TextUtils;

import org.w3c.dom.Text;

import java.io.Serializable;

public class McDeviceLocation implements Serializable{
	private String position;
	private double lat ;
	private double lng ;
	private long deviceId;
	private String province;
	private String collectionTime;

	public String getCollectionTime() {
        if (TextUtils.isEmpty(collectionTime)){
            return "暂无";
        }
		return collectionTime;
	}

	public void setCollectionTime(String collectionTime) {
		this.collectionTime = collectionTime;
	}

	public String getPosition() {
		if (TextUtils.isEmpty(position)){
			position="暂无";

		}
		return position;
	}
	public long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public void setPosition(String position) {
		this.position = position;
	}
	
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
}
