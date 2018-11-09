package com.cloudmachine.bean;
public class RepairHistoryInfo {
	/**
	 * deviceId : null
	 * deviceName : null
	 * category : 挖掘机
	 * orderNo : CMA_20180412143626691000
	 * flag : null
	 * brandId : null
	 * brandName : 斗山
	 * modelId : null
	 * modelName : DX500LC-G
	 * rackId : 565
	 * serviceDesc : 描述文件
	 * createTime : 2018-04-12 14:36:27
	 * actualPayAmount : null
	 * addressDetail : 浙江省杭州市余杭区五常街道浙江海外高层次人才创新园18幢浙江海外高层次人才创新园
	 * isEvaluate : 0
	 * isEvaluateValue : 未评价
	 * orderStatus : 3
	 * orderStatusValue : 已完工（待付款)
	 */

	private Object deviceId;
	private Object deviceName;
	private String category;
	private String orderNo;
	private String flag;
	private Object brandId;
	private String brandName;
	private Object modelId;
	private String modelName;
	private String rackId;
	private String serviceDesc;
	private String createTime;
	private Object actualPayAmount;
	private String addressDetail;
	private int isEvaluate;
	private String isEvaluateValue;
	private int orderStatus;
	private String orderStatusValue;


	public boolean isEvaluated(){
		return  isEvaluate==1;
	}


	public Object getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Object deviceId) {
		this.deviceId = deviceId;
	}

	public Object getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(Object deviceName) {
		this.deviceName = deviceName;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Object getBrandId() {
		return brandId;
	}

	public void setBrandId(Object brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Object getModelId() {
		return modelId;
	}

	public void setModelId(Object modelId) {
		this.modelId = modelId;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getRackId() {
		return rackId;
	}

	public void setRackId(String rackId) {
		this.rackId = rackId;
	}

	public String getServiceDesc() {
		return serviceDesc;
	}

	public void setServiceDesc(String serviceDesc) {
		this.serviceDesc = serviceDesc;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public Object getActualPayAmount() {
		return actualPayAmount;
	}

	public void setActualPayAmount(Object actualPayAmount) {
		this.actualPayAmount = actualPayAmount;
	}

	public String getAddressDetail() {
		return addressDetail;
	}

	public void setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
	}

	public int getIsEvaluate() {
		return isEvaluate;
	}

	public void setIsEvaluate(int isEvaluate) {
		this.isEvaluate = isEvaluate;
	}

	public String getIsEvaluateValue() {
		return isEvaluateValue;
	}

	public void setIsEvaluateValue(String isEvaluateValue) {
		this.isEvaluateValue = isEvaluateValue;
	}

	public int getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderStatusValue() {
		return orderStatusValue;
	}

	public void setOrderStatusValue(String orderStatusValue) {
		this.orderStatusValue = orderStatusValue;
	}


//	private String     logo_flag;
//	private String     flag;
//    private String     price;
//    private String     dopportunity;
//    private String     vmachinenum;
//    private String     vbrandname;
//    private String     vmaterialname;
//    private String     vmacopname;
//    private String     vdiscription;
//    private String     is_EVALUATE;
//	private boolean isAlliance;
//	private String orderStatusValue;
//
//	public String getOrderStatusValue() {
//		return orderStatusValue;
//	}
//
//	public void setOrderStatusValue(String orderStatusValue) {
//		this.orderStatusValue = orderStatusValue;
//	}
//
//	public boolean isAlliance() {
//		return isAlliance;
//	}
//
//	public void setAlliance(boolean alliance) {
//		isAlliance = alliance;
//	}
//
//	public String getLogo_flag() {
//		return logo_flag;
//	}
//
//	public void setLogo_flag(String logo_flag) {
//		this.logo_flag = logo_flag;
//	}
//
//	private String     vprodname;
//    private String     vmacoptel;
//    private String     nstatus;
//    private String     orderNum;
//	private int        nloanamount_TYPE;//欠款状态
//	private String nloanamount;//欠款金额
//
//	public String getFlag() {
//		return flag;
//	}
//	public void setFlag(String flag) {
//		this.flag = flag;
//	}
//	public String getPrice() {
//		return price;
//	}
//	public void setPrice(String price) {
//		this.price = price;
//	}
//	public String getDopportunity() {
//		return dopportunity;
//	}
//	public void setDopportunity(String dopportunity) {
//		this.dopportunity = dopportunity;
//	}
//	public String getVmachinenum() {
//		return vmachinenum;
//	}
//	public void setVmachinenum(String vmachinenum) {
//		this.vmachinenum = vmachinenum;
//	}
//	public String getVbrandname() {
//		return vbrandname;
//	}
//	public void setVbrandname(String vbrandname) {
//		this.vbrandname = vbrandname;
//	}
//	public String getVmaterialname() {
//		return vmaterialname;
//	}
//	public void setVmaterialname(String vmaterialname) {
//		this.vmaterialname = vmaterialname;
//	}
//	public String getVmacopname() {
//		return vmacopname;
//	}
//	public void setVmacopname(String vmacopname) {
//		this.vmacopname = vmacopname;
//	}
//	public String getVdiscription() {
//		return vdiscription;
//	}
//	public void setVdiscription(String vdiscription) {
//		this.vdiscription = vdiscription;
//	}
//	public String getIs_EVALUATE() {
//		return is_EVALUATE;
//	}
//	public void setIs_EVALUATE(String is_EVALUATE) {
//		this.is_EVALUATE = is_EVALUATE;
//	}
//	public String getVprodname() {
//		return vprodname;
//	}
//	public void setVprodname(String vprodname) {
//		this.vprodname = vprodname;
//	}
//	public String getVmacoptel() {
//		return vmacoptel;
//	}
//	public void setVmacoptel(String vmacoptel) {
//		this.vmacoptel = vmacoptel;
//	}
//	public String getNstatus() {
//		return nstatus;
//	}
//	public void setNstatus(String nstatus) {
//		this.nstatus = nstatus;
//	}
//	public String getOrderNum() {
//		return orderNum;
//	}
//	public void setOrderNum(String orderNum) {
//		this.orderNum = orderNum;
//	}
//
//	public String getNloanamount() {
//		return nloanamount;
//	}
//
//	public void setNloanamount(String nloanamount) {
//		this.nloanamount = nloanamount;
//	}
//
//	public int getNloanamount_TYPE() {
//		return nloanamount_TYPE;
//	}
//
//	public void setNloanamount_TYPE(int nloanamount_TYPE) {
//		this.nloanamount_TYPE = nloanamount_TYPE;
//	}
//
//
//	@Override
//	public String toString() {
//		return "RepairHistoryInfo{" +
//				"flag='" + flag + '\'' +
//				", logo_flag='" + logo_flag + '\'' +
//				", price='" + price + '\'' +
//				", dopportunity='" + dopportunity + '\'' +
//				", vmachinenum='" + vmachinenum + '\'' +
//				", vbrandname='" + vbrandname + '\'' +
//				", vmaterialname='" + vmaterialname + '\'' +
//				", vmacopname='" + vmacopname + '\'' +
//				", vdiscription='" + vdiscription + '\'' +
//				", is_EVALUATE='" + is_EVALUATE + '\'' +
//				", vprodname='" + vprodname + '\'' +
//				", vmacoptel='" + vmacoptel + '\'' +
//				", nstatus='" + nstatus + '\'' +
//				", orderNum='" + orderNum + '\'' +
//				", nloanamount_TYPE=" + nloanamount_TYPE +
//				", nloanamount='" + nloanamount + '\'' +
//				'}';
//	}
}
