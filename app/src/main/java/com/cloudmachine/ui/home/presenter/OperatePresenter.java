package com.cloudmachine.ui.home.presenter;

import android.widget.Toast;

import com.cloudmachine.base.baserx.RxSubscriber;
import com.cloudmachine.chart.utils.AppLog;
import com.cloudmachine.ui.home.contract.OperateContact;
import com.cloudmachine.utils.ToastUtils;
import com.google.gson.JsonObject;

/**
 * Created by xiaojw on 2018/7/5.
 */

public class OperatePresenter extends OperateContact.Presenter {
    @Override
    public void getVerifyCode(long memberId, String taskId) {
        mRxManage.add(mModel.getVerifyCode(memberId,taskId).subscribe(new RxSubscriber<String>(mContext) {
            @Override
            protected void _onNext(String s) {
                mView.returnVerfiyCode();

            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext,message);

            }
        }));
    }

    @Override
    public void checkVerifyCode(long memberId, String taskId, String smsCode) {
        mRxManage.add(mModel.checkVerifyCode(memberId,taskId,smsCode).subscribe(new RxSubscriber<String>(mContext) {
            @Override
            protected void _onNext(String s) {
                mView.checkVertifyCodeSuccess(s);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext,message);
            }
        }));

    }

    @Override
    public void authOperator(long memberId, String servicePwd) {
        mRxManage.add(mModel.authOperator(memberId,servicePwd).subscribe(new RxSubscriber<JsonObject>(mContext) {
            @Override
            protected void _onNext(JsonObject jsonObject) {
                AppLog.print("authResult___result:"+jsonObject.toString());
                String taskId=jsonObject.get("taskId").getAsString();
                int authStatus=jsonObject.get("authStatus").getAsInt();
                mView.returnAuthOperator(taskId,authStatus==1);
            }

            @Override
            protected void _onError(String message) {
                ToastUtils.showToast(mContext,message);

            }
        }));

    }
}
