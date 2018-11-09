package com.cloudmachine.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojw on 2018/9/26.
 */

public class RepairDetail implements Parcelable {


    /**
     * deviceId : null
     * deviceName : null
     * category : 挖掘机
     * orderNo : CMA_20180522165019712004
     * flag : null
     * brandId : 10
     * brandName : 神钢
     * modelId : 811
     * modelName : SK320-6
     * rackId : 56645
     * serviceDesc : 啦
     * createTime : null
     * actualPayAmount : null
     * addressDetail : 浙江省 杭州市 余杭区 五常街道浙江海外高层次人才创新园16幢浙江海外高层次人才创新园
     * isEvaluate : null
     * isEvaluateValue : null
     * orderStatus : 0
     * orderStatusValue : null
     * allianceStationMobile : 400-008-0581
     * appointTime : null
     * orderLng : 120.016376
     * orderLat : 30.281072
     * artificerLng : null
     * artificerLat : null
     * stationId : 0
     * stationName : 云机械服务站
     * attachmentUrls : []
     * artificerId : null
     * artificerName : null
     * artificerMobile : null
     */

    private Object deviceId;
    private Object deviceName;
    private String category;
    private String orderNo;
    private Object flag;
    private int brandId;
    private String brandName;
    private int modelId;
    private String modelName;
    private String rackId;
    private String serviceDesc;
    private Object createTime;
    private double actualPayAmount;

    private double debtAmount;
    private double totalAmount;

    public double getDebtAmount() {
        return debtAmount;
    }

    public void setDebtAmount(double debtAmount) {
        this.debtAmount = debtAmount;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    private String addressDetail;
    private Object isEvaluate;
    private Object isEvaluateValue;
    private int orderStatus;
    private Object orderStatusValue;
    private String allianceStationMobile;
    private Object appointTime;
    private String orderLng;
    private String orderLat;
    private String artificerLng;
    private String artificerLat;
    private int stationId;
    private String stationName;
    private Object artificerId;
    private String artificerName;
    private String artificerMobile;
    private List<String> attachmentUrls;

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

    public Object getFlag() {
        return flag;
    }

    public void setFlag(Object flag) {
        this.flag = flag;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
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

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }

    public double getActualPayAmount() {
        return actualPayAmount;
    }

    public void setActualPayAmount(double actualPayAmount) {
        this.actualPayAmount = actualPayAmount;
    }

    public String getAddressDetail() {
        return addressDetail;
    }

    public void setAddressDetail(String addressDetail) {
        this.addressDetail = addressDetail;
    }

    public Object getIsEvaluate() {
        return isEvaluate;
    }

    public void setIsEvaluate(Object isEvaluate) {
        this.isEvaluate = isEvaluate;
    }

    public Object getIsEvaluateValue() {
        return isEvaluateValue;
    }

    public void setIsEvaluateValue(Object isEvaluateValue) {
        this.isEvaluateValue = isEvaluateValue;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Object getOrderStatusValue() {
        return orderStatusValue;
    }

    public void setOrderStatusValue(Object orderStatusValue) {
        this.orderStatusValue = orderStatusValue;
    }

    public String getAllianceStationMobile() {
        return allianceStationMobile;
    }

    public void setAllianceStationMobile(String allianceStationMobile) {
        this.allianceStationMobile = allianceStationMobile;
    }

    public Object getAppointTime() {
        return appointTime;
    }

    public void setAppointTime(Object appointTime) {
        this.appointTime = appointTime;
    }

    public String getOrderLng() {
        return orderLng;
    }

    public void setOrderLng(String orderLng) {
        this.orderLng = orderLng;
    }

    public String getOrderLat() {
        return orderLat;
    }

    public void setOrderLat(String orderLat) {
        this.orderLat = orderLat;
    }

    public String getArtificerLng() {
        return artificerLng;
    }

    public void setArtificerLng(String artificerLng) {
        this.artificerLng = artificerLng;
    }

    public String getArtificerLat() {
        return artificerLat;
    }

    public void setArtificerLat(String artificerLat) {
        this.artificerLat = artificerLat;
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

    public Object getArtificerId() {
        return artificerId;
    }

    public void setArtificerId(Object artificerId) {
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

    public List<String> getAttachmentUrls() {
        return attachmentUrls;
    }

    public void setAttachmentUrls(List<String> attachmentUrls) {
        this.attachmentUrls = attachmentUrls;
    }

    public RepairDetail() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.brandId);
        dest.writeString(this.brandName);
        dest.writeInt(this.modelId);
        dest.writeString(this.modelName);
        dest.writeString(this.serviceDesc);
        dest.writeString(this.addressDetail);
        dest.writeStringList(this.attachmentUrls);
    }

    protected RepairDetail(Parcel in) {
        this.brandId = in.readInt();
        this.brandName = in.readString();
        this.modelId = in.readInt();
        this.modelName = in.readString();
        this.serviceDesc = in.readString();
        this.addressDetail = in.readString();
        this.attachmentUrls = in.createStringArrayList();
    }

    public static final Creator<RepairDetail> CREATOR = new Creator<RepairDetail>() {
        @Override
        public RepairDetail createFromParcel(Parcel source) {
            return new RepairDetail(source);
        }

        @Override
        public RepairDetail[] newArray(int size) {
            return new RepairDetail[size];
        }
    };
}
