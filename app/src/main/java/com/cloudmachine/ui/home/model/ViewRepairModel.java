package com.cloudmachine.ui.home.model;

import com.cloudmachine.base.baserx.RxHelper;
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
        return Api.getDefault(HostType.CLOUDM_HOST).getOrderCouponList(memberId,orderNum).compose(RxHelper.<OrderCouponBean>handleResult());
    }
}
