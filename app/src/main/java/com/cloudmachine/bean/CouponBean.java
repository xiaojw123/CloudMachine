package com.cloudmachine.bean;

import java.util.List;

/**
 * Created by xiaojw on 2017/10/11.
 */

public class CouponBean {


    /**
     * userCouponBOList : [{"id":2,"couponBaseId":null,"couponName":null,"remark":null,"packName":null,"userId":142,"limitInfo":null,"amount":null,"couponNum":null,"totalAmount":null,"useNum":0,"cStatus":2,"useTime":null,"startTime":"2017-02-28 ","endTime":"2017-02-28 "},{"id":3,"couponBaseId":null,"couponName":null,"remark":null,"packName":null,"userId":142,"limitInfo":null,"amount":null,"couponNum":null,"totalAmount":null,"useNum":0,"cStatus":2,"useTime":null,"startTime":"2017-02-28 ","endTime":"2017-02-28 "},{"id":4,"couponBaseId":null,"couponName":null,"remark":null,"packName":null,"userId":142,"limitInfo":null,"amount":null,"couponNum":null,"totalAmount":null,"useNum":0,"cStatus":2,"useTime":null,"startTime":"2017-02-28 ","endTime":"2017-02-28 "},{"id":5,"couponBaseId":null,"couponName":null,"remark":null,"packName":null,"userId":142,"limitInfo":null,"amount":null,"couponNum":null,"totalAmount":null,"useNum":0,"cStatus":2,"useTime":null,"startTime":"2017-02-28 ","endTime":"2017-02-28 "},{"id":10,"couponBaseId":null,"couponName":null,"remark":null,"packName":null,"userId":142,"limitInfo":null,"amount":null,"couponNum":null,"totalAmount":null,"useNum":0,"cStatus":2,"useTime":null,"startTime":"2017-02-28 ","endTime":"2017-02-28 "},{"id":11,"couponBaseId":null,"couponName":null,"remark":null,"packName":null,"userId":142,"limitInfo":null,"amount":null,"couponNum":null,"totalAmount":null,"useNum":0,"cStatus":2,"useTime":null,"startTime":"2017-02-28 ","endTime":"2017-02-28 "},{"id":12,"couponBaseId":null,"couponName":null,"remark":null,"packName":null,"userId":142,"limitInfo":null,"amount":null,"couponNum":null,"totalAmount":null,"useNum":0,"cStatus":2,"useTime":null,"startTime":"2017-02-28 ","endTime":"2017-02-28 "},{"id":13,"couponBaseId":null,"couponName":null,"remark":null,"packName":null,"userId":142,"limitInfo":null,"amount":null,"couponNum":null,"totalAmount":null,"useNum":0,"cStatus":2,"useTime":null,"startTime":"2017-02-28 ","endTime":"2017-02-28 "},{"id":14,"couponBaseId":null,"couponName":null,"remark":null,"packName":null,"userId":142,"limitInfo":null,"amount":null,"couponNum":null,"totalAmount":null,"useNum":0,"cStatus":2,"useTime":null,"startTime":"2017-02-28 ","endTime":"2017-02-28 "},{"id":15,"couponBaseId":null,"couponName":null,"remark":null,"packName":null,"userId":142,"limitInfo":null,"amount":null,"couponNum":null,"totalAmount":null,"useNum":0,"cStatus":2,"useTime":null,"startTime":"2017-02-28 ","endTime":"2017-02-28 "},{"id":16,"couponBaseId":null,"couponName":null,"remark":null,"packName":null,"userId":142,"limitInfo":null,"amount":null,"couponNum":null,"totalAmount":null,"useNum":0,"cStatus":2,"useTime":null,"startTime":"2017-03-01 ","endTime":"2017-06-09 "},{"id":17,"couponBaseId":null,"couponName":null,"remark":null,"packName":null,"userId":142,"limitInfo":null,"amount":null,"couponNum":null,"totalAmount":null,"useNum":0,"cStatus":2,"useTime":null,"startTime":"2017-03-01 ","endTime":"2017-06-09 "},{"id":18,"couponBaseId":null,"couponName":null,"remark":null,"packName":null,"userId":142,"limitInfo":null,"amount":null,"couponNum":null,"totalAmount":null,"useNum":0,"cStatus":2,"useTime":null,"startTime":"2017-03-01 ","endTime":"2017-06-09 "},{"id":19,"couponBaseId":null,"couponName":null,"remark":null,"packName":null,"userId":142,"limitInfo":null,"amount":null,"couponNum":null,"totalAmount":null,"useNum":0,"cStatus":2,"useTime":null,"startTime":"2017-03-01 ","endTime":"2017-06-09 "},{"id":55,"couponBaseId":null,"couponName":null,"remark":null,"packName":null,"userId":142,"limitInfo":null,"amount":null,"couponNum":null,"totalAmount":null,"useNum":0,"cStatus":1,"useTime":null,"startTime":"2017-07-05 ","endTime":"2018-07-05 "}]
     * sumNum : 15
     * sumAmount : null
     */

    private int sumNum;
    private Integer sumAmount;
    private List<CouponItem> userCouponBOList;

    public int getSumNum() {
        return sumNum;
    }

    public void setSumNum(int sumNum) {
        this.sumNum = sumNum;
    }

    public Integer getSumAmount() {
        return sumAmount;
    }

    public void setSumAmount(Integer sumAmount) {
        this.sumAmount = sumAmount;
    }

    public List<CouponItem> getUserCouponBOList() {
        return userCouponBOList;
    }

    public void setUserCouponBOList(List<CouponItem> userCouponBOList) {
        this.userCouponBOList = userCouponBOList;
    }


}
