package com.cloudmachine.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaojw on 2018/9/19.
 */

public class LarkLocBean implements Parcelable {

    /**
     * deviceId : 0
     * province : 浙江省
     * city : 杭州市
     * district : 西湖区
     * position : 浙江省杭州市西湖区文华路425号
     * lng : 120.101078
     * lat : 30.294088
     * collectionTime : 2017-04-10 17:21:55
     */

    private int deviceId;
    private String province;
    private String city;
    private String district;
    private String position;
    private double lng;
    private double lat;
    private String collectionTime;

    public int getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(int deviceId) {
        this.deviceId = deviceId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getCollectionTime() {
        return collectionTime;
    }

    public void setCollectionTime(String collectionTime) {
        this.collectionTime = collectionTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.deviceId);
        dest.writeString(this.province);
        dest.writeString(this.city);
        dest.writeString(this.district);
        dest.writeString(this.position);
        dest.writeDouble(this.lng);
        dest.writeDouble(this.lat);
        dest.writeString(this.collectionTime);
    }

    public LarkLocBean() {
    }

    public LarkLocBean(Parcel in) {
        this.deviceId = in.readInt();
        this.province = in.readString();
        this.city = in.readString();
        this.district = in.readString();
        this.position = in.readString();
        this.lng = in.readDouble();
        this.lat = in.readDouble();
        this.collectionTime = in.readString();
    }

    public static final Parcelable.Creator<LarkLocBean> CREATOR = new Parcelable.Creator<LarkLocBean>() {
        @Override
        public LarkLocBean createFromParcel(Parcel source) {
            return new LarkLocBean(source);
        }

        @Override
        public LarkLocBean[] newArray(int size) {
            return new LarkLocBean[size];
        }
    };
}
