package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.helper.UserHelper;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.MemberInfo;
import com.cloudmachine.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class DevicesMemberListAsync extends ATask {

    private Context context;
    private Handler handler;
    private long deviceId;
    private String memid;

    public DevicesMemberListAsync(long deviceId, Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        this.deviceId = deviceId;
        try {
            if (UserHelper.isLogin(context)) {
                memid = String.valueOf(UserHelper.getMemberId(context));
                getCacheName(Constants.URL_MEMBERLIST, String.valueOf(deviceId), memid);
            }
        } catch (Exception ee) {
        }
    }


    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        if (memid != null) {
            list.add(new BasicNameValuePair("memberId", memid));
        }
        list.add(new BasicNameValuePair("deviceId", String.valueOf(deviceId)));
        String result = null;
        try {
            result = httpRequest.post(Constants.URL_MEMBERLIST, list);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        decodeJson(result);
    }


    @Override
    protected void decodeJson(String result) {
        // TODO Auto-generated method stub
        super.decodeJson(result);

        Message msg = Message.obtain();
        if (isSuccess) {
            Gson gson = new Gson();
            BaseBO<List<MemberInfo>> baseBO = gson.fromJson(result, new TypeToken<BaseBO<List<MemberInfo>>>() {
            }.getType());
            msg.what = Constants.HANDLER_GETDEVICEMEMBER_SUCCESS;
            msg.obj = baseBO.getResult();
            handler.sendMessage(msg);
        } else {
            msg.what = Constants.HANDLER_GETDEVICEMEMBER_FAIL;
            msg.obj = message;
            handler.sendMessage(msg);
        }
    }


}
