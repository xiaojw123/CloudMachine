package com.cloudmachine.struc;

import java.io.Serializable;

public class ResidentAddressInfo implements Serializable{

	private int id;
	private double lng ;
	private double lat ;
	private String province;
	private String city;
	private String district;
	private String position;
	private String title;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getProvince(){
		return province;
	}
	public void setProvince(String province){
		this.province = province;
	}
	public String getCity(){
		return city;
	}
	public void setCity(String city){
		this.city = city;
	}
	public String getDistrict(){
		return district;
	}
	public void setDistrict(String district){
		this.district = district;
	}
	public String getPosition(){
		return position;
	}
	public void setPosition(String position){
		this.position = position;
	}
	public String getTitle(){
		return title;
	}
	public void setTitle(String title){
		this.title = title;
	}
}
