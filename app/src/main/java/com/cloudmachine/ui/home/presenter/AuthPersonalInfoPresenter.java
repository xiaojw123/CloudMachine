package com.cloudmachine.ui.home.presenter;

import android.app.Activity;
import android.text.TextUtils;

import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.camera.CameraActivity;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.helper.QiniuManager;
import com.cloudmachine.ui.home.activity.AuthPersonalInfoActivity;
import com.cloudmachine.ui.home.contract.AuthPersonalInfoContract;
import com.cloudmachine.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;

import org.json.JSONObject;

import java.io.File;

import id.zelory.compressor.Compressor;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by xiaojw on 2018/7/10.
 */
public class AuthPersonalInfoPresenter extends AuthPersonalInfoContract.Presenter {


    @Override
    public void getMemberAuthInfo(long memberId) {
        mRxManage.add(mModel.getMemberAuthInfo(memberId).subscribe(new RxSubscriber<JsonObject>(mContext) {
            @Override
            protected void _onNext(JsonObject jsonObject) {
                if (jsonObject != null && !jsonObject.isJsonNull()) {
                    JsonElement j1 = jsonObject.get("realName");
                    JsonElement j2 = jsonObject.get("idCardNo");
                    if (j1 != null && !j1.isJsonNull() && j2 != null && !j2.isJsonNull()) {
                        mView.updateMemberAuthInfo(j1.getAsString(), j2.getAsString());
                    }
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext, message);
            }
        }));

    }

    @Override
    public void submitIdUserInfo(long memberId, String redisUserId) {
        mRxManage.add(mModel.submitIdUserInfo(memberId, redisUserId).subscribe(new RxSubscriber<String>(mContext) {
            @Override
            protected void _onNext(String s) {
                mView.submitSuccess();
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext, message);
            }
        }));

    }

    @Override
    public void verifyOcr(final long memberId, String imgUrl, String redisUserId) {
        AppLog.print("memberId___" + memberId + " ,imgUrl___" + imgUrl);
        mRxManage.add(mModel.verifyOcr(memberId, imgUrl, redisUserId).compose(RxSchedulers.<JsonObject>io_main()).subscribe(new RxSubscriber<JsonObject>(mContext) {
            @Override
            protected void _onNext(JsonObject resposeJson) {
                if (resposeJson != null) {
                    JsonElement messageJe = resposeJson.get("message");
                    JsonElement resutJe = resposeJson.get("result");
                    JsonElement successJe = resposeJson.get("success");
                    boolean isSuccess = successJe.getAsBoolean();
                    String errorMesage = "未知异常";
                    if (messageJe != null && !messageJe.isJsonNull()) {
                        errorMesage = messageJe.getAsString();
                    }
                    if (isSuccess) {
                        if (resutJe != null && !resutJe.isJsonNull()) {
                            JsonObject jobj = resutJe.getAsJsonObject();
                            JsonElement j1 = jobj.get("redisUserId");
                            String redisUserId = null;
                            String realName = null;
                            String idCardNo = null;
                            if (!j1.isJsonNull()) {
                                redisUserId = j1.getAsString();
                            }
                            JsonElement j2 = jobj.get("realName");
                            if (!j2.isJsonNull()) {
                                realName = j2.getAsString();
                            }
                            JsonElement j3 = jobj.get("idCardNo");
                            if (!j3.isJsonNull()) {
                                idCardNo = j3.getAsString();
                            }
                            mView.returnVerifyOrcSuccess(redisUserId, realName, idCardNo);
                        } else {
                            _onError(errorMesage);
                        }
                    } else {
                        _onError(errorMesage);
                    }
                }
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext, message);
                mView.retrunVerifyOrcFailed();
            }
        }));
    }

    @Override
    public void uploadFile(File file) {

        Compressor.getDefault(mContext)
                .compressToFileAsObservable(file)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        QiniuManager.getUploadManager().put(file, "img_id_card/" + file.getName(), QiniuManager.uptoken, new UpCompletionHandler() {
                            @Override
                            public void complete(String key, ResponseInfo info, final JSONObject response) {
                                mView.uploadFileSuccess(QiniuManager.origin + key);
                            }
                        }, null);
                    }
                });


    }
}
