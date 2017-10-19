package com.cloudmachine.ui.home.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.ui.home.contract.MyCouponContract;
import com.cloudmachine.ui.home.model.CouponBean;

/**
 * Created by xiaojw on 2017/10/11.
 */

public class MyCouponPresenter extends MyCouponContract.Presenter {
    @Override
    public void getAvaildCouponList(long memberid) {
        mRxManage.add(mModel.getAvaildCouponList(memberid).subscribe(new RxSubscriber<CouponBean>(mContext) {
            @Override
            protected void _onNext(CouponBean couponBean) {
                if (couponBean != null) {
                    mView.updateAvaildCouponListView(couponBean.getSumNum(),couponBean.getSumAmount(), couponBean.getUserCouponBOList());
                }
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }

    @Override
    public void getInavaildCouponList(long memberid) {
        mRxManage.add(mModel.getInavaildCouponList(memberid).subscribe(new RxSubscriber<CouponBean>(mContext) {
            @Override
            protected void _onNext(CouponBean couponBean) {
                mView.updateInavaildCouponListView(couponBean.getSumNum(),couponBean.getUserCouponBOList());
            }

            @Override
            protected void _onError(String message) {

            }
        }));

    }

    @Override
    public void getMyCouponDetailList(long memberid, final int couponBaseId) {
        mRxManage.add(mModel.getMyCouponDetailList(memberid, couponBaseId).subscribe(new RxSubscriber<CouponBean>(mContext) {
            @Override
            protected void _onNext(CouponBean couponBean) {
                if (couponBean != null) {
                    mView.updateMyCouponDetailListView(couponBean.getSumAmount(), couponBean.getUserCouponBOList());
                }

            }

            @Override
            protected void _onError(String message) {

            }
        }));

    }
}
