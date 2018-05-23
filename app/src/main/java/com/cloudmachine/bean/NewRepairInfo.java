package com.cloudmachine.bean;

import java.io.Serializable;

public class NewRepairInfo implements Serializable {

    /**
     * 新增报修数据类型
     */
    private static final long serialVersionUID = -2794908900406343914L;

    private String vmacopname;
    private String vmacoptel;
    private String vmachinenum;
    private String vdiscription;
    private String vservicetype;
    private String vworkaddress;
    private String typeId;
    private String brandId;

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    private String modelId;
    private double lat;
    private double lng;

    public String getModelname() {
        return modelname;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public void setModelname(String modelname) {
        this.modelname = modelname;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
    }

    private String memberId;
    private String province;
    private String deviceId;
    private String logo_address;
    private String modelname;
    private String brandname;
    private String typeName;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getLogo_address() {
        return logo_address;
    }

    public void setLogo_address(String logo_address) {
        this.logo_address = logo_address;
    }

    public String getVmacopname() {
        return vmacopname;
    }

    public void setVmacopname(String vmacopname) {
        this.vmacopname = vmacopname;
    }

    public String getVmacoptel() {
        return vmacoptel;
    }

    public void setVmacoptel(String vmacoptel) {
        this.vmacoptel = vmacoptel;
    }






    public String getVmachinenum() {
        return vmachinenum;
    }

    public void setVmachinenum(String vmachinenum) {
        this.vmachinenum = vmachinenum;
    }

    public String getVdiscription() {
        return vdiscription;
    }

    public void setVdiscription(String vdiscription) {
        this.vdiscription = vdiscription;
    }

    public String getVservicetype() {
        return vservicetype;
    }

    public void setVservicetype(String vservicetype) {
        this.vservicetype = vservicetype;
    }

    public String getVworkaddress() {
        return vworkaddress;
    }

    public void setVworkaddress(String vworkaddress) {
        this.vworkaddress = vworkaddress;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


}
