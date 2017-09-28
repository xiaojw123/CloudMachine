package com.cloudmachine.bean;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/2/15 下午3:56
 * 修改人：shixionglu
 * 修改时间：2017/2/15 下午3:56
 * 修改备注：
 */

public class CouponInfo implements Serializable {


    /**
     * createTime : 2017-02-13
     * creator : 714
     * limitInfo : 满1000使用
     * cStatus : 0
     * amount : 1000
     * userId : 714
     * couponId : 1
     * couponInfoId : 4
     * userType : 维修保养抵用券
     * worknum : null
     * endTime : 2017-05-28
     * startTime : 2017-02-17
     * id : 124
     */

    private String     createTime;
    private String     creator;
    private String     limitInfo;
    private int        cStatus;//0,可用  1，已使用  2，已过期
    private BigDecimal amount;
    private long       userId;
    private long       couponId;
    private long       couponInfoId;
    private String     userType;
    private String        worknum;
    private String     endTime;
    private String     startTime;
    private long       id;


    private String remark;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private String title;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getLimitInfo() {
        return limitInfo;
    }

    public void setLimitInfo(String limitInfo) {
        this.limitInfo = limitInfo;
    }

    public int getcStatus() {
        return cStatus;
    }

    public void setcStatus(int cStatus) {
        this.cStatus = cStatus;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCouponId() {
        return couponId;
    }

    public void setCouponId(long couponId) {
        this.couponId = couponId;
    }

    public long getCouponInfoId() {
        return couponInfoId;
    }

    public void setCouponInfoId(long couponInfoId) {
        this.couponInfoId = couponInfoId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWorknum() {
        return worknum;
    }

    public void setWorknum(String worknum) {
        this.worknum = worknum;
    }

    @Override
    public String toString() {
        return "CouponInfo{" +
                "createTime='" + createTime + '\'' +
                ", creator='" + creator + '\'' +
                ", limitInfo='" + limitInfo + '\'' +
                ", cStatus=" + cStatus +
                ", amount=" + amount +
                ", userId=" + userId +
                ", couponId=" + couponId +
                ", couponInfoId=" + couponInfoId +
                ", userType='" + userType + '\'' +
                ", worknum='" + worknum + '\'' +
                ", endTime='" + endTime + '\'' +
                ", startTime='" + startTime + '\'' +
                ", id=" + id +
                '}';
    }
}
