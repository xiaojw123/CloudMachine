package com.cloudmachine.ui.login.model;

import com.cloudmachine.api.Api;
import com.cloudmachine.api.HostType;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.recyclerbean.CheckNumBean;
import com.cloudmachine.struc.Member;
import com.cloudmachine.ui.login.contract.VerifyPhoneNumContract;

import rx.Observable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/27 下午2:46
 * 修改人：shixionglu
 * 修改时间：2017/3/27 下午2:46
 * 修改备注：
 */

public class VerifyPhoneNumModel implements VerifyPhoneNumContract.Model {

    @Override
    public Observable<BaseRespose> wxBindMobile(long mobile, long type) {
        return Api.getDefault(HostType.CAITINGTING_HOST).wxBindMobile(mobile,type);
    }

    @Override
    public Observable<CheckNumBean> checkNum(long mobile) {
        return Api.getDefault(HostType.CAITINGTING_HOST).checkNum(mobile)
                .compose(RxHelper.<CheckNumBean>handleResult())
                ;
    }

    @Override
    public Observable<Member> bindWx(String unionId, String openId, String account,
                                     String code, String inviteCode, String pwd,
                                     String nickname, String headLogo, Integer type) {
        return Api.getDefault(HostType.CAITINGTING_HOST).wxBind(unionId,openId,account,code,inviteCode
        ,pwd,nickname,headLogo,type)
                .compose(RxHelper.<Member>handleResult());
    }
}
