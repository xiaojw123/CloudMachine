package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.Member;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.URLs;
import com.cloudmachine.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class LoginAsync extends ATask {

    private Handler handler;
    private String mobile;
    private String password;

    public  LoginAsync(String mobile, String password, Context context, Handler handler) {
        this.handler = handler;
        this.mobile = mobile.trim();
        this.password = password.trim();
        //缓存数据第1步
    }


    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("mobile", mobile));
        list.add(new BasicNameValuePair("password", Utils.getPwdStr(password)));
        String result = null;
        try {
            result = httpRequest.post(URLs.LOGIN_URL, list);
            return result;
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
            BaseBO<Member> loginBO = gson.fromJson(result, new TypeToken<BaseBO<Member>>() {
            }.getType());
            msg.what = Constants.HANDLER_LOGIN_SUCCESS;
            msg.obj = loginBO.getResult();
        } else {
            msg.what = Constants.HANDLER_LOGIN_FAIL;
            msg.obj = message;
        }
        handler.sendMessage(msg);
    }


}
