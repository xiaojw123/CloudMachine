package com.cloudmachine.bean;

/**
 * Created by xiaojw on 2018/8/13.
 */

public class PayCodeItem {


    /**
     * id : 3
     * gmtCreate : -2209017600000
     * accountId : 15500
     * accountNo : 13000000000
     * orderId : 3
     * platformId : 1
     * platformIdStr : 云机械
     * buyType : 1
     * buyTypeStr : 云盒体验
     * boxType : 1
     * boxTypeStr : 标配盒子
     * boxNumber : 1
     * tokerName :
     * tokerMobile :
     * boxCodeId : 12
     * boxCode : A8789798897
     * deviceId : null
     * boxStatus : 1
     * boxStatusStr : 未使用
     * usedDate : null
     * invalidDate : null
     */

    private int id;
    private long gmtCreate;
    private int accountId;
    private String accountNo;
    private int orderId;
    private int platformId;
    private String platformIdStr;
    private int buyType;
    private String buyTypeStr;
    private int boxType;
    private String boxTypeStr;
    private int boxNumber;
    private String tokerName;
    private String tokerMobile;
    private int boxCodeId;
    private String boxCode;
    private String deviceId;
    private int boxStatus;//1:未使用，2:已使用，3:失效
    private String boxStatusStr;
    private String usedDate;
    private String invalidDate;

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

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getPlatformId() {
        return platformId;
    }

    public void setPlatformId(int platformId) {
        this.platformId = platformId;
    }

    public String getPlatformIdStr() {
        return platformIdStr;
    }

    public void setPlatformIdStr(String platformIdStr) {
        this.platformIdStr = platformIdStr;
    }

    public int getBuyType() {
        return buyType;
    }

    public void setBuyType(int buyType) {
        this.buyType = buyType;
    }

    public String getBuyTypeStr() {
        return buyTypeStr;
    }

    public void setBuyTypeStr(String buyTypeStr) {
        this.buyTypeStr = buyTypeStr;
    }

    public int getBoxType() {
        return boxType;
    }

    public void setBoxType(int boxType) {
        this.boxType = boxType;
    }

    public String getBoxTypeStr() {
        return boxTypeStr;
    }

    public void setBoxTypeStr(String boxTypeStr) {
        this.boxTypeStr = boxTypeStr;
    }

    public int getBoxNumber() {
        return boxNumber;
    }

    public void setBoxNumber(int boxNumber) {
        this.boxNumber = boxNumber;
    }

    public String getTokerName() {
        return tokerName;
    }

    public void setTokerName(String tokerName) {
        this.tokerName = tokerName;
    }

    public String getTokerMobile() {
        return tokerMobile;
    }

    public void setTokerMobile(String tokerMobile) {
        this.tokerMobile = tokerMobile;
    }

    public int getBoxCodeId() {
        return boxCodeId;
    }

    public void setBoxCodeId(int boxCodeId) {
        this.boxCodeId = boxCodeId;
    }

    public String getBoxCode() {
        return boxCode;
    }

    public void setBoxCode(String boxCode) {
        this.boxCode = boxCode;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getBoxStatus() {
        return boxStatus;
    }

    public void setBoxStatus(int boxStatus) {
        this.boxStatus = boxStatus;
    }

    public String getBoxStatusStr() {
        return boxStatusStr;
    }

    public void setBoxStatusStr(String boxStatusStr) {
        this.boxStatusStr = boxStatusStr;
    }

    public String getUsedDate() {
        return usedDate;
    }

    public void setUsedDate(String usedDate) {
        this.usedDate = usedDate;
    }

    public String getInvalidDate() {
        return invalidDate;
    }

    public void setInvalidDate(String invalidDate) {
        this.invalidDate = invalidDate;
    }
}
