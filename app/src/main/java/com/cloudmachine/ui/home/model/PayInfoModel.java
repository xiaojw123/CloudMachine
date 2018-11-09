package com.cloudmachine.ui.home.model;

import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.contract.PayInfoContract;

import rx.Observable;

/**
 * Created by xiaojw on 2018/6/15.
 */

public class PayInfoModel implements PayInfoContract.Model {

    @Override
    public Observable<String> getVerfyCode(String mobile, int type) {
        return Api.getDefault(HostType.HOST_LARK).getCode(Long.parseLong(mobile),type).compose(RxHelper.handleBooleanResult());
    }

}
