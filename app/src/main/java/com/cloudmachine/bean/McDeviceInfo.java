package com.cloudmachine.bean;

import java.io.Serializable;

public class McDeviceInfo implements Serializable {


    /**
     * deviceId : 12677
     * deviceName : 小松200-8①
     * typeId : 1
     * typePicUrl : null
     * category : 挖掘机
     * license : WJ07935
     * workTime : null
     * snId : 04201804B0001449
     * brandId : 2
     * brandName : 小松
     * modelId : 14
     * modelName : PC200-8
     * rackId : KMTPC180L62BA7308
     * factoryTime : null
     * ownerId : 880
     * devicePhotoS : null
     * nameplatePhotoS : null
     * enginePhotoS : null
     * address : 山东省济宁市市中区八间房农家院
     */

    private int deviceId;
    private String deviceName;
    private int typeId;
    private String typePicUrl;
    private String category;
    private String license;
    private String workTime;
    private String snId;
    private int brandId;
    private String brandName;
    private int modelId;
    private String modelName;
    private String rackId;
    private String factoryTime;
    private int ownerId;
    private Object devicePhotoS;
    private Object nameplatePhotoS;
    private Object enginePhotoS;
    private String address;

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
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

    public Object getTypePicUrl() {
        return typePicUrl;
    }

    public void setTypePicUrl(String typePicUrl) {
        this.typePicUrl = typePicUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Object getWorkTime() {
        return workTime;
    }

    public void setWorkTime(String workTime) {
        this.workTime = workTime;
    }

    public String getSnId() {
        return snId;
    }

    public void setSnId(String snId) {
        this.snId = snId;
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

    public String getFactoryTime() {
        return factoryTime;
    }

    public void setFactoryTime(String factoryTime) {
        this.factoryTime = factoryTime;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public Object getDevicePhotoS() {
        return devicePhotoS;
    }

    public void setDevicePhotoS(Object devicePhotoS) {
        this.devicePhotoS = devicePhotoS;
    }

    public Object getNameplatePhotoS() {
        return nameplatePhotoS;
    }

    public void setNameplatePhotoS(Object nameplatePhotoS) {
        this.nameplatePhotoS = nameplatePhotoS;
    }

    public Object getEnginePhotoS() {
        return enginePhotoS;
    }

    public void setEnginePhotoS(Object enginePhotoS) {
        this.enginePhotoS = enginePhotoS;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
