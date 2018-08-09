package com.cloudmachine.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaojw on 2018/7/12.
 */

public class TypeItem implements Parcelable {


    /**
     * code : 1
     * value : 父子(父女)
     */

    private int code;
    private String value;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.code);
        dest.writeString(this.value);
    }

    public TypeItem() {
    }

    protected TypeItem(Parcel in) {
        this.code = in.readInt();
        this.value = in.readString();
    }

    public static final Parcelable.Creator<TypeItem> CREATOR = new Parcelable.Creator<TypeItem>() {
        @Override
        public TypeItem createFromParcel(Parcel source) {
            return new TypeItem(source);
        }

        @Override
        public TypeItem[] newArray(int size) {
            return new TypeItem[size];
        }
    };
}
