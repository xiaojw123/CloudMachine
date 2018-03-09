package com.cloudmachine.bean;

import java.util.List;

/**
 * Created by xiaojw on 2018/2/28.
 * 加盟站列表item
 */

public class AllianceItem {


    /**
     * id : 315
     * gmtCreate : 1519811195000
     * orderNo : CMA_20180228174634733000
     * serviceType : 0
     * serviceTypeAlis : 故障报修
     * machineNum : 82726
     * orderStatus : 3
     * brandName : 小松
     * modelName : PC240LC-8M0
     * reporterName : 测试
     * reporterMobile : 15168412427
     * reporterRole : null
     * demandDescription : 需求
     * addressDetail : 北京北京东城区U
     * assignTime : null
     * finishTime : null
     * totalAmount : 0.1
     * couponAmount : null
     * actualAmount : null
     * receivableAmount : null
     * debtAmount : null
     * gmtCreateStr : 2018-02-28 17:46:35
     * stationId : 39
     * stationName : 小仙女
     * orderStatusValue : 已完工（待付款)
     * maintainStartTime : null
     * payTime : null
     * machineWorkTime : null
     * areaId : null
     * areaAlias : null
     * artificerId : 20
     * artificerName : 测试02
     * artificerMobile : null
     * maintainMeasure : 措施
     * boxSn : null
     * areaFullName : 北京北京东城区
     * orderSource : 1
     * orderSourceValue : 自有工单
     * allianceType : 1
     * allianceTypeValue : 加盟
     * plantFormSettlementId : null
     * settlementAmount : null
     * settlementStatus : null
     * appealReason : null
     * isEvaluate : 0
     * gmtModifiedStr : 2018-02-28 17:44:33
     * attachmentUrls : ["http://medias.test.cloudm.com/img0/20180228/1519811219982.jpg","http://medias.test.cloudm.com/img0/20180228/1519811224922.jpg","http://medias.test.cloudm.com/img0/20180228/1519811230635.jpg"]
     */

    private int id;
    private long gmtCreate;
    private String orderNo;
    private int serviceType;
    private String serviceTypeAlis;
    private String machineNum;
    private int orderStatus;
    private String brandName;
    private String modelName;
    private String reporterName;
    private String reporterMobile;
    private String reporterRole;
    private String demandDescription;
    private String addressDetail;
    private String assignTime;
    private String finishTime;
    private double totalAmount;
    private String couponAmount;
    private String actualAmount;
    private String receivableAmount;
    private String debtAmount;
    private String gmtCreateStr;
    private int stationId;
    private String stationName;
    private String orderStatusValue;
    private String maintainStartTime;
    private String payTime;
    private String machineWorkTime;
    private String areaId;
    private String areaAlias;
    private int artificerId;
    private String artificerName;
    private String artificerMobile;
    private String maintainMeasure;
    private String boxSn;
    private String areaFullName;
    private int orderSource;
    private String orderSourceValue;
    private int allianceType;
    private String allianceTypeValue;
    private String plantFormSettlementId;
    private String settlementAmount;
    private String settlementStatus;
    private String appealReason;
    private int isEvaluate;
    private String gmtModifiedStr;
    private List<String> attachmentUrls;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(long gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getServiceType() {
        return serviceType;
    }

    public void setServiceType(int serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceTypeAlis() {
        return serviceTypeAlis;
    }

    public void setServiceTypeAlis(String serviceTypeAlis) {
        this.serviceTypeAlis = serviceTypeAlis;
    }

    public String getMachineNum() {
        return machineNum;
    }

    public void setMachineNum(String machineNum) {
        this.machineNum = machineNum;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getReporterMobile() {
        return reporterMobile;
    }

    public void setReporterMobile(String reporterMobile) {
        this.reporterMobile = reporterMobile;
    }

    public Object getReporterRole() {
        return reporterRole;
    }

    public void setReporterRole(String reporterRole) {
        this.reporterRole = reporterRole;
    }

    public String getDemandDescription() {
        return demandDescription;
    }

    public void setDemandDescription(String demandDescription) {
        this.demandDescription = demandDescription;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public Object getAssignTime() {
        return assignTime;
    }

    public void setAssignTime(String assignTime) {
        this.assignTime = assignTime;
    }

    public Object getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Object getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(String couponAmount) {
        this.couponAmount = couponAmount;
    }

    public Object getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(String actualAmount) {
        this.actualAmount = actualAmount;
    }

    public Object getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(String receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

    public Object getDebtAmount() {
        return debtAmount;
    }

    public void setDebtAmount(String debtAmount) {
        this.debtAmount = debtAmount;
    }

    public String getGmtCreateStr() {
        return gmtCreateStr;
    }

    public void setGmtCreateStr(String gmtCreateStr) {
        this.gmtCreateStr = gmtCreateStr;
    }

    public int getStationId() {
        return stationId;
    }

    public void setStationId(int stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getOrderStatusValue() {
        return orderStatusValue;
    }

    public void setOrderStatusValue(String orderStatusValue) {
        this.orderStatusValue = orderStatusValue;
    }

    public Object getMaintainStartTime() {
        return maintainStartTime;
    }

    public void setMaintainStartTime(String maintainStartTime) {
        this.maintainStartTime = maintainStartTime;
    }

    public Object getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public Object getMachineWorkTime() {
        return machineWorkTime;
    }

    public void setMachineWorkTime(String machineWorkTime) {
        this.machineWorkTime = machineWorkTime;
    }

    public Object getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public Object getAreaAlias() {
        return areaAlias;
    }

    public void setAreaAlias(String areaAlias) {
        this.areaAlias = areaAlias;
    }

    public int getArtificerId() {
        return artificerId;
    }

    public void setArtificerId(int artificerId) {
        this.artificerId = artificerId;
    }

    public String getArtificerName() {
        return artificerName;
    }

    public void setArtificerName(String artificerName) {
        this.artificerName = artificerName;
    }

    public String getArtificerMobile() {
        return artificerMobile;
    }

    public void setArtificerMobile(String artificerMobile) {
        this.artificerMobile = artificerMobile;
    }

    public String getMaintainMeasure() {
        return maintainMeasure;
    }

    public void setMaintainMeasure(String maintainMeasure) {
        this.maintainMeasure = maintainMeasure;
    }

    public Object getBoxSn() {
        return boxSn;
    }

    public void setBoxSn(String boxSn) {
        this.boxSn = boxSn;
    }

    public String getAreaFullName() {
        return areaFullName;
    }

    public void setAreaFullName(String areaFullName) {
        this.areaFullName = areaFullName;
    }

    public int getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(int orderSource) {
        this.orderSource = orderSource;
    }

    public String getOrderSourceValue() {
        return orderSourceValue;
    }

    public void setOrderSourceValue(String orderSourceValue) {
        this.orderSourceValue = orderSourceValue;
    }

    public int getAllianceType() {
        return allianceType;
    }

    public void setAllianceType(int allianceType) {
        this.allianceType = allianceType;
    }

    public String getAllianceTypeValue() {
        return allianceTypeValue;
    }

    public void setAllianceTypeValue(String allianceTypeValue) {
        this.allianceTypeValue = allianceTypeValue;
    }

    public Object getPlantFormSettlementId() {
        return plantFormSettlementId;
    }

    public void setPlantFormSettlementId(String plantFormSettlementId) {
        this.plantFormSettlementId = plantFormSettlementId;
    }

    public Object getSettlementAmount() {
        return settlementAmount;
    }

    public void setSettlementAmount(String settlementAmount) {
        this.settlementAmount = settlementAmount;
    }

    public Object getSettlementStatus() {
        return settlementStatus;
    }

    public void setSettlementStatus(String settlementStatus) {
        this.settlementStatus = settlementStatus;
    }

    public Object getAppealReason() {
        return appealReason;
    }

    public void setAppealReason(String appealReason) {
        this.appealReason = appealReason;
    }

    public int getIsEvaluate() {
        return isEvaluate;
    }

    public void setIsEvaluate(int isEvaluate) {
        this.isEvaluate = isEvaluate;
    }

    public String getGmtModifiedStr() {
        return gmtModifiedStr;
    }

    public void setGmtModifiedStr(String gmtModifiedStr) {
        this.gmtModifiedStr = gmtModifiedStr;
    }

    public List<String> getAttachmentUrls() {
        return attachmentUrls;
    }

    public void setAttachmentUrls(List<String> attachmentUrls) {
        this.attachmentUrls = attachmentUrls;
    }
}
