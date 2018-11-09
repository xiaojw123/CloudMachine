package com.cloudmachine.ui.home.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.LarkMemberInfo;
import com.cloudmachine.ui.home.contract.PurseContract;
import com.cloudmachine.bean.CouponBean;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by xiaojw on 2017/12/6.
 */

public class PursePresenter extends PurseContract.Presenter {

    @Override
    public void getWalletAmount() {
        mRxManage.add(mModel.getWalletAmount().subscribe(new RxSubscriber<JsonObject>(mContext) {
            @Override
            protected void _onNext(JsonObject jsonObject) {
                if (jsonObject != null) {
                    JsonElement walletJE=jsonObject.get("walletAmount");
                    JsonElement jrlJe=jsonObject.get("jumpUrl");
                    double walletAmount=0;
                    if (walletJE!=null&&!walletJE.isJsonNull()){
                        walletAmount = walletJE.getAsDouble();
                    }
                    String jumpUrl=null;
                    if (jrlJe!=null&&!jrlJe.isJsonNull()){
                        jumpUrl=jrlJe.getAsString();
                    }
                    mView.updateWalletAmountView(walletAmount,jumpUrl);
                }

            }

            @Override
            protected void _onError(String message) {

            }
        }));
    }


    @Override
    public void getMemberInfo() {
        mRxManage.add(mModel.getMemberInfo().subscribe(new RxSubscriber<LarkMemberInfo>(mContext) {
            @Override
            protected void _onNext(LarkMemberInfo member) {
                mView.updateMemberInfo(member);
            }

            @Override
            protected void _onError(String message) {

            }
        }));

    }


}
