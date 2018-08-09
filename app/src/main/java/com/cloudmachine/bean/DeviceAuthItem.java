package com.cloudmachine.bean;

/**
 * Created by xiaojw on 2018/8/6.
 */

public class DeviceAuthItem {

    /**
     * uniqueId : MzEyMzQxMTExMTExMTExMTEx
     * deviceId : 680
     * auditStatus : 0
     * auditStatusTxt : 待认证
     * deviceName : 邸辉
     * brand : 斗山
     * model : DH150W-7
     */

    private String uniqueId;
    private int deviceId;
    private int auditStatus;
    private String auditStatusTxt;
    private String deviceName;
    private String brand;
    private String model;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public int getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(int auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditStatusTxt() {
        return auditStatusTxt;
    }

    public void setAuditStatusTxt(String auditStatusTxt) {
        this.auditStatusTxt = auditStatusTxt;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }
}
