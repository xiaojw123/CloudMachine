package com.cloudmachine.ui.home.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.cloudmachine.bean.CouponItem;

import java.util.List;

/**
 * Created by xiaojw on 2017/10/11.
 */

public class OrderCouponBean implements Parcelable {


    /**
     * useAmount : 50
     * needPay : 9.5
     * couponList : [{"id":null,"couponBaseId":2,"couponName":"50元无门槛券","remark":"无门槛使用券","packName":null,"userId":142,"limitInfo":0,"amount":50,"couponNum":5,"totalAmount":250,"useNum":1,"cStatus":null,"useTime":null,"startTime":null,"endTime":null},{"id":null,"couponBaseId":3,"couponName":"48元无门槛券","remark":"无门槛使用券","packName":null,"userId":142,"limitInfo":0,"amount":48,"couponNum":1,"totalAmount":48,"useNum":0,"cStatus":null,"useTime":null,"startTime":null,"endTime":null}]
     */

    private int useAmount;
    private double needPay;
    private List<CouponItem> couponList;

    public int getUseAmount() {
        return useAmount;
    }

    public void setUseAmount(int useAmount) {
        this.useAmount = useAmount;
    }

    public double getNeedPay() {
        return needPay;
    }

    public void setNeedPay(double needPay) {
        this.needPay = needPay;
    }

    public List<CouponItem> getCouponList() {
        return couponList;
    }

    public void setCouponList(List<CouponItem> couponList) {
        this.couponList = couponList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.useAmount);
        dest.writeDouble(this.needPay);
        dest.writeTypedList(this.couponList);
    }

    public OrderCouponBean() {
    }

    protected OrderCouponBean(Parcel in) {
        this.useAmount = in.readInt();
        this.needPay = in.readDouble();
        this.couponList = in.createTypedArrayList(CouponItem.CREATOR);
    }

    public static final Parcelable.Creator<OrderCouponBean> CREATOR = new Parcelable.Creator<OrderCouponBean>() {
        @Override
        public OrderCouponBean createFromParcel(Parcel source) {
            return new OrderCouponBean(source);
        }

        @Override
        public OrderCouponBean[] newArray(int size) {
            return new OrderCouponBean[size];
        }
    };
}
