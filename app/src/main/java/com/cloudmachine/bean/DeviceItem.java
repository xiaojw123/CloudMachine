package com.cloudmachine.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaojw on 2018/6/29.
 */

public class DeviceItem implements Parcelable {


    /**
     * id : 698
     * name : 郑庆海27
     * typeId : 1
     * type : 1
     * lng : 116.936757
     * lat : 30.70694
     * workStatus : 0
     * snId : 01201606B0000092
     * typePicUrl : http://medias.cloudm.com/static/app/map/wa_jue_ji
     */

    private int id;
    private String name;
    private int typeId;
    private int type;
    private double lng;
    private double lat;
    private int workStatus;
    private String snId;
    private String typePicUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public int getWorkStatus() {
        return workStatus;
    }

    public void setWorkStatus(int workStatus) {
        this.workStatus = workStatus;
    }

    public String getSnId() {
        return snId;
    }

    public void setSnId(String snId) {
        this.snId = snId;
    }

    public String getTypePicUrl() {
        return typePicUrl;
    }

    public void setTypePicUrl(String typePicUrl) {
        this.typePicUrl = typePicUrl;
    }

    public void setSelected(boolean sel) {
        this.sel = sel;
    }

    public boolean isSelected() {
        return sel;
    }

    private boolean sel;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.typeId);
        dest.writeInt(this.type);
        dest.writeDouble(this.lng);
        dest.writeDouble(this.lat);
        dest.writeInt(this.workStatus);
        dest.writeString(this.snId);
        dest.writeString(this.typePicUrl);
        dest.writeByte(this.sel ? (byte) 1 : (byte) 0);
    }

    public DeviceItem() {
    }

    protected DeviceItem(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.typeId = in.readInt();
        this.type = in.readInt();
        this.lng = in.readDouble();
        this.lat = in.readDouble();
        this.workStatus = in.readInt();
        this.snId = in.readString();
        this.typePicUrl = in.readString();
        this.sel = in.readByte() != 0;
    }

    public static final Parcelable.Creator<DeviceItem> CREATOR = new Parcelable.Creator<DeviceItem>() {
        @Override
        public DeviceItem createFromParcel(Parcel source) {
            return new DeviceItem(source);
        }

        @Override
        public DeviceItem[] newArray(int size) {
            return new DeviceItem[size];
        }
    };
}
