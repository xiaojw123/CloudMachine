package com.cloudmachine.ui.home.model;

import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.bean.AllianceDetail;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.contract.ViewRepairContract;

import rx.Observable;

/**
 * Created by xiaojw on 2017/10/11.
 */

public class ViewRepairModel implements ViewRepairContract.Model {
    @Override
    public Observable<OrderCouponBean> getOrderCoupon(long memberId, String orderNum) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getOrderCouponList(memberId,orderNum).compose(RxHelper.<OrderCouponBean>handleResult());
    }

    @Override
    public Observable<AllianceDetail> getAllianceOrderDetail(long memberId, String orderNo) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getAllianceOrderDetail(memberId,orderNo).compose(RxHelper.<AllianceDetail>handleResult());
    }
}