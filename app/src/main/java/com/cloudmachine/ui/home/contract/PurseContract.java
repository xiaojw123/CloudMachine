package com.cloudmachine.ui.home.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.Member;
import com.cloudmachine.ui.home.model.CouponBean;

import java.util.Map;

import rx.Observable;

/**
 * Created by xiaojw on 2017/12/6.
 */

public interface PurseContract {

    interface View extends BaseView {
        void returnVerfyCode(int type);
        void returnToastError(String message);
        void returnIdentifyCode(int type);
        void returnUnBind(int type,boolean isSuccess);
        void updateBindWxUserView(String accountText);
        void updateAvaildCouponSumNum(int sumNum);
        void returnMemberInfo(Member member);
    }

    interface Model extends BaseModel {

        Observable<BaseRespose> getVerfyCode(String  mobile);
        Observable<BaseRespose> identifyCode(String mobile,String code);
        Observable<BaseRespose> unBind(long memberId,int type);
        Observable<BaseRespose> bindWxUser(Map<String,String> pm);
        Observable<CouponBean> getAvaildCouponList(long memberid);
        Observable<Member> getMemberInfo(long memberId);
    }

    public abstract class Presenter extends BasePresenter<PurseContract.View, PurseContract.Model> {

        public abstract void getVerfyCode(int type,String mobile);
        public abstract void identifyCode(int type,String mobile,String code);
        public abstract void unBind(long memberId,int type);
        public abstract void bindWxUser(Map<String, String> pm,String wxNickName,String openId);
        public  abstract void getAvaildCouponList(long memberid);
        public abstract void getMemberInfo(long memberId);


    }


}
