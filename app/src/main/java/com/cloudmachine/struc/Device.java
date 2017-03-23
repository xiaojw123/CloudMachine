package com.cloudmachine.struc;

import java.math.BigDecimal;
import java.util.List;

/**
 * 设备基础信息有效表
 * @author WeiSky
 *
 */
public class Device {

	private static final long serialVersionUID = -4839004424286075675L;
	
	private String deviceName;//设备名称
	private String category;//品类
	private String brand;//品牌
	private String model;//型号
	private String factoryTime;//新机出厂时间
	private int deviceType;//机器类型  1：新机  2：二手
	private String buyTime;//购机时间
	private String buyPlace;//购机所在地区
	private BigDecimal buyPrice;//购机价格
	private String sellerName;//卖家名称
	private String sellerPlace;//卖家地址
	private String sellerContacts;//卖家联系人姓名
	private String sellerMobi;//卖家联系人手机
	private String sellerEmail;//卖家联系人邮箱
	private String insurer;//保险公司
	private String insurerNo;//保险单号
	private String company;//融资银行或公司
	private String contractNo;//融资协议合同号
	private String serviceName;//维保单位名称
	private String serviceMobi;//维保单位联系电话
	private String servicePlace;//维保单位地址
	private Long oldId;//原来的设备ID号  特指该机器为二手
	private int isDelete;//逻辑有效 1：有效 0：无效
	private int checkType;//审核状态 1：审核中 2：审核通过  3：审核拒绝
	private Long deviceId;//设备基础信息有效表id
	private int type;//待审核数据的类型 1:新增设备  2：修改设备信息  3：过户   4：删除
	private String idCard;//身份证号
	private String discCode;//设备识别号
	private String hubCode;//HUB设备号
	private List<String> idCardPhoto;
	private List<String> nameplatePhoto;
	private List<String> devicePhoto;
	public List<String> getIdCardPhoto() {
		return idCardPhoto;
	}
	public void setIdCardPhoto(List<String> idCardPhoto) {
		this.idCardPhoto = idCardPhoto;
	}
	public List<String> getNameplatePhoto() {
		return nameplatePhoto;
	}
	public void setNameplatePhoto(List<String> nameplatePhoto) {
		this.nameplatePhoto = nameplatePhoto;
	}
	public List<String> getDevicePhoto() {
		return devicePhoto;
	}
	public void setDevicePhoto(List<String> devicePhoto) {
		this.devicePhoto = devicePhoto;
	}
	public int getCheckType() {
		return checkType;
	}
	public void setCheckType(int checkType) {
		this.checkType = checkType;
	}
	public Long getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getFiltNumber() {
		return filtNumber;
	}
	public void setFiltNumber(String filtNumber) {
		this.filtNumber = filtNumber;
	}
	private String filtNumber;//网关编号
	
	//临时字段
	private String deviceMaster;//机主名字
	
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
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public BigDecimal getBuyPrice() {
		return buyPrice;
	}
	public void setBuyPrice(BigDecimal buyPrice) {
		this.buyPrice = buyPrice;
	}
	public String getDeviceMaster() {
		return deviceMaster;
	}
	public void setDeviceMaster(String deviceMaster) {
		this.deviceMaster = deviceMaster;
	}
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
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
	public String getFactoryTime() {
		return factoryTime;
	}
	public void setFactoryTime(String factoryTime) {
		this.factoryTime = factoryTime;
	}
	public int getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(int deviceType) {
		this.deviceType = deviceType;
	}
	public String getBuyTime() {
		return buyTime;
	}
	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
	}
	public String getBuyPlace() {
		return buyPlace;
	}
	public void setBuyPlace(String buyPlace) {
		this.buyPlace = buyPlace;
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
	public String getSellerContacts() {
		return sellerContacts;
	}
	public void setSellerContacts(String sellerContacts) {
		this.sellerContacts = sellerContacts;
	}
	public String getSellerMobi() {
		return sellerMobi;
	}
	public void setSellerMobi(String sellerMobi) {
		this.sellerMobi = sellerMobi;
	}
	public String getSellerEmail() {
		return sellerEmail;
	}
	public void setSellerEmail(String sellerEmail) {
		this.sellerEmail = sellerEmail;
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
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getServiceMobi() {
		return serviceMobi;
	}
	public void setServiceMobi(String serviceMobi) {
		this.serviceMobi = serviceMobi;
	}
	public String getServicePlace() {
		return servicePlace;
	}
	public void setServicePlace(String servicePlace) {
		this.servicePlace = servicePlace;
	}
	public Long getOldId() {
		return oldId;
	}
	public void setOldId(Long oldId) {
		this.oldId = oldId;
	}
	public int getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}
}
