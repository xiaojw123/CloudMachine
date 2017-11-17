package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.McDeviceBasicsInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class DevicesBasicsInfoAsync extends ATask {

    private Context context;
    private Handler handler;
    private int type;
    private long deviceId;

    public DevicesBasicsInfoAsync(long deviceId, Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        this.deviceId = deviceId;
        try {
            memberId = String.valueOf(MemeberKeeper.getOauth(context).getId());
        } catch (Exception ee) {

        }
        //getCacheName(Constants.URL_Devices,String.valueOf(deviceId));
    }

    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("deviceId", String.valueOf(deviceId)));
        list.add(new BasicNameValuePair("memberId", memberId));
        String result = null;
        try {
            result = httpRequest.post(Constants.URL_Devices, list);
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
            BaseBO<McDeviceBasicsInfo> baseBO = gson.fromJson(result, new TypeToken<BaseBO<McDeviceBasicsInfo>>() {
            }.getType());
            msg.what = Constants.HANDLER_GETDEVICEBASICSINFO_SUCCESS;
            msg.obj = baseBO.getResult();
            handler.sendMessage(msg);
        } else {
            msg.what = Constants.HANDLER_GETDEVICEINFO_FAIL;
            msg.obj = message;
            handler.sendMessage(msg);
        }
    }


}
