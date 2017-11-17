package com.cloudmachine.ui.home.model;

import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.contract.MyCouponContract;

import rx.Observable;

/**
 * Created by xiaojw on 2017/10/11.
 */

public class MyCouponModel implements MyCouponContract.Model {
    @Override
    public Observable<CouponBean> getAvaildCouponList(long memberid) {
        return Api.getDefault(HostType.HOST_CLOUDM).getAvalidCouponList(memberid).compose(RxHelper.<CouponBean>handleResult());
    }

    @Override
    public Observable<CouponBean> getInavaildCouponList(long memberid) {
        return Api.getDefault(HostType.HOST_CLOUDM).getInvaildCouponList(memberid).compose(RxHelper.<CouponBean>handleResult());
    }

    @Override
    public Observable<CouponBean> getMyCouponDetailList(long memberid, int couponBaseId) {
        return Api.getDefault(HostType.HOST_CLOUDM).getMyCouponDetailList(memberid,couponBaseId).compose(RxHelper.<CouponBean>handleResult());
    }
}
