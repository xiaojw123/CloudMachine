package com.cloudmachine.ui.personal.model;

import com.cloudmachine.net.api.Api;
import com.cloudmachine.net.api.HostType;
import com.cloudmachine.base.baserx.RxHelper;
import com.cloudmachine.bean.Member;
import com.cloudmachine.bean.ScoreInfo;
import com.cloudmachine.ui.personal.contract.PersonalContract;

import rx.Observable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/18 上午8:53
 * 修改人：shixionglu
 * 修改时间：2017/3/18 上午8:53
 * 修改备注：
 */

public class PersonalModel implements PersonalContract.Model{

    @Override
    public Observable<Member> getMemberInfoById(long memberId) {
        return Api.getDefault(HostType.CLOUDM_HOST).getMemberInfoById(memberId)
                .compose(RxHelper.<Member>handleResult());
    }

    @Override
    public Observable<ScoreInfo> getUserScoreInfo(Long memberId) {
        return Api.getDefault(HostType.CLOUDM_HOST)
                .getUserScoreInfo(memberId)
                .compose(RxHelper.<ScoreInfo>handleResult());
    }
}
