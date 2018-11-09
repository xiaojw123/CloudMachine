package com.cloudmachine.ui.home.contract;

import android.view.View;
import android.widget.FrameLayout;

import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.base.bean.BaseRespose;
import com.google.gson.JsonObject;

import rx.Observable;

/**
 * Created by xiaojw on 2018/7/5.
 */

public interface OperateContact {

    interface Model extends BaseModel {

        Observable<String> getVerifyCode(String taskId);
        Observable<String>  checkVerifyCode(String taskId,String smsCode);
        Observable<JsonObject> authOperator(String servicePwd);

    }

    interface View extends BaseView {
        void returnVerfiyCode();
        void checkVertifyCodeSuccess(String message);
        void returnAuthOperator(String taskId,boolean isAuthed);
    }

    public abstract class Presenter extends BasePresenter<OperateContact.View, OperateContact.Model> {
        public abstract void getVerifyCode(String taskId);
        public abstract void checkVerifyCode(String taskId,String smsCode);

        public abstract void authOperator(String servicePwd, RadiusButtonView button, FrameLayout loadingView);

    }


}
