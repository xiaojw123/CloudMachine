package com.cloudmachine.ui.home.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.bean.LarkMemberInfo;
import com.cloudmachine.bean.CouponBean;
import com.google.gson.JsonObject;

import rx.Observable;

/**
 * Created by xiaojw on 2017/12/6.
 */

public interface PurseContract {

    interface View extends BaseView {
        void updateWalletAmountView(double walletAmount,String jumpUrl);
        void updateMemberInfo(LarkMemberInfo member);
    }

    interface Model extends BaseModel {
        Observable<JsonObject> getWalletAmount();
        Observable<LarkMemberInfo> getMemberInfo();
    }

    public abstract class Presenter extends BasePresenter<PurseContract.View, PurseContract.Model> {
        public abstract void getWalletAmount();
        public abstract void getMemberInfo();
    }


}
