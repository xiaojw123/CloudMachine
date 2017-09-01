package com.cloudmachine.ui.personal.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.bean.Member;
import com.cloudmachine.bean.ScoreInfo;

import rx.Observable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/18 上午8:56
 * 修改人：shixionglu
 * 修改时间：2017/3/18 上午8:56
 * 修改备注：
 */

public interface PersonalContract {

    interface Model extends BaseModel {

        Observable<Member> getMemberInfoById(long memberId);

        Observable<ScoreInfo> getUserScoreInfo(Long memberId);
    }

    interface View extends BaseView {
        void returnMemberInfo(Member member);

        void returnUserScoreInfo(ScoreInfo scoreInfo);
    }

    abstract static class Presenter extends BasePresenter<View,Model> {
        public abstract void getMemberInfoById(long memberId);

        public abstract void getUserScoreInfo(Long memberId);
    }

}
