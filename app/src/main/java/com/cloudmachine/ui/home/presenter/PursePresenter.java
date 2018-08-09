package com.cloudmachine.ui.home.presenter;

import android.text.TextUtils;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.Member;
import com.cloudmachine.ui.home.contract.PurseContract;
import com.cloudmachine.ui.home.model.CouponBean;
import com.cloudmachine.utils.ToastUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

/**
 * Created by xiaojw on 2017/12/6.
 */

public class PursePresenter extends PurseContract.Presenter {

    @Override
    public void getWalletAmount(long memberId) {
        mRxManage.add(mModel.getWalletAmount(memberId).subscribe(new RxSubscriber<JsonObject>(mContext) {
            @Override
            protected void _onNext(JsonObject jsonObject) {
                if (jsonObject != null) {
                    double walletAmount = jsonObject.get("walletAmount").getAsDouble();
                    double depositAmount = jsonObject.get("depositAmount").getAsDouble();
                    JsonElement jrlJe=jsonObject.get("jumpUrl");
                    String jumpUrl=null;
                    if (jrlJe!=null&&!jrlJe.isJsonNull()){
                        jumpUrl=jrlJe.getAsString();
                    }
                    mView.updateWalletAmountView(walletAmount, depositAmount,jumpUrl);
                }

            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }



    @Override
    public void getAvaildCouponList(long memberid) {
        mRxManage.add(mModel.getAvaildCouponList(memberid).subscribe(new RxSubscriber<CouponBean>(mContext) {
            @Override
            protected void _onNext(CouponBean couponBean) {
                if (couponBean != null) {
                    mView.updateAvaildCouponSumNum(couponBean.getSumNum());
                }
            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }






}
