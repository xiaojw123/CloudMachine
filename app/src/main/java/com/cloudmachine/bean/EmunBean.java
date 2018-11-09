package com.cloudmachine.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaojw on 2018/9/21.
 */

public class EmunBean implements Parcelable {


    /**
     * keyType : 2
     * description : 拥有为机器添加和删除成员的权限(适用于调度者、租赁者、工程业主等)
     * valueName : 管理者
     */

    private int keyType;
    private String description;
    private String valueName;

    public int getKeyType() {
        return keyType;
    }

    public void setKeyType(int keyType) {
        this.keyType = keyType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.keyType);
        dest.writeString(this.description);
        dest.writeString(this.valueName);
    }

    public EmunBean() {
    }

    protected EmunBean(Parcel in) {
        this.keyType = in.readInt();
        this.description = in.readString();
        this.valueName = in.readString();
    }

    public static final Parcelable.Creator<EmunBean> CREATOR = new Parcelable.Creator<EmunBean>() {
        @Override
        public EmunBean createFromParcel(Parcel source) {
            return new EmunBean(source);
        }

        @Override
        public EmunBean[] newArray(int size) {
            return new EmunBean[size];
        }
    };
}
