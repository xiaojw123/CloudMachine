package com.cloudmachine.ui.login.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.google.gson.JsonObject;

import java.util.Map;

import rx.Observable;

/**
 * Created by xiaojw on 2017/10/18.
 */

public interface FindPasswordContract {

    interface View extends BaseView {

    }

    interface Model extends BaseModel {
        Observable<JsonObject> register(Map<String,String> paramsMap);
        Observable<JsonObject> getMobileCode(String mobile, String type);
        Observable<JsonObject> forgetPassword(String mobile, String pwd, String code);
    }

    abstract class Presenter extends BasePresenter<FindPasswordContract.View, FindPasswordContract.Model> {
        public abstract void getMobileCode(String mobile, String type);

        public abstract void register(String mobile, String pwd, String code, String inviteCode);

        public abstract void forgetPassword(String mobile, String pwd, String code);
    }


}
