package com.cloudmachine.ui.home.model;

import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.Member;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.contract.PurseContract;
import com.google.gson.JsonObject;

import java.util.Map;

import rx.Observable;

/**
 * Created by xiaojw on 2017/12/6.
 */

public class PurseModel implements PurseContract.Model{

    @Override
    public Observable<JsonObject> getWalletAmount(long memberId) {
        return Api.getDefault(HostType.HOST_CLOUDM).getWalletAmount(memberId).compose(RxHelper.<JsonObject>handleResult());
    }

    @Override
    public Observable<CouponBean> getAvaildCouponList(long memberid) {
        return Api.getDefault(HostType.HOST_CLOUDM).getAvalidCouponList(memberid).compose(RxHelper.<CouponBean>handleResult());
    }

    @Override
    public Observable<Member> getMemberInfo(long memberid) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getMemberInfoById(memberid).compose(RxHelper.<Member>handleResult());
    }


}
