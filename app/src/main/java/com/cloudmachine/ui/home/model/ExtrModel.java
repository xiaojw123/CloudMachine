package com.cloudmachine.ui.home.model;

import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.LarkMemberInfo;
import com.cloudmachine.bean.Member;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.contract.ExtrContract;

import java.util.Map;

import rx.Observable;

/**
 * Created by xiaojw on 2018/6/7.
 */

public class ExtrModel implements ExtrContract.Model {
    @Override
    public Observable<BaseRespose> getVerfyCode(String  mobile,int type) {
        return Api.getDefault(HostType.HOST_LARK).getCode(Long.parseLong(mobile), type).compose(RxSchedulers.<BaseRespose>io_main());
    }

    @Override
    public Observable<BaseRespose> identifyCode(String mobile, String code) {
        return Api.getDefault(HostType.HOST_LARK).getIdentifyCode(mobile, code).compose(RxSchedulers.<BaseRespose>io_main());
    }

    @Override
    public Observable<BaseRespose> unBind(int type) {
        return Api.getDefault(HostType.HOST_LARK).unbundled(type).compose(RxSchedulers.<BaseRespose>io_main());
    }

    @Override
    public Observable<BaseRespose> bindWxUser(Map<String, String> pm) {
        return Api.getDefault(HostType.HOST_LARK).weiXinUserInfo(pm).compose(RxSchedulers.<BaseRespose>io_main());
    }


    @Override
    public Observable<LarkMemberInfo> getMemberInfo() {
        return Api.getDefault(HostType.HOST_LARK).getLarkMemberInfo().compose(RxHelper.<LarkMemberInfo>handleResult());
    }

}
