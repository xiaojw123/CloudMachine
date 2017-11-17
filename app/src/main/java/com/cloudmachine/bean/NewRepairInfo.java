package com.cloudmachine.bean;

import java.io.Serializable;

public class NewRepairInfo implements Serializable {

    /**
     * 新增报修数据类型
     */
    private static final long serialVersionUID = -2794908900406343914L;

    private String vmacopname;
    private String vmacoptel;
    private String pk_prod_def;
    private String pk_brand;
    private String pk_vhcl_material;
    private String vmachinenum;
    private String vdiscription;
    private String vservicetype;
    private String vworkaddress;
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

    public String getPk_prod_def() {
        return pk_prod_def;
    }

    public void setPk_prod_def(String pk_prod_def) {
        this.pk_prod_def = pk_prod_def;
    }

    public String getPk_brand() {
        return pk_brand;
    }

    public void setPk_brand(String pk_brand) {
        this.pk_brand = pk_brand;
    }

    public String getPk_vhcl_material() {
        return pk_vhcl_material;
    }

    public void setPk_vhcl_material(String pk_vhcl_material) {
        this.pk_vhcl_material = pk_vhcl_material;
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
