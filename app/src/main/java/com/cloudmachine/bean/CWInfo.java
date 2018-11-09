package com.cloudmachine.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by shixionglu on 2016/10/29.
 */

public class CWInfo implements Serializable{

    private WorkSettleBean workSettle;
    private ArrayList<ScheduleBean> schedule;
    private WorkDetailBean workDetail;
    private String flag;
    private ArrayList<String> logoList;


    public ArrayList<String> getLogoList() {
        return logoList;
    }

    public void setLogoList(ArrayList<String> logoList) {
        this.logoList = logoList;
    }


    public WorkSettleBean getWorkSettle() {
        return workSettle;
    }

    public void setWorkSettle(WorkSettleBean workSettle) {
        this.workSettle = workSettle;
    }

    public ArrayList<ScheduleBean> getSchedule() {
        return schedule;
    }

    public void setSchedule(ArrayList<ScheduleBean> schedule) {
        this.schedule = schedule;
    }

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

    @Override
    public String toString() {
        return "CWInfo{" +
                ", workSettle=" + workSettle +
                ", schedule=" + schedule +
                ", workDetail=" + workDetail +
                ", flag='" + flag + '\'' +
                '}';
    }
}
