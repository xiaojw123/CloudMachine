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
 * Created by xiaojw on 2018/6/7.
 */

public interface ExtrContract {

    interface Model extends BaseModel{


        Observable<BaseRespose> getVerfyCode(String  mobile,int type);
        Observable<BaseRespose> identifyCode(String mobile,String code);
        Observable<BaseRespose> unBind(long memberId,int type);
        Observable<BaseRespose> bindWxUser(Map<String,String> pm);
        Observable<Member> getMemberInfo(long memberId);

    }

    interface View extends BaseView{

        void returnVerfyCode(int type);
        void returnIdentifyCode(int type);
        void returnUnBind(int type,boolean isSuccess);
        void updateBindWxUserView(String accountText);
        void returnMemberInfo(Member member);

    }


    abstract class Presenter extends BasePresenter<ExtrContract.View,ExtrContract.Model>{


        public abstract void getVerfyCode(int type,String mobile);
        public abstract void identifyCode(int type,String mobile,String code);
        public abstract void unBind(long memberId,int type);
        public abstract void bindWxUser(Map<String, String> pm,String wxNickName,String openId);
        public abstract void getMemberInfo(long memberId);

    }



}
