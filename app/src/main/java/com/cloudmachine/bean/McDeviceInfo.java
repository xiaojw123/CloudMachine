package com.cloudmachine.bean;

import android.renderscript.ScriptIntrinsicYuvToRGB;

import java.io.Serializable;

public class McDeviceInfo implements Serializable {

    public String getSnId() {
        return snId;
    }

    public void setSnId(String snId) {
        this.snId = snId;
    }

    /**
     *
     */

    private int oilLave;
    private float workTime;
    private String snId;

    public int getOilLave() {
        return oilLave;
    }

    public void setOilLave(int oilLave) {
        this.oilLave = oilLave;
    }

    public float getWorkTime() {
        return workTime;
    }

    public void setWorkTime(float workTime) {
        this.workTime = workTime;
    }

    private long id=-1;
    private long deviceId;

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    private String name;
    private String deviceName;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    private String category;
    private String model;
    private String brand;
    private String image;
    private String macAddress;
    private int checkStatus;
    private int workStatus;// 0表示不工作  1表示工作  2表示在线
    private int type; //1 2 4
    private String pk_PROD_DEF;
    private String pk_BRAND;
    private String pk_VHCL_MATERIAL;
    private String rackId;
    private String typePicUrl;
    private int typeId;
    private int brandId;
    private int modelId;

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public String getTypePicUrl() {
        return typePicUrl;
    }

    public void setTypePicUrl(String typePicUrl) {
        this.typePicUrl = typePicUrl;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getPk_PROD_DEF() {
        return pk_PROD_DEF;
    }

    public void setPk_PROD_DEF(String pk_PROD_DEF) {
        this.pk_PROD_DEF = pk_PROD_DEF;
    }

    public String getPk_BRAND() {
        return pk_BRAND;
    }

    public void setPk_BRAND(String pk_BRAND) {
        this.pk_BRAND = pk_BRAND;
    }

    public String getPk_VHCL_MATERIAL() {
        return pk_VHCL_MATERIAL;
    }

    public void setPk_VHCL_MATERIAL(String pk_VHCL_MATERIAL) {
        this.pk_VHCL_MATERIAL = pk_VHCL_MATERIAL;
    }

    public String getRackId() {
        return rackId;
    }

    public void setRackId(String rackId) {
        this.rackId = rackId;
    }

    private McDeviceLocation location;

    public long getId() {
        return id==-1?deviceId:id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name==null?deviceName:name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public int getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(int checkStatus) {
        this.checkStatus = checkStatus;
    }

    public int getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(int workStatus) {
        this.workStatus = workStatus;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public McDeviceLocation getLocation() {
        return location;
    }

    public void setLocation(McDeviceLocation location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "McDeviceInfo [id=" + id + ", name=" + name + ", model=" + model
                + ", brand=" + brand + ", image=" + image + ", macAddress="
                + macAddress + ", checkStatus=" + checkStatus + ", workStatus="
                + workStatus + ", type=" + type + ", location=" + location + ", workTime=" + workTime
                + "]";
    }

    public void setSelected(boolean sel) {
        this.sel = sel;
    }

    public boolean isSelected() {
        return sel;
    }

    private boolean sel;


}
