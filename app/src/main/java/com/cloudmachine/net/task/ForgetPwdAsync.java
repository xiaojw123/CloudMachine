package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.LarkUrls;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class ForgetPwdAsync extends ATask {

    private Handler handler;
    private String mobile;
    private String pwd;
    private String code;


    public ForgetPwdAsync(String mobile, String pwd, String code, Context context, Handler handler) {
        this.handler = handler;
        this.mobile = mobile;
        this.pwd = pwd;
        this.code = code;
    }


    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        try {
            Map<String,String> pm=new HashMap<>();
            pm.put("mobile", mobile);
            pm.put("pwd", pwd);
            pm.put("code", code);
            return httpRequest.post(LarkUrls.FORGET_PWD, pm);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        //缓存数据第2步
        super.onPostExecute(result);
        decodeJson(result);
    }


    @Override
    protected void decodeJson(String result) {
        // TODO Auto-generated method stub
        //缓存数据第3步
        super.decodeJson(result);
        Message msg = Message.obtain();
        if (isSuccess) {
            Gson gson = new Gson();
            BaseBO updateResult = gson.fromJson(result,
                    BaseBO.class);
            msg.what = Constants.HANDLER_FORGETPWD_SUCCESS;
            msg.obj = updateResult.getMessage();
            handler.sendMessage(msg);
        } else {
            msg.what = Constants.HANDLER_FORGETPWD_FAIL;
            msg.obj = message;
            handler.sendMessage(msg);
        }

    }


}
