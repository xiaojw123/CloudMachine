package com.cloudmachine.ui.home.contract;

import com.cloudmachine.base.BaseModel;
import com.cloudmachine.base.BasePresenter;
import com.cloudmachine.base.BaseView;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.helper.QiniuManager;
import com.google.gson.JsonObject;

import java.io.File;

import retrofit2.http.Field;
import rx.Observable;

/**
 * Created by xiaojw on 2018/7/10.
 */

public interface AuthPersonalInfoContract {

    interface Model extends BaseModel {
        Observable<JsonObject> getMemberAuthInfo();
        Observable<String> submitIdUserInfo(String redisUserId);
        Observable<JsonObject> verifyOcr(String imgUrl,String redisUserId);
    }
    interface View extends BaseView {
        void updateMemberAuthInfo(String realName,String idCardNo);
        void returnVerifyOrcSuccess(String redisUserId,String realName,String idCardNo);
        void retrunVerifyOrcFailed();
        void uploadFileSuccess(String imgUrl);
        void submitSuccess();
    }

    public abstract class Presenter extends BasePresenter<AuthPersonalInfoContract.View, AuthPersonalInfoContract.Model> {
        public abstract void getMemberAuthInfo();
        public abstract void submitIdUserInfo(String redisUserId);
        public abstract void verifyOcr(String imgUrl,String redisUserId);
        public abstract void uploadFile(File file, QiniuManager.OnUploadListener listener);



    }

}
