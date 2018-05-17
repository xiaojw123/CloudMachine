package com.cloudmachine.bean;

/**
 * Created by xiaojw on 2018/5/9.
 */

public class OilSynBean {

//    {
//        deviceId: 666,
//                oilPosition: 0,
//            oilPositionName: "油箱为空",
//            oilPositionValue: 0,
//            synTime: null,
//            synTimeStr: null,
//            synStatus: null,
//            synStatusValue: null
//    }

    private long deviceId;
    private  int oilPosition;
    private  String oilPositionName;
    private  int oilPositionValue;

    private String synTime;
    private String synTimeStr;

    private String synStatus;

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public int getOilPosition() {
        return oilPosition;
    }

    public void setOilPosition(int oilPosition) {
        this.oilPosition = oilPosition;
    }

    public String getOilPositionName() {
        return oilPositionName;
    }

    public void setOilPositionName(String oilPositionName) {
        this.oilPositionName = oilPositionName;
    }

    public int getOilPositionValue() {
        return oilPositionValue;
    }

    public void setOilPositionValue(int oilPositionValue) {
        this.oilPositionValue = oilPositionValue;
    }

    public String getSynTime() {
        return synTime;
    }

    public void setSynTime(String synTime) {
        this.synTime = synTime;
    }

    public String getSynTimeStr() {
        return synTimeStr;
    }

    public void setSynTimeStr(String synTimeStr) {
        this.synTimeStr = synTimeStr;
    }

    public String getSynStatus() {
        return synStatus;
    }

    public void setSynStatus(String synStatus) {
        this.synStatus = synStatus;
    }

    public String getSynStatusValue() {
        return synStatusValue;
    }

    public void setSynStatusValue(String synStatusValue) {
        this.synStatusValue = synStatusValue;
    }

    private String synStatusValue;









}
