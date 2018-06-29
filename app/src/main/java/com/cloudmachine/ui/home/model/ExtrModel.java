package com.cloudmachine.ui.home.model;

import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.bean.BaseRespose;
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
        return Api.getDefault(HostType.HOST_CLOUDM).wxBindMobile(Long.parseLong(mobile), type).compose(RxSchedulers.<BaseRespose>io_main());
    }

    @Override
    public Observable<BaseRespose> identifyCode(String mobile, String code) {
        return Api.getDefault(HostType.HOST_CLOUDM).getIdentifyCode(mobile, code).compose(RxSchedulers.<BaseRespose>io_main());
    }

    @Override
    public Observable<BaseRespose> unBind(long memberId, int type) {
        return Api.getDefault(HostType.HOST_CLOUDM).unbundled(memberId, type).compose(RxSchedulers.<BaseRespose>io_main());
    }

    @Override
    public Observable<BaseRespose> bindWxUser(Map<String, String> pm) {
        return Api.getDefault(HostType.HOST_CLOUDM).weiXinUserInfo(pm).compose(RxSchedulers.<BaseRespose>io_main());
    }

    @Override
    public Observable<Member> getMemberInfo(long memberId) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getMemberInfoById(memberId).compose(RxHelper.<Member>handleResult());
    }

}
