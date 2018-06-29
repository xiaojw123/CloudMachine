package com.cloudmachine.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xiaojw on 2018/6/12.
 */

public class Operator implements Parcelable {


    /**
     * id : 31
     * name : Wangbo
     * nickName : qq
     * isAuth : 0
     * gmtCreate : null
     * salaryAmount : null
     */

    private int id;
    private String name;
    private String nickName;
    private int isAuth;
    private String gmtCreate;
    private String salaryAmount;
    private String amount;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

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

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public int getIsAuth() {
        return isAuth;
    }

    public void setIsAuth(int isAuth) {
        this.isAuth = isAuth;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getSalaryAmount() {
        return salaryAmount;
    }

    public void setSalaryAmount(String salaryAmount) {
        this.salaryAmount = salaryAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.nickName);
        dest.writeInt(this.isAuth);
        dest.writeString(this.gmtCreate);
        dest.writeString(this.salaryAmount);
        dest.writeString(this.amount);
    }

    public Operator() {
    }

    private Operator(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.nickName = in.readString();
        this.isAuth = in.readInt();
        this.gmtCreate = in.readString();
        this.salaryAmount = in.readString();
        this.amount=in.readString();
    }

    public static final Parcelable.Creator<Operator> CREATOR = new Parcelable.Creator<Operator>() {
        @Override
        public Operator createFromParcel(Parcel source) {
            return new Operator(source);
        }

        @Override
        public Operator[] newArray(int size) {
            return new Operator[size];
        }
    };
}
