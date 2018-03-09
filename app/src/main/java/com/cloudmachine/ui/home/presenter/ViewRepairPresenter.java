package com.cloudmachine.ui.home.presenter;

import android.view.View;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.AllianceDetail;
import com.cloudmachine.ui.home.contract.ViewRepairContract;
import com.cloudmachine.ui.home.model.OrderCouponBean;

/**
 * Created by xiaojw on 2017/10/11.
 */

public class ViewRepairPresenter extends ViewRepairContract.Presenter {
    @Override
    public void getOrderCoupon(final View view, long memberId, String orderNum) {
        mRxManage.add(mModel.getOrderCoupon(memberId, orderNum).subscribe(new RxSubscriber<OrderCouponBean>(mContext) {
            @Override
            protected void _onNext(OrderCouponBean orderCouponBean) {
                view.setEnabled(true);
                mView.updateOrderCouponView(orderCouponBean);
            }

            @Override
            protected void _onError(String message) {
                view.setEnabled(true);
                mView.updateOrderCouponError();
            }
        }));
    }

    @Override
    public void updateAllianceDetail(long memberId, String orderNo) {
        mRxManage.add(mModel.getAllianceOrderDetail(memberId,orderNo).subscribe(new RxSubscriber<AllianceDetail>(mContext) {
            @Override
            protected void _onNext(AllianceDetail detail) {
                mView.returnAllianceDetail(detail);
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }

}
