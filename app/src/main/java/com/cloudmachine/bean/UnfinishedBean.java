package com.cloudmachine.bean;

public class UnfinishedBean {
    public String getLogo_flag() {
        return logo_flag;
    }

    public void setLogo_flag(String logo_flag) {
        this.logo_flag = logo_flag;
    }

    private String logo_flag;
    private String flag;
    private String price;
    private String dopportunity;
    private String vmachinenum;
    private String vbrandname;
    private String vmaterialname;
    private String vmacopname;
    private String vdiscription;
    private String is_EVALUATE;
    private String vprodname;
    private String vmacoptel;
    private String nstatus;
    private String orderNum;
    private String nloanamount;
    private int nloanamount_TYPE;


    public String getFlag() {
        return flag;
    }

    public boolean isAlliance() {
        return isAlliance;
    }

    public void setAlliance(boolean alliance) {
        isAlliance = alliance;
    }

    private boolean isAlliance;

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDopportunity() {
        return dopportunity;
    }

    public void setDopportunity(String dopportunity) {
        this.dopportunity = dopportunity;
    }

    public String getVmachinenum() {
        return vmachinenum;
    }

    public void setVmachinenum(String vmachinenum) {
        this.vmachinenum = vmachinenum;
    }

    public String getVbrandname() {
        return vbrandname;
    }

    public void setVbrandname(String vbrandname) {
        this.vbrandname = vbrandname;
    }

    public String getVmaterialname() {
        return vmaterialname;
    }

    public void setVmaterialname(String vmaterialname) {
        this.vmaterialname = vmaterialname;
    }

    public String getVmacopname() {
        return vmacopname;
    }

    public void setVmacopname(String vmacopname) {
        this.vmacopname = vmacopname;
    }

    public String getVdiscription() {
        return vdiscription;
    }

    public void setVdiscription(String vdiscription) {
        this.vdiscription = vdiscription;
    }

    public String getIs_EVALUATE() {
        return is_EVALUATE;
    }

    public void setIs_EVALUATE(String is_EVALUATE) {
        this.is_EVALUATE = is_EVALUATE;
    }

    public String getVprodname() {
        return vprodname;
    }

    public void setVprodname(String vprodname) {
        this.vprodname = vprodname;
    }

    public String getVmacoptel() {
        return vmacoptel;
    }

    public void setVmacoptel(String vmacoptel) {
        this.vmacoptel = vmacoptel;
    }

    public String getNstatus() {
        return nstatus;
    }

    public void setNstatus(String nstatus) {
        this.nstatus = nstatus;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getNloanamount() {
        return nloanamount;
    }

    public void setNloanamount(String nloanamount) {
        this.nloanamount = nloanamount;
    }

    public int getNloanamount_TYPE() {
        return nloanamount_TYPE;
    }

    public void setNloanamount_TYPE(int nloanamount_TYPE) {
        this.nloanamount_TYPE = nloanamount_TYPE;
    }

    @Override
    public String toString() {
        return "UnfinishedBean{" +
                "flag='" + flag + '\'' +
                ", logo_flag='" + logo_flag + '\'' +
                ", price='" + price + '\'' +
                ", dopportunity='" + dopportunity + '\'' +
                ", vmachinenum='" + vmachinenum + '\'' +
                ", vbrandname='" + vbrandname + '\'' +
                ", vmaterialname='" + vmaterialname + '\'' +
                ", vmacopname='" + vmacopname + '\'' +
                ", vdiscription='" + vdiscription + '\'' +
                ", is_EVALUATE='" + is_EVALUATE + '\'' +
                ", vprodname='" + vprodname + '\'' +
                ", vmacoptel='" + vmacoptel + '\'' +
                ", nstatus='" + nstatus + '\'' +
                ", orderNum='" + orderNum + '\'' +
                ", nloanamount='" + nloanamount + '\'' +
                ", nloanamount_TYPE=" + nloanamount_TYPE +
                '}';
    }
}