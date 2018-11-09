package com.cloudmachine.ui.home.presenter;

import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.autonavi.rtbt.IFrameForRTBT;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.base.baserx.RxSchedulers;
import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.ui.home.contract.OperateContact;
import com.cloudmachine.utils.CommonUtils;
import com.cloudmachine.utils.ToastUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Created by xiaojw on 2018/7/5.
 */

public class OperatePresenter extends OperateContact.Presenter {
    @Override
    public void getVerifyCode(String taskId) {
        mRxManage.add(mModel.getVerifyCode(taskId).subscribe(new RxSubscriber<String>(mContext) {
            @Override
            protected void _onNext(String s) {
                ToastUtils.showToast(mContext, "验证码已发送");
//                mView.returnVerfiyCode();

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext, message);

            }
        }));
    }

    @Override
    public void checkVerifyCode( String taskId, String smsCode) {
        mRxManage.add(mModel.checkVerifyCode( taskId, smsCode).subscribe(new RxSubscriber<String>(mContext) {
            @Override
            protected void _onNext(String s) {
                mView.checkVertifyCodeSuccess(s);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext, message);
            }
        }));

    }

    @Override
    public void authOperator(String servicePwd, final RadiusButtonView button, final FrameLayout loadingView) {
        button.setButtonClickEnable(false);
        loadingView.setVisibility(View.VISIBLE);
        mRxManage.add(mModel.authOperator(servicePwd).compose(RxSchedulers.<JsonObject>io_main()).subscribe(new RxSubscriber<JsonObject>(mContext) {
            @Override
            protected void _onNext(JsonObject br) {
                if (CommonUtils.isActivityDestoryed(mContext)) {
                    return;
                }
                button.setButtonClickEnable(true);
                loadingView.setVisibility(View.GONE);
                if (br != null && !br.isJsonNull()) {
                    JsonElement successJE = br.get("success");
                    if (successJE != null && !successJE.isJsonNull()) {
                        boolean success = successJE.getAsBoolean();
                        if (success) {
                            JsonElement resultJE = br.get("result");
                            if (resultJE != null && !resultJE.isJsonNull()) {
                                JsonObject resultJson = resultJE.getAsJsonObject();
                                if (resultJson != null && !resultJson.isJsonNull()) {
                                    JsonElement j1 = resultJson.get("taskId");
                                    JsonElement j2 = resultJson.get("authStatus");
                                    String taskId = null;
                                    int authStatus = 0;
                                    if (j1 != null && !j1.isJsonNull()) {
                                        taskId = j1.getAsString();
                                    }
                                    if (j2 != null && !j2.isJsonNull()) {
                                        authStatus = j2.getAsInt();
                                    }
                                    mView.returnAuthOperator(taskId, authStatus == 1);
                                }
                            }

                        } else {
                            JsonElement messageJE = br.get("message");
                            if (messageJE != null && !messageJE.isJsonNull()) {
                                String message = messageJE.getAsString();
                                ToastUtils.showToast(mContext, message);
                            }
                        }
                    } else {
                        ToastUtils.showToast(mContext, "未知错误");
                    }
                }
            }

            @Override
            protected void _onError(String message) {
                if (CommonUtils.isActivityDestoryed(mContext)) {
                    return;
                }
                button.setButtonClickEnable(true);
                loadingView.setVisibility(View.GONE);
                ToastUtils.showToast(mContext, message);

            }
        }));

    }
}
