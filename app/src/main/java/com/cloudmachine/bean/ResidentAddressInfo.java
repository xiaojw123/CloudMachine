package com.cloudmachine.bean;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
/**
 * Entity mapped to table "NOTE".
 */
public class ResidentAddressInfo   extends DataSupport implements Serializable{
	private static final  long serialVersionUID=132l;
	private long _id;
	private double lng ;
	private double lat ;
	private String province;
	private String city;
	private String district;
	private String position;
	private String title;

	public ResidentAddressInfo(long _id, double lng, double lat, String province,
			String city, String district, String position, String title) {
		this._id = _id;
		this.lng = lng;
		this.lat = lat;
		this.province = province;
		this.city = city;
		this.district = district;
		this.position = position;
		this.title = title;
	}

	public ResidentAddressInfo() {
	}
	public ResidentAddressInfo(long _id){
		this._id=_id;
	}


	public long getId() {
		return _id;
	}
	public void setId(long id) {
		this._id = id;
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

	public long get_id() {
		return this._id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

}
