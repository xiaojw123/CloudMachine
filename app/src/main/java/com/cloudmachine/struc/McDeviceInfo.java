package com.cloudmachine.struc;

import java.io.Serializable;

public class McDeviceInfo implements Serializable{
	/**
	 * 
	 */
	private int oilLave;
	private float workTime;

	public double getOilLave() {
		return oilLave;
	}

	public void setOilLave(int oilLave) {
		this.oilLave = oilLave;
	}

	public float getWorkTime() {
		return workTime;
	}

	public void setWorkTime(float workTime) {
		this.workTime = workTime;
	}

	private long id;
	private String name;
	private String category;
	private String model;
	private String brand;
	private String image;
	private String macAddress;
	private int checkStatus;
	private int workStatus;// 0表示不工作  1表示工作
	private int type; //1 2 4
	private String pk_PROD_DEF;
    private String pk_BRAND;
    private String pk_VHCL_MATERIAL;
    private String rackId;
	public String getPk_PROD_DEF() {
		return pk_PROD_DEF;
	}
	public void setPk_PROD_DEF(String pk_PROD_DEF) {
		this.pk_PROD_DEF = pk_PROD_DEF;
	}
	public String getPk_BRAND() {
		return pk_BRAND;
	}
	public void setPk_BRAND(String pk_BRAND) {
		this.pk_BRAND = pk_BRAND;
	}
	public String getPk_VHCL_MATERIAL() {
		return pk_VHCL_MATERIAL;
	}
	public void setPk_VHCL_MATERIAL(String pk_VHCL_MATERIAL) {
		this.pk_VHCL_MATERIAL = pk_VHCL_MATERIAL;
	}
	public String getRackId() {
		return rackId;
	}
	public void setRackId(String rackId) {
		this.rackId = rackId;
	}
	private McDeviceLocation location;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	public int getCheckStatus() {
		return checkStatus;
	}
	public void setCheckStatus(int checkStatus) {
		this.checkStatus = checkStatus;
	}
	public int getWorkStatus() {
		return workStatus;
	}
	public void setWorkStatus(int workStatus) {
		this.workStatus = workStatus;
	}
	public int getType(){
		return type;
	}
	public void setType(int type){
		this.type = type;
	}
	public McDeviceLocation getLocation() {
		return location;
	}
	public void setLocation(McDeviceLocation location) {
		this.location = location;
	}
	@Override
	public String toString() {
		return "McDeviceInfo [id=" + id + ", name=" + name + ", model=" + model
				+ ", brand=" + brand + ", image=" + image + ", macAddress="
				+ macAddress + ", checkStatus=" + checkStatus + ", workStatus="
				+ workStatus + ", type=" + type + ", location=" + location
				+ "]";
	}
	
}
