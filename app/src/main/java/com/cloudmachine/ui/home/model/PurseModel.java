package com.cloudmachine.ui.home.model;

import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.Member;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.home.contract.PurseContract;

import java.util.Map;

import rx.Observable;

/**
 * Created by xiaojw on 2017/12/6.
 */

public class PurseModel implements PurseContract.Model{

    @Override
    public Observable<BaseRespose> getVerfyCode(String  mobile) {
        return Api.getDefault(HostType.HOST_CLOUDM).wxBindMobile(Long.parseLong(mobile), 2).compose(RxSchedulers.<BaseRespose>io_main());
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
    public Observable<CouponBean> getAvaildCouponList(long memberid) {
        return Api.getDefault(HostType.HOST_CLOUDM).getAvalidCouponList(memberid).compose(RxHelper.<CouponBean>handleResult());
    }

    @Override
    public Observable<Member> getMemberInfo(long memberId) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).getMemberInfoById(memberId).compose(RxHelper.<Member>handleResult());
    }


}
