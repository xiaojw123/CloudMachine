package com.cloudmachine.bean;

import java.io.Serializable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/2/23 下午2:33
 * 修改人：shixionglu
 * 修改时间：2017/2/23 下午2:33
 * 修改备注：
 */

public class WorkDetailBean implements Serializable {


    public String getCusdemanddesc() {
        return cusdemanddesc;
    }

    public void setCusdemanddesc(String cusdemanddesc) {
        this.cusdemanddesc = cusdemanddesc;
    }

    public String getVworkaddress() {
        return vworkaddress;
    }

    public void setVworkaddress(String vworkaddress) {
        this.vworkaddress = vworkaddress;
    }

    /**
     * vworknum : ~
     * pk_VHCL_MATERIAL : MA000000000000010406
     * station_LON : 120.017468
     * station_LAT : 30.281468
     * vrepair_BO : KBO20170801000010932
     * station_MOBILE : 4008169911
     * station_NAME : 合肥服务站
     * dopportunity_DATETIME : 2017-08-01 14:47:08
     * dr : 0
     * followdate :
     * nstatus : 10220001
     * pk_BRAND : BD000000000000000038
     * pk_CUSTOMER : ~
     * pk_PROD_DEF : PD000000000000000002
     * pk_REPAIR_OPPORTUNITY : 0001AA10000000010933
     * vbrandname : 神钢
     * vmacopname : 测试
     * vmacoptel : 15168412427
     * vdiscription : 客户未填写，请速回电
     * vmachinenum : 00000
     * vmaterialname : SK210LC-6E
     * vprodname : 挖掘机
     * vservicetype : null
     */
    private String vworkaddress;
    private String cusdemanddesc;
    private String service_LAT;
    private String service_LON;
    private String tech_MOBILE;
    private String tech_NAME;
    private String vworknum;
    private String pk_VHCL_MATERIAL;

    public String getService_LAT() {
        return service_LAT;
    }

    public void setService_LAT(String service_LAT) {
        this.service_LAT = service_LAT;
    }

    public String getService_LON() {
        return service_LON;
    }

    public void setService_LON(String service_LON) {
        this.service_LON = service_LON;
    }

    public String getTech_MOBILE() {
        return tech_MOBILE;
    }

    public void setTech_MOBILE(String tech_MOBILE) {
        this.tech_MOBILE = tech_MOBILE;
    }

    public String getTech_NAME() {
        return tech_NAME;
    }

    public void setTech_NAME(String tech_NAME) {
        this.tech_NAME = tech_NAME;
    }

    private String station_LON;
    private String station_LAT;
    private String vrepair_BO;
    private String station_MOBILE;
    private String station_NAME;
    private String dopportunity_DATETIME;
    private String dr;
    private String followdate;
    private String nstatus;
    private String pk_BRAND;
    private String pk_CUSTOMER;
    private String pk_PROD_DEF;
    private String pk_REPAIR_OPPORTUNITY;
    private String vbrandname;
    private String vmacopname;
    private String vmacoptel;
    private String vdiscription;
    private String vmachinenum;
    private String vmaterialname;
    private String vprodname;
    private Object vservicetype;

    public String getVworknum() {
        return vworknum;
    }

    public void setVworknum(String vworknum) {
        this.vworknum = vworknum;
    }

    public String getPk_VHCL_MATERIAL() {
        return pk_VHCL_MATERIAL;
    }

    public void setPk_VHCL_MATERIAL(String pk_VHCL_MATERIAL) {
        this.pk_VHCL_MATERIAL = pk_VHCL_MATERIAL;
    }

    public String getStation_LON() {
        return station_LON;
    }

    public void setStation_LON(String station_LON) {
        this.station_LON = station_LON;
    }

    public String getStation_LAT() {
        return station_LAT;
    }

    public void setStation_LAT(String station_LAT) {
        this.station_LAT = station_LAT;
    }

    public String getVrepair_BO() {
        return vrepair_BO;
    }

    public void setVrepair_BO(String vrepair_BO) {
        this.vrepair_BO = vrepair_BO;
    }

    public String getStation_MOBILE() {
        return station_MOBILE;
    }

    public void setStation_MOBILE(String station_MOBILE) {
        this.station_MOBILE = station_MOBILE;
    }

    public String getStation_NAME() {
        return station_NAME;
    }

    public void setStation_NAME(String station_NAME) {
        this.station_NAME = station_NAME;
    }

    public String getDopportunity_DATETIME() {
        return dopportunity_DATETIME;
    }

    public void setDopportunity_DATETIME(String dopportunity_DATETIME) {
        this.dopportunity_DATETIME = dopportunity_DATETIME;
    }

    public String getDr() {
        return dr;
    }

    public void setDr(String dr) {
        this.dr = dr;
    }

    public String getFollowdate() {
        return followdate;
    }

    public void setFollowdate(String followdate) {
        this.followdate = followdate;
    }

    public String getNstatus() {
        return nstatus;
    }

    public void setNstatus(String nstatus) {
        this.nstatus = nstatus;
    }

    public String getPk_BRAND() {
        return pk_BRAND;
    }

    public void setPk_BRAND(String pk_BRAND) {
        this.pk_BRAND = pk_BRAND;
    }

    public String getPk_CUSTOMER() {
        return pk_CUSTOMER;
    }

    public void setPk_CUSTOMER(String pk_CUSTOMER) {
        this.pk_CUSTOMER = pk_CUSTOMER;
    }

    public String getPk_PROD_DEF() {
        return pk_PROD_DEF;
    }

    public void setPk_PROD_DEF(String pk_PROD_DEF) {
        this.pk_PROD_DEF = pk_PROD_DEF;
    }

    public String getPk_REPAIR_OPPORTUNITY() {
        return pk_REPAIR_OPPORTUNITY;
    }

    public void setPk_REPAIR_OPPORTUNITY(String pk_REPAIR_OPPORTUNITY) {
        this.pk_REPAIR_OPPORTUNITY = pk_REPAIR_OPPORTUNITY;
    }

    public String getVbrandname() {
        return vbrandname;
    }

    public void setVbrandname(String vbrandname) {
        this.vbrandname = vbrandname;
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

    public String getVdiscription() {
        return vdiscription;
    }

    public void setVdiscription(String vdiscription) {
        this.vdiscription = vdiscription;
    }

    public String getVmachinenum() {
        return vmachinenum;
    }

    public void setVmachinenum(String vmachinenum) {
        this.vmachinenum = vmachinenum;
    }

    public String getVmaterialname() {
        return vmaterialname;
    }

    public void setVmaterialname(String vmaterialname) {
        this.vmaterialname = vmaterialname;
    }

    public String getVprodname() {
        return vprodname;
    }

    public void setVprodname(String vprodname) {
        this.vprodname = vprodname;
    }

    public Object getVservicetype() {
        return vservicetype;
    }

    public void setVservicetype(Object vservicetype) {
        this.vservicetype = vservicetype;
    }
}
