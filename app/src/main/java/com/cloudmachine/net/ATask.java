package com.cloudmachine.net;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.cloudmachine.MyApplication;
import com.cloudmachine.R;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.ui.login.acticity.LoginActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.UIHelper;
import com.google.gson.Gson;

public class ATask extends AsyncTask<String, Integer, String> {
    public boolean isSuccess;
    public String message = "系统错误，请稍后重试！";
    public String memberId = "0";
    protected BaseRespose br;

    /**
     * 接口说明：
     * 返回code：
     * -1：失败
     * 0：列表返回成功
     * 1：请求返回成功
     **/
    @Override
    protected void onPreExecute() {
        if (!MyApplication.getInstance().isOpenNetwork(MyApplication.mContext)) {
            UIHelper.ToastMessage(MyApplication.mContext,
                    MyApplication.mContext.getResources().getString(R.string.no_network));
            return;
        }
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        return null;
    }


    protected void decodeJson(String result) {
        Gson gson = new Gson();
        br = gson.fromJson(result, BaseRespose.class);
        if (br != null) {
            message = br.getMessage();
            isSuccess = br.isSuccess();
            if (!isSuccess && br.getCode() == -10086) {
                Intent intent = new Intent(MyApplication.mContext, LoginActivity.class);
                MyApplication.mContext.startActivity(intent);
            }
        }
    }




}
