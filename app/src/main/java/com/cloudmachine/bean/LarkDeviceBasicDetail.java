package com.cloudmachine.bean;

import java.util.List;

/**
 * Created by xiaojw on 2018/9/18.
 */

public class LarkDeviceBasicDetail {


    /**
     * deviceId : 0
     * deviceName : 体验机
     * typeId : 1
     * typePicUrl : null
     * category : 挖掘机
     * license : TF00009
     * workTime : 3609.85
     * snId : 156018434650373831FFDB05
     * brandId : 9
     * brandName : 斗山
     * modelId : 181
     * modelName : DH220LC-7
     * rackId : 2563
     * factoryTime : 2013-11-05
     * ownerId : 862
     * devicePhotoS : ["http://image.cloudm.com/2016/11/601d934d-4bde-48ac-b134-8f1ae75477b5.jpg"]
     * nameplatePhotoS : ["http://image.cloudm.com/2016/11/d13cd49b-059e-4f01-afc8-5cdc3803e153.jpg"]
     * enginePhotoS : ["http://image.cloudm.com/2016/11/c47eb125-b5e5-40f0-934f-8cdfb6fce08f.jpg"]
     */

    private int deviceId;
    private String deviceName;
    private int typeId;
    private Object typePicUrl;
    private String category;
    private String license;
    private double workTime;
    private String snId;
    private int brandId;
    private String brandName;
    private int modelId;
    private String modelName;
    private String rackId;
    private String factoryTime;
    private int ownerId;
    private List<String> devicePhotoS;
    private List<String> nameplatePhotoS;
    private List<String> enginePhotoS;

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

    public void setTypePicUrl(Object typePicUrl) {
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

    public double getWorkTime() {
        return workTime;
    }

    public void setWorkTime(double workTime) {
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

    public List<String> getDevicePhotoS() {
        return devicePhotoS;
    }

    public void setDevicePhotoS(List<String> devicePhotoS) {
        this.devicePhotoS = devicePhotoS;
    }

    public List<String> getNameplatePhotoS() {
        return nameplatePhotoS;
    }

    public void setNameplatePhotoS(List<String> nameplatePhotoS) {
        this.nameplatePhotoS = nameplatePhotoS;
    }

    public List<String> getEnginePhotoS() {
        return enginePhotoS;
    }

    public void setEnginePhotoS(List<String> enginePhotoS) {
        this.enginePhotoS = enginePhotoS;
    }
}
