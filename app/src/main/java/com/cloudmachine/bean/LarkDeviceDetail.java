package com.cloudmachine.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaojw on 2018/9/18.
 */

public class LarkDeviceDetail  implements Parcelable {

    public boolean  isSimpleBox(){
        return isSimpleBox==1;
    }

    public int getIsSimpleBox() {
        return isSimpleBox;
    }

    public void setIsSimpleBox(int isSimpleBox) {
        this.isSimpleBox = isSimpleBox;
    }

    /**
     * deviceId : 0
     * deviceName : 体验机
     * typeId : 1
     * typePicUrl : http://medias.cloudm.com/static/app/map/wa_jue_ji
     * category : null
     * location : {"deviceId":0,"province":"浙江省","city":"杭州市","district":"西湖区","position":"浙江省杭州市西湖区文华路425号","lng":120.101078,"lat":30.294088,"collectionTime":"2017-04-10 17:21:55"}
     * workStatus : 0
     * oilLave : 49
     * workTime : 1
     * snId : 156018434650373831FFDB05
     * signalIntensity : null

     * positAccuracy : null
     */
    private int isSimpleBox;

    private int deviceId;

    public int getRoleType() {
        return roleType;
    }

    public void setRoleType(int roleType) {
        this.roleType = roleType;
    }

    private String deviceName;
    private int typeId;
    private int roleType;
    private String typePicUrl;
    private String category;
    private LarkLocBean location;
    private int workStatus;
    private int oilLave;
    private float workTime;
    private String snId;
    private String signalIntensity;
    private String positAccuracy;

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getTypePicUrl() {
        return typePicUrl;
    }

    public void setTypePicUrl(String typePicUrl) {
        this.typePicUrl = typePicUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LarkLocBean getLocation() {
        return location;
    }

    public void setLocation(LarkLocBean location) {
        this.location = location;
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

    public float getWorkTime() {
        return workTime;
    }

    public void setWorkTime(float workTime) {
        this.workTime = workTime;
    }

    public String getSnId() {
        return snId;
    }

    public void setSnId(String snId) {
        this.snId = snId;
    }

    public Object getSignalIntensity() {
        return signalIntensity;
    }

    public void setSignalIntensity(String signalIntensity) {
        this.signalIntensity = signalIntensity;
    }

    public String getPositAccuracy() {
        return positAccuracy;
    }

    public void setPositAccuracy(String positAccuracy) {
        this.positAccuracy = positAccuracy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.deviceId);
        dest.writeString(this.deviceName);
        dest.writeInt(this.typeId);
        dest.writeInt(this.roleType);
        dest.writeString(this.typePicUrl);
        dest.writeString(this.category);
        dest.writeParcelable(this.location, flags);
        dest.writeInt(this.workStatus);
        dest.writeInt(this.oilLave);
        dest.writeFloat(this.workTime);
        dest.writeString(this.snId);
        dest.writeString(this.signalIntensity);
        dest.writeString(this.positAccuracy);
    }

    public LarkDeviceDetail() {
    }

    protected LarkDeviceDetail(Parcel in) {
        this.deviceId = in.readInt();
        this.deviceName = in.readString();
        this.typeId = in.readInt();
        this.roleType = in.readInt();
        this.typePicUrl = in.readString();
        this.category = in.readString();
        this.location = in.readParcelable(LarkLocBean.class.getClassLoader());
        this.workStatus = in.readInt();
        this.oilLave = in.readInt();
        this.workTime = in.readFloat();
        this.snId = in.readString();
        this.signalIntensity = in.readString();
        this.positAccuracy = in.readString();
    }

    public static final Creator<LarkDeviceDetail> CREATOR = new Creator<LarkDeviceDetail>() {
        @Override
        public LarkDeviceDetail createFromParcel(Parcel source) {
            return new LarkDeviceDetail(source);
        }

        @Override
        public LarkDeviceDetail[] newArray(int size) {
            return new LarkDeviceDetail[size];
        }
    };
}
