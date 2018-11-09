package com.cloudmachine.ui.login.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.bean.LarkMemberInfo;
import com.cloudmachine.bean.Member;
import com.google.gson.JsonObject;

import rx.Observable;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/27 下午1:33
 * 修改人：shixionglu
 * 修改时间：2017/3/27 下午1:33
 * 修改备注：
 */

public interface LoginContract {

    interface Model extends BaseModel {
       Observable<JsonObject>  login(String userName,String password);
       Observable<LarkMemberInfo> getMemberInfo();
       Observable<JsonObject> loginByWx(String unionId,String openId,String nickName,String headLogo);
    }

    interface View extends BaseView {
        void loginSuccess(Member member);
        void setNameShakeAnimation();
    }

     abstract  class Presenter extends BasePresenter<LoginContract.View,LoginContract.Model> {
         public abstract void login(String userName,String password);
         public abstract void getMemberInfo();
         public abstract void loginByWx(String unionId,String openId,String nickName,String headLogo,int sex);


    }
}
