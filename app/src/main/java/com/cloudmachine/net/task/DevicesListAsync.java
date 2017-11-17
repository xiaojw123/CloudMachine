package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.McDeviceInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class DevicesListAsync extends ATask {

    private Context context;
    private Handler handler;
    private String type;
    private String key;
    private String memid = "0";

    public DevicesListAsync(int type, String key, Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        this.type = String.valueOf(type);
        this.key = key;
        try {
            memid = String.valueOf(MemeberKeeper.getOauth(context).getId());
        } catch (Exception ee) {

        }
        //缓存数据第1步
        if (type == Constants.MC_DevicesList_AllType && this.key == null) {
            getCacheName(Constants.URL_MyDevices, memid, this.type, this.key);
        }
    }


    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("memberId", memid));
        list.add(new BasicNameValuePair("type", type));
        if (!TextUtils.isEmpty(key)) {
            list.add(new BasicNameValuePair("key", Constants.toUtf(key)));
        }

        String result = null;
        try {
            result = httpRequest.post(Constants.URL_MyDevices, list);
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
            BaseBO<List<McDeviceInfo>> baseBO = gson.fromJson(result, new TypeToken<BaseBO<List<McDeviceInfo>>>() {
            }.getType());
            msg.what = Constants.HANDLER_GETDEVICELIST_SUCCESS;
            msg.obj = baseBO.getResult();
            handler.sendMessage(msg);
        } else {
            msg.what = Constants.HANDLER_GETDEVICELIST_FAIL;
            msg.obj = message;
            handler.sendMessage(msg);
        }
    }


}
