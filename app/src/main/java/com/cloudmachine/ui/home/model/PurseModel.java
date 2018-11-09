package com.cloudmachine.ui.home.model;

import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.bean.CouponBean;
import com.cloudmachine.bean.LarkMemberInfo;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.contract.PurseContract;
import com.google.gson.JsonObject;

import rx.Observable;

/**
 * Created by xiaojw on 2017/12/6.
 */

public class PurseModel implements PurseContract.Model{

    @Override
    public Observable<JsonObject> getWalletAmount() {
        return Api.getDefault(HostType.HOST_LARK).getWalletAmount().compose(RxHelper.<JsonObject>handleResult());
    }


    @Override
    public Observable<LarkMemberInfo> getMemberInfo() {
        return Api.getDefault(HostType.HOST_LARK).getLarkMemberInfo().compose(RxHelper.<LarkMemberInfo>handleResult());
    }


}
