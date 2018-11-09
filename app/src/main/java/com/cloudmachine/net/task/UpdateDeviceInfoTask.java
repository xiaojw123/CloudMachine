package com.cloudmachine.net.task;

import android.os.Handler;
import android.os.Message;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.LarkUrls;
import com.cloudmachine.utils.Utils;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xiaojw on 2017/8/9.
 */

public class UpdateDeviceInfoTask extends ATask {

    String deviceId;
    String deviceName;
    Handler handler;

    public UpdateDeviceInfoTask(Handler handler, String deviceId, String deviceName) {
        this.handler = handler;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
    }

    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        try {
            return httpRequest.post(LarkUrls.UPDATE_DEVICEINFO_URL, getMapParams());
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
        super.decodeJson(result);

        Message msg = Message.obtain();
        if (isSuccess) {
            msg.what = Constants.HANDLER_UPDATE_INFO_SUCCESS;
        } else {
            msg.what = Constants.HANDLER_UPDATE_INFO_FAILD;
        }
        handler.sendMessage(msg);
    }

    private Map<String, String> getMapParams() {
        Map<String, String> map = new HashMap<>();
        map.put("deviceId", deviceId);
        map.put("deviceName", deviceName);
        return map;
    }

}
