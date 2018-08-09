package com.cloudmachine.ui.home.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.Member;
import com.cloudmachine.ui.home.model.CouponBean;
import com.google.gson.JsonObject;

import java.util.Map;

import rx.Observable;

/**
 * Created by xiaojw on 2017/12/6.
 */

public interface PurseContract {

    interface View extends BaseView {
        void updateWalletAmountView(double walletAmount,double depositAmount,String jumpUrl);
        void updateAvaildCouponSumNum(int sumNum);
    }

    interface Model extends BaseModel {
        Observable<JsonObject> getWalletAmount(long memberId);
        Observable<CouponBean> getAvaildCouponList(long memberid);
    }

    public abstract class Presenter extends BasePresenter<PurseContract.View, PurseContract.Model> {
        public abstract void getWalletAmount(long memberId);
        public  abstract void getAvaildCouponList(long memberid);


    }


}
