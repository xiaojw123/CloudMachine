package com.cloudmachine.bean;

/**
 * Created by xiaojw on 2018/9/17.
 */

public class LarkDeviceInfo {


    /**
     * deviceId : 12677
     * deviceName : 小松200-8①
     * typeId : 1
     * typePicUrl : http://medias.cloudm.com/static/app/map/wa_jue_ji
     * category : null
     * roleType : 1
     * lng : null
     * lat : null
     * workStatus : 0
     * snId : 04201804B0001449
     */

    private long deviceId;
    private String deviceName;
    private int typeId;
    private String typePicUrl;
    private String category;
    private int roleType;
    private double lng;
    private double lat;
    private int workStatus;
    private String snId;

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypePicUrl() {
        return typePicUrl;
    }

    public void setTypePicUrl(String typePicUrl) {
        this.typePicUrl = typePicUrl;
    }

    public Object getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(int workStatus) {
        this.workStatus = workStatus;
    }

    public String getSnId() {
        return snId;
    }

    public void setSnId(String snId) {
        this.snId = snId;
    }
}
