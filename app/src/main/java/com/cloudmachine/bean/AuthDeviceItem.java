package com.cloudmachine.bean;

/**
 * Created by xiaojw on 2018/10/23.
 */

public class AuthDeviceItem {

    private int id;
    private int deviceId;
    private String devicePicUrl;
    private int deviceAuditStatus;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getDevicePicUrl() {
        return devicePicUrl;
    }

    public void setDevicePicUrl(String devicePicUrl) {
        this.devicePicUrl = devicePicUrl;
    }

    public int getDeviceAuditStatus() {
        return deviceAuditStatus;
    }

    public void setDeviceAuditStatus(int deviceAuditStatus) {
        this.deviceAuditStatus = deviceAuditStatus;
    }
}
