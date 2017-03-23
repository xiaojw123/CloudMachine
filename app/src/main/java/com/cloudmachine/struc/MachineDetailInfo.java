package com.cloudmachine.struc;

import java.io.Serializable;

/**
 * Created by shixionglu on 2016/10/17.
 */

public class MachineDetailInfo implements Serializable{



    /**
	 * 
	 */
	private static final long serialVersionUID = -5897226590221878339L;
	private float workTime;
    private long deviceId;
    private long repairId;
    private String deviceName;
    private int workStatus;
    private int oilLave;

    private McDeviceLocation location;

    public long getRepairId() {
        return repairId;
    }

    public void setRepairId(long repairId) {
        this.repairId = repairId;
    }

    public float getWorkTime() {
        return workTime;
    }

    public void setWorkTime(float workTime) {
        this.workTime = workTime;
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

    public int getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(int workStatus) {
        this.workStatus = workStatus;
    }

    public int getOilLave() {
        return oilLave;
    }

    public void setOilLave(int oilLave) {
        this.oilLave = oilLave;
    }

    public McDeviceLocation getLocation() {
        return location;
    }

    public void setLocation(McDeviceLocation location) {
        this.location = location;
    }
	
}
