package com.cloudmachine.ui.home.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CouponItem implements Parcelable {
    /**
     * id : 2
     * couponBaseId : null
     * couponName : null
     * remark : null
     * packName : null
     * userId : 142
     * limitInfo : null
     * amount : null
     * couponNum : null
     * totalAmount : null
     * useNum : 0
     * cStatus : 2
     * useTime : null
     * startTime : 2017-02-28
     * endTime : 2017-02-28
     */

    private int id;
    private int couponBaseId;
    private String couponName;
    private String remark;
    private String packName;
    private int userId;
    private String limitInfo;
    private int amount;
    private int couponNum;
    private String totalAmount;
    private int useNum;
    private int cStatus;
    private String useTime;
    private String startTime;
    private String endTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCouponBaseId() {
        return couponBaseId;
    }

    public void setCouponBaseId(int couponBaseId) {
        this.couponBaseId = couponBaseId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLimitInfo() {
        return limitInfo;
    }

    public void setLimitInfo(String limitInfo) {
        this.limitInfo = limitInfo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCouponNum() {
        return couponNum;
    }

    public void setCouponNum(int couponNum) {
        this.couponNum = couponNum;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getUseNum() {
        return useNum;
    }

    public void setUseNum(int useNum) {
        this.useNum = useNum;
    }

    public int getCStatus() {
        return cStatus;
    }

    public void setCStatus(int cStatus) {
        this.cStatus = cStatus;
    }

    public String getUseTime() {
        return useTime;
    }

    public void setUseTime(String useTime) {
        this.useTime = useTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.couponBaseId);
        dest.writeString(this.couponName);
        dest.writeString(this.remark);
        dest.writeString(this.packName);
        dest.writeInt(this.userId);
        dest.writeString(this.limitInfo);
        dest.writeInt(this.amount);
        dest.writeInt(this.couponNum);
        dest.writeString(this.totalAmount);
        dest.writeInt(this.useNum);
        dest.writeInt(this.cStatus);
        dest.writeString(this.useTime);
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
    }

    public CouponItem() {
    }

    protected CouponItem(Parcel in) {
        this.id = in.readInt();
        this.couponBaseId = in.readInt();
        this.couponName = in.readString();
        this.remark = in.readString();
        this.packName = in.readString();
        this.userId = in.readInt();
        this.limitInfo = in.readString();
        this.amount = in.readInt();
        this.couponNum = in.readInt();
        this.totalAmount = in.readString();
        this.useNum = in.readInt();
        this.cStatus = in.readInt();
        this.useTime = in.readString();
        this.startTime = in.readString();
        this.endTime = in.readString();
    }

    public static final Parcelable.Creator<CouponItem> CREATOR = new Parcelable.Creator<CouponItem>() {
        @Override
        public CouponItem createFromParcel(Parcel source) {
            return new CouponItem(source);
        }

        @Override
        public CouponItem[] newArray(int size) {
            return new CouponItem[size];
        }
    };
}