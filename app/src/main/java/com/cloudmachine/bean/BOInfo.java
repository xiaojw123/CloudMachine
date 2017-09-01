package com.cloudmachine.bean;

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


   
}
