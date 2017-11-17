package com.cloudmachine.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaojw on 2017/11/9.
 */

public class PickItemBean implements Parcelable {


    /**
     * key : 867923045021004/2017-11-14/Pic/2017-11-14&16-53-26.jpg
     * hash : Fog0g74E6wrBnTsESXsfG6WvSFe1
     * fsize : 114452
     * putTime : 2017-11-14 16:53:27
     * mimeType : image/jpeg
     * endUser :
     * origin : http://oxqplnjn2.bkt.clouddn.com
     */

    private String key;
    private String hash;
    private int fsize;
    private String putTime;
    private String mimeType;
    private String endUser;
    private String origin;
    private String imgUrl;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public int getFsize() {
        return fsize;
    }

    public void setFsize(int fsize) {
        this.fsize = fsize;
    }

    public String getPutTime() {
        return putTime;
    }

    public void setPutTime(String putTime) {
        this.putTime = putTime;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getEndUser() {
        return endUser;
    }

    public void setEndUser(String endUser) {
        this.endUser = endUser;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getImgUrl() {
        return origin+"/"+key;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public PickItemBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.hash);
        dest.writeInt(this.fsize);
        dest.writeString(this.putTime);
        dest.writeString(this.mimeType);
        dest.writeString(this.endUser);
        dest.writeString(this.origin);
        dest.writeString(this.imgUrl);
    }

    protected PickItemBean(Parcel in) {
        this.key = in.readString();
        this.hash = in.readString();
        this.fsize = in.readInt();
        this.putTime = in.readString();
        this.mimeType = in.readString();
        this.endUser = in.readString();
        this.origin = in.readString();
        this.imgUrl = in.readString();
    }

    public static final Creator<PickItemBean> CREATOR = new Creator<PickItemBean>() {
        @Override
        public PickItemBean createFromParcel(Parcel source) {
            return new PickItemBean(source);
        }

        @Override
        public PickItemBean[] newArray(int size) {
            return new PickItemBean[size];
        }
    };
}
