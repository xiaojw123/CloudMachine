package com.cloudmachine.bean;

import java.io.Serializable;
import java.util.List;

public class McDeviceBasicsInfo implements Serializable{
	private String snId;
	private long id;
	private String[] devicePhoto;
	private String[] nameplatePhoto;
	private String category;

	public String getSnId() {
		return snId;
	}

	public void setSnId(String snId) {
		this.snId = snId;
	}

	private String brand;
	private String model;
	private String buyPlace;
	private String buyPrice;
	private String buyTime;
	private String company;
	private String contractNo;
	private String deviceName;
	private int deviceType = 1;
	private String factoryTime;
	private String insurer;
	private String insurerNo;
	private int isDelete;
	private String oldId;
	private String sellerContacts;
	private String sellerEmail;
	private String sellerMobi;
	private String sellerName;
	private String sellerPlace;
	private String serviceMobi;
	private String serviceName;
	private String servicePlace;
	private String filtNumber;
	private int checkType;
	private String discCode;
	private String hubCode;
	private String idCard;
	private String deviceMaster;
	private int type;
	private String updater;
	private int creator;
	private String createTime;
	private String updateTime;
	private McDeviceCircleFence circleFence;
	private McDeviceLocation location;
	private int typeId;//种类ID
	private int brandId;//品牌ID
	private int modelId;//型号ID
	private List<FenceInfo> fence;
	private int workStatus;
	private String license;
	private String enginePhoto;
	private String rackId;
	private double workTime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String[] getDevicePhoto() {
		return devicePhoto;
	}
	public void setDevicePhoto(String[] devicePhoto) {
		this.devicePhoto = devicePhoto;
	}
	public String[] getNameplatePhoto() {
		return nameplatePhoto;
	}
	public void setNameplatePhoto(String[] nameplatePhoto) {
		this.nameplatePhoto = nameplatePhoto;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getBuyPlace() {
		return buyPlace;
	}
	public void setBuyPlace(String buyPlace) {
		this.buyPlace = buyPlace;
	}
	public String getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getContractNo() {
		return contractNo;
	}
	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getFactoryTime() {
		return factoryTime;
	}
	public void setFactoryTime(String factoryTime) {
		this.factoryTime = factoryTime;
	}
	public String getInsurer() {
		return insurer;
	}
	public void setInsurer(String insurer) {
		this.insurer = insurer;
	}
	public String getInsurerNo() {
		return insurerNo;
	}
	public void setInsurerNo(String insurerNo) {
		this.insurerNo = insurerNo;
	}
	public String getOldId() {
		return oldId;
	}
	public void setOldId(String oldId) {
		this.oldId = oldId;
	}
	public String getSellerContacts() {
		return sellerContacts;
	}
	public void setSellerContacts(String sellerContacts) {
		this.sellerContacts = sellerContacts;
	}
	public String getSellerEmail() {
		return sellerEmail;
	}
	public void setSellerEmail(String sellerEmail) {
		this.sellerEmail = sellerEmail;
	}
	public String getSellerMobi() {
		return sellerMobi;
	}
	public void setSellerMobi(String sellerMobi) {
		this.sellerMobi = sellerMobi;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getSellerPlace() {
		return sellerPlace;
	}
	public void setSellerPlace(String sellerPlace) {
		this.sellerPlace = sellerPlace;
	}
	public String getServiceMobi() {
		return serviceMobi;
	}
	public void setServiceMobi(String serviceMobi) {
		this.serviceMobi = serviceMobi;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServicePlace() {
		return servicePlace;
	}
	public void setServicePlace(String servicePlace) {
		this.servicePlace = servicePlace;
	}
	public String getFiltNumber() {
		return filtNumber;
	}
	public void setFiltNumber(String filtNumber) {
		this.filtNumber = filtNumber;
	}
	public String getDiscCode() {
		return discCode;
	}
	public void setDiscCode(String discCode) {
		this.discCode = discCode;
	}
	public String getHubCode() {
		return hubCode;
	}
	public void setHubCode(String hubCode) {
		this.hubCode = hubCode;
	}
	public String getIdCard() {
		return idCard;
	}
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	public String getDeviceMaster() {
		return deviceMaster;
	}
	public void setDeviceMaster(String deviceMaster) {
		this.deviceMaster = deviceMaster;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getUpdater() {
		return updater;
	}
	public void setUpdater(String updater) {
		this.updater = updater;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getBuyPrice() {
		return buyPrice!=null?buyPrice:"";
	}
	public void setBuyPrice(String buyPrice) {
		this.buyPrice = buyPrice;
	}
	public int getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
	public int getCheckType() {
		return checkType;
	}
	public void setCheckType(int checkType) {
		this.checkType = checkType;
	}
	public int getCreator() {
		return creator;
	}
	public void setCreator(int creator) {
		this.creator = creator;
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
	public int getTypeId(){
		return typeId;
	}
	public void setTypeId(int typeId){
		this.typeId = typeId;
	}
	public int getBrandId(){
		return brandId;
	}
	public void setBrandId(int brandId){
		this.brandId = brandId;
	}
	public int getModelId(){
		return modelId;
	}
	public void setModelId(int modelId){
		this.modelId = modelId;
	}
	public List<FenceInfo> getFence(){
		return fence;
	}
	public void setFence(List<FenceInfo> fence){
		this.fence = fence;
	}
	public int getWorkStatus() {
		return workStatus;
	}
	public void setWorkStatus(int workStatus) {
		this.workStatus = workStatus;
	}
	public String getLicense() {
		return license;
	}
	public void setLicense(String license) {
		this.license = license;
	}
	public String getEnginePhoto() {
		return enginePhoto;
	}
	public void setEnginePhoto(String enginePhoto) {
		this.enginePhoto = enginePhoto;
	}
	public String getRackId() {
		return rackId;
	}
	public void setRackId(String rackId) {
		this.rackId = rackId;
	}
	public double getWorkTime() {
		return workTime;
	}
	public void setWorkTime(double workTime) {
		this.workTime = workTime;
	}

	public McDeviceCircleFence getCircleFence() {
		return circleFence;
	}

	public void setCircleFence(McDeviceCircleFence circleFence) {
		this.circleFence = circleFence;
	}
}
