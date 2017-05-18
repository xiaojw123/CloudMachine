package com.cloudmachine.struc;

import java.util.ArrayList;

/**
 * Created by shixionglu on 2016/10/29.
 */

public class BOInfo {


    /**
     * dr : 0
     * vrepair_BO : KBO2016102800010156
     * vmachinenum : 12223
     * vprodname : 挖掘机
     * vbrandname : 卡特
     * vmaterialname : 340D
     * vdiscription : 3455
     * dopportunity_DATETIME : 2016-10-28 17:25:33
     * nstatus : 10220001
     * vworknum : ~
     * followdate :
     * pk_CUSTOMER : ~
     * pk_REPAIR_OPPORTUNITY : 0001AA10000000010155
     * vservicetype : null
     * pk_PROD_DEF : PD000000000000000002
     * pk_BRAND : BD000000000000000019
     * pk_VHCL_MATERIAL : 0001AA1000000000O6KB
     * vmacoptel : 13111111111
     * vmacopname : errr
     */

    private WorkDetailBean workDetail;
    /**
     * schedule : [{"createTime":"2016-10-28 17:25:33","desc":"已报修","key":"0"}]
     * workDetail : {"dr":"0","vrepair_BO":"KBO2016102800010156","vmachinenum":"12223","vprodname":"挖掘机","vbrandname":"卡特","vmaterialname":"340D","vdiscription":"3455","dopportunity_DATETIME":"2016-10-28 17:25:33","nstatus":"10220001","vworknum":"~","followdate":"","pk_CUSTOMER":"~","pk_REPAIR_OPPORTUNITY":"0001AA10000000010155","vservicetype":null,"pk_PROD_DEF":"PD000000000000000002","pk_BRAND":"BD000000000000000019","pk_VHCL_MATERIAL":"0001AA1000000000O6KB","vmacoptel":"13111111111","vmacopname":"errr"}
     * flag : 0
     */

    private String flag;

    public ArrayList<String> getLogoList() {
        return logoList;
    }

    public void setLogoList(ArrayList<String> logoList) {
        this.logoList = logoList;
    }

    /**
     * createTime : 2016-10-28 17:25:33
     * desc : 已报修
     * key : 0
     */

    private ArrayList<ScheduleBean> schedule;
    private ArrayList<String> logoList;

    public WorkDetailBean getWorkDetail() {
        return workDetail;
    }

    public void setWorkDetail(WorkDetailBean workDetail) {
        this.workDetail = workDetail;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public ArrayList<ScheduleBean> getSchedule() {
        return schedule;
    }

    public void setSchedule(ArrayList<ScheduleBean> schedule) {
        this.schedule = schedule;
    }

    public static class WorkDetailBean {
        private String dr;
        private String vrepair_BO;
        private String vmachinenum;
        private String vprodname;
        private String vbrandname;
        private String vmaterialname;
        private String vdiscription;
        private String dopportunity_DATETIME;
        private String nstatus;
        private String vworknum;
        private String followdate;
        private String pk_CUSTOMER;
        private String pk_REPAIR_OPPORTUNITY;
        private String vservicetype;
        private String pk_PROD_DEF;
        private String pk_BRAND;
        private String pk_VHCL_MATERIAL;
        private String vmacoptel;
        private String vmacopname;


        public String getDr() {
            return dr;
        }

        public void setDr(String dr) {
            this.dr = dr;
        }

        public String getVrepair_BO() {
            return vrepair_BO;
        }

        public void setVrepair_BO(String vrepair_BO) {
            this.vrepair_BO = vrepair_BO;
        }

        public String getVmachinenum() {
            return vmachinenum;
        }

        public void setVmachinenum(String vmachinenum) {
            this.vmachinenum = vmachinenum;
        }

        public String getVprodname() {
            return vprodname;
        }

        public void setVprodname(String vprodname) {
            this.vprodname = vprodname;
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

        public String getVdiscription() {
            return vdiscription;
        }

        public void setVdiscription(String vdiscription) {
            this.vdiscription = vdiscription;
        }

        public String getDopportunity_DATETIME() {
            return dopportunity_DATETIME;
        }

        public void setDopportunity_DATETIME(String dopportunity_DATETIME) {
            this.dopportunity_DATETIME = dopportunity_DATETIME;
        }

        public String getNstatus() {
            return nstatus;
        }

        public void setNstatus(String nstatus) {
            this.nstatus = nstatus;
        }

        public String getVworknum() {
            return vworknum;
        }

        public void setVworknum(String vworknum) {
            this.vworknum = vworknum;
        }

        public String getFollowdate() {
            return followdate;
        }

        public void setFollowdate(String followdate) {
            this.followdate = followdate;
        }

        public String getPk_CUSTOMER() {
            return pk_CUSTOMER;
        }

        public void setPk_CUSTOMER(String pk_CUSTOMER) {
            this.pk_CUSTOMER = pk_CUSTOMER;
        }

        public String getPk_REPAIR_OPPORTUNITY() {
            return pk_REPAIR_OPPORTUNITY;
        }

        public void setPk_REPAIR_OPPORTUNITY(String pk_REPAIR_OPPORTUNITY) {
            this.pk_REPAIR_OPPORTUNITY = pk_REPAIR_OPPORTUNITY;
        }

        public String getVservicetype() {
            return vservicetype;
        }

        public void setVservicetype(String vservicetype) {
            this.vservicetype = vservicetype;
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

        public String getVmacoptel() {
            return vmacoptel;
        }

        public void setVmacoptel(String vmacoptel) {
            this.vmacoptel = vmacoptel;
        }

        public String getVmacopname() {
            return vmacopname;
        }

        public void setVmacopname(String vmacopname) {
            this.vmacopname = vmacopname;
        }
    }

   
}
