package com.cloudmachine.ui.home.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.bean.AllianceDetail;
import com.cloudmachine.ui.home.model.OrderCouponBean;

import rx.Observable;

/**
 * Created by xiaojw on 2017/10/11.
 */

public interface ViewRepairContract {

    interface Model extends BaseModel {
        Observable<OrderCouponBean> getOrderCoupon(long memberId, String orderNum);
        Observable<AllianceDetail> getAllianceOrderDetail(long memberId, String orderNo);

    }

    interface View extends BaseView {
        void updateOrderCouponView(OrderCouponBean orderCouponBean);
        void updateOrderCouponError();
        void returnAllianceDetail(AllianceDetail detail);
    }

    abstract class Presenter extends BasePresenter<ViewRepairContract.View, ViewRepairContract.Model> {
        public abstract void getOrderCoupon(final android.view.View view, long memberId, String orderNum);
        public abstract void updateAllianceDetail(long memberId,String orderNo);

    }

}
