package com.cloudmachine.ui.login.model;

import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.login.contract.FindPasswordContract;
import com.google.gson.JsonObject;

import java.util.Map;

import rx.Observable;

/**
 * Created by xiaojw on 2017/10/18.
 */

public class FindPasswordModel implements FindPasswordContract.Model {
    @Override
    public Observable<JsonObject> register(Map<String, String> paramsMap) {
        return Api.getDefault(HostType.GUOSHUAI_HOST).register(paramsMap).compose(RxHelper.<JsonObject>handleResult());
    }

    @Override
    public Observable<JsonObject> getMobileCode(String mobile, String type) {
        return Api.getDefault(HostType.CLOUDM_HOST).getCode(mobile, type).compose(RxHelper.<JsonObject>handleResult());
    }

    @Override
    public Observable<JsonObject> forgetPassword(String mobile, String pwd, String code) {
        return Api.getDefault(HostType.CLOUDM_HOST).forgetPassword(mobile, pwd, code).compose(RxHelper.<JsonObject>handleResult());
    }
}
