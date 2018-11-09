package com.cloudmachine.bean;

import com.cloudmachine.utils.CommonUtils;

/**
 * Created by xiaojw on 2018/6/14.
 */

public class SalaryHistoryItem {


    /**
     * payMemberId : null
     * payMemberName : null
     * payoffTime : 1528966237000
     * recordDesc :
     * receiveMemberId : 200
     * receiveMemberName : abc
     * salaryAmount : -0.01
     * totalAmountOfInterval : 0.01
     */

    private String payMemberId;
    private String payMemberName;
    private String payoffTime;
    private String recordDesc;
    private String receiveMemberId;
    private String receiveMemberName;
    private double salaryAmount;
    private double totalAmountOfInterval;



    public void setFoarmtDate(String formatDate) {
        this.formatDate = formatDate;
    }

    private String formatDate;

    public String getPayMemberId() {
        return payMemberId;
    }

    public void setPayMemberId(String payMemberId) {
        this.payMemberId = payMemberId;
    }

    public String getPayMemberName() {
        return payMemberName;
    }

    public void setPayMemberName(String payMemberName) {
        this.payMemberName = payMemberName;
    }

    public String getPayoffTime() {
        return payoffTime;
    }

    public void setPayoffTime(String payoffTime) {
        this.payoffTime = payoffTime;
    }

    public String getRecordDesc() {
        return recordDesc;
    }

    public void setRecordDesc(String recordDesc) {
        this.recordDesc = recordDesc;
    }

    public String getReceiveMemberId() {
        return receiveMemberId;
    }

    public void setReceiveMemberId(String receiveMemberId) {
        this.receiveMemberId = receiveMemberId;
    }

    public String getReceiveMemberName() {
        return receiveMemberName;
    }

    public void setReceiveMemberName(String receiveMemberName) {
        this.receiveMemberName = receiveMemberName;
    }

    public double getSalaryAmount() {
        return salaryAmount;
    }

    public void setSalaryAmount(double salaryAmount) {
        this.salaryAmount = salaryAmount;
    }

    public double getTotalAmountOfInterval() {
        return totalAmountOfInterval;
    }

    public void setTotalAmountOfInterval(double totalAmountOfInterval) {
        this.totalAmountOfInterval = totalAmountOfInterval;
    }
}
