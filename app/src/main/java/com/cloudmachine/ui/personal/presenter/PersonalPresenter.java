package com.cloudmachine.ui.personal.presenter;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.bean.Member;
import com.cloudmachine.bean.ScoreInfo;
import com.cloudmachine.ui.personal.contract.PersonalContract;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.chart.utils.AppLog;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/18 下午3:21
 * 修改人：shixionglu
 * 修改时间：2017/3/18 下午3:21
 * 修改备注：
 */

public class PersonalPresenter extends PersonalContract.Presenter {

    @Override
    public void getMemberInfoById(long memberId) {
        AppLog.print("Preseneter getMemberInfoById__");
        mRxManage.add(mModel.getMemberInfoById(memberId)
        .subscribe(new RxSubscriber<Member>(mContext,false) {
            @Override
            protected void _onNext(Member member) {
                AppLog.print("getMemberInfoById _onNext___");
                mView.returnMemberInfo(member);
            }

            @Override
            protected void _onError(String message) {
                AppLog.print("getMemberInfoById _onError___"+message);
                ToastUtils.error(message,true);
            }
        }));
    }

    @Override
    public void getUserScoreInfo(Long memberId) {
        mRxManage.add(mModel.getUserScoreInfo(memberId)
        .subscribe(new RxSubscriber<ScoreInfo>(mContext,false) {
            @Override
            protected void _onNext(ScoreInfo scoreInfo) {
                mView.returnUserScoreInfo(scoreInfo);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.error(message,true);
            }
        }));
    }
}
