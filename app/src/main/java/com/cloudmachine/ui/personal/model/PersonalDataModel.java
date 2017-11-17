package com.cloudmachine.ui.personal.model;

import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.ui.personal.contract.PersonalDataContract;

import rx.Observable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/4/7 下午4:35
 * 修改人：shixionglu
 * 修改时间：2017/4/7 下午4:35
 * 修改备注：
 */

public class PersonalDataModel implements PersonalDataContract.Model{
    @Override
    public Observable<String> modifyNickName(long memberId, String key, String value) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).modifySignature(memberId,key,value)
                .compose(RxHelper.handleBooleanResult());
    }

    @Override
    public Observable<String> modifyLogo(long memberId, String key, String value) {
        return Api.getDefault(HostType.HOST_CLOUDM_YJX).modifySignature(memberId, key, value)
                .compose(RxHelper.handleBooleanResult());
    }
}
