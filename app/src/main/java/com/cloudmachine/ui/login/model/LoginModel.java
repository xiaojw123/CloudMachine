package com.cloudmachine.ui.login.model;

import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.bean.LarkMemberInfo;
import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.ui.login.contract.LoginContract;
import com.cloudmachine.utils.CommonUtils;
import com.google.gson.JsonObject;
import com.umeng.socialize.sina.helper.MD5;

import rx.Observable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/27 下午1:34
 * 修改人：shixionglu
 * 修改时间：2017/3/27 下午1:34
 * 修改备注：
 */

public class LoginModel implements LoginContract.Model {
    @Override
    public Observable<JsonObject> login(String userName, String password) {
        String timeStap=CommonUtils.getTimeStamp();
        String sign=CommonUtils.getD5Str("passworduserName"+timeStap);
        return Api.getDefault(HostType.HOST_LARK).login(userName, password,timeStap,sign).compose(RxHelper.handleCommonResult(JsonObject.class));
    }

    @Override
    public Observable<LarkMemberInfo> getMemberInfo() {
        return Api.getDefault(HostType.HOST_LARK).getLarkMemberInfo().compose(RxHelper.<LarkMemberInfo>handleResult());
    }

    @Override
    public Observable<JsonObject> loginByWx(String unionId, String openId, String nickName, String headLogo) {
        return Api.getDefault(HostType.HOST_LARK)
                .wxLogin(unionId, openId, nickName, headLogo)
                .compose(RxSchedulers.<JsonObject>io_main());
    }


}
