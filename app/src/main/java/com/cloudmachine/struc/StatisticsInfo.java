package com.cloudmachine.struc;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2016/12/6 下午4:36
 * 修改人：shixionglu
 * 修改时间：2016/12/6 下午4:36
 * 修改备注：
 */

public class StatisticsInfo {

    private String totalWorkTime;
    private String workDay;
    private long deviceId;
    private String deviceName;
    private String workRate;
    private String mouth;
    private String avgWorkTime;
    private long ranking;
    private String leading;

    public String getTotalWorkTime() {
        return totalWorkTime;
    }

    public void setTotalWorkTime(String totalWorkTime) {
        this.totalWorkTime = totalWorkTime;
    }

    public String getWorkDay() {
        return workDay;
    }

    public void setWorkDay(String workDay) {
        this.workDay = workDay;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getWorkRate() {
        return workRate;
    }

    public void setWorkRate(String workRate) {
        this.workRate = workRate;
    }

    public String getMouth() {
        return mouth;
    }

    public void setMouth(String mouth) {
        this.mouth = mouth;
    }

    public String getAvgWorkTime() {
        return avgWorkTime;
    }

    public void setAvgWorkTime(String avgWorkTime) {
        this.avgWorkTime = avgWorkTime;
    }

    public long getRanking() {
        return ranking;
    }

    public void setRanking(long ranking) {
        this.ranking = ranking;
    }

    public String getLeading() {
        return leading;
    }

    public void setLeading(String leading) {
        this.leading = leading;
    }

    @Override
    public String toString() {
        return "StatisticsInfo{" +
                "totalWorkTime='" + totalWorkTime + '\'' +
                ", workDay='" + workDay + '\'' +
                ", deviceId=" + deviceId +
                ", deviceName='" + deviceName + '\'' +
                ", workRate='" + workRate + '\'' +
                ", mouth='" + mouth + '\'' +
                ", avgWorkTime='" + avgWorkTime + '\'' +
                ", ranking=" + ranking +
                ", leading='" + leading + '\'' +
                '}';
    }
}
