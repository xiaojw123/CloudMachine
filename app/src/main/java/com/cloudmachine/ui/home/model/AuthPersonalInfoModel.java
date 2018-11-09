package com.cloudmachine.ui.home.model;

import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.contract.AuthPersonalInfoContract;
import com.google.gson.JsonObject;

import rx.Observable;
import rx.schedulers.Schedulers;

/**
 * Created by xiaojw on 2018/7/10.
 */

public class AuthPersonalInfoModel implements AuthPersonalInfoContract.Model {
    @Override
    public Observable<JsonObject> getMemberAuthInfo() {
        return Api.getDefault(HostType.HOST_LARK).getMemberAuthInfo().compose(RxHelper.<JsonObject>handleResult());
    }

    @Override
    public Observable<String> submitIdUserInfo(String redisUserId) {
        return Api.getDefault(HostType.HOST_LARK).submitIdUserInfo(redisUserId).compose(RxHelper.<String>handleResult());
    }

    @Override
    public Observable<JsonObject> verifyOcr(String imgUrl,String redisUserId) {
        return Api.getDefault(HostType.HOST_LARK).verifyOcr(imgUrl,redisUserId);
    }
}
