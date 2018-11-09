package com.cloudmachine.net.task;

import android.os.Handler;
import android.os.Message;

import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.ScanningWTInfo;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.LarkUrls;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetWorkTimeListAsync extends ATask {

    private Handler handler;
    private long deviceId;

    public GetWorkTimeListAsync(long deviceId, Handler handler) {
        this.handler = handler;
        this.deviceId = deviceId;
        //缓存数据第1步
    }


    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        Map<String,String> pm=new HashMap<>();
        pm.put("deviceId",String.valueOf(deviceId));
        String result = null;
        try {
            result = httpRequest.post(LarkUrls.GET_DAY_DATA, pm);
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
        super.decodeJson(result);
        Message msg = Message.obtain();
        if (isSuccess) {
            Gson gson = new Gson();
            BaseBO<List<ScanningWTInfo>> baseBO = gson.fromJson(result, new TypeToken<BaseBO<List<ScanningWTInfo>>>() {
            }.getType());
            msg.what = Constants.HANDLE_GETWORKTIMELIST_SUCCESS;
            msg.obj = baseBO.getResult();
            handler.sendMessage(msg);
        } else {
            msg.what = Constants.HANDLE_GETWORKTIMELIST_FAILD;
            msg.obj = message;
            handler.sendMessage(msg);
        }
    }


}
