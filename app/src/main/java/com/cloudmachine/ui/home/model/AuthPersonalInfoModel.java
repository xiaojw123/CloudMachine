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
    public Observable<JsonObject> getMemberAuthInfo(long memberId) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getMemberAuthInfo(memberId).compose(RxHelper.<JsonObject>handleResult());
    }

    @Override
    public Observable<String> submitIdUserInfo(long memberId, String redisUserId) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).submitIdUserInfo(memberId,redisUserId).compose(RxHelper.<String>handleResult());
    }

    @Override
    public Observable<JsonObject> verifyOcr(long memberId, String imgUrl,String redisUserId) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).verifyOcr(memberId,imgUrl,redisUserId);
    }
}
