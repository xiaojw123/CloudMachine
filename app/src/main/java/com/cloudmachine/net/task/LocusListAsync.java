package com.cloudmachine.net.task;

import android.os.Handler;
import android.os.Message;

import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.LocationXY;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.LarkUrls;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocusListAsync extends ATask {

    private Handler handler;
    private long deviceId;
    private long startTime;
    private long endTime;

    public LocusListAsync(long deviceId, long startTime, long endTime, Handler handler) {
        this.handler = handler;
        this.deviceId = deviceId;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public LocusListAsync(long deviceId,Handler handler) {
        this.handler = handler;
        this.deviceId = deviceId;
        //缓存数据第1步
    }

    @Override
    protected String doInBackground(String... params) {

        IHttp httpRequest = new HttpURLConnectionImp();
        String result = null;
        try {
            Map<String, String> paramsMap = new HashMap<>();
            paramsMap.put("deviceId", String.valueOf(deviceId));
            SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmm");
            if (startTime != 0) {
                paramsMap.put("startTime", sdf.format(startTime));
            }
            if (endTime != 0) {
                paramsMap.put("endTime", sdf.format(endTime));
            }
            result = httpRequest.get(LarkUrls.LOCUS, paramsMap);
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
        super.decodeJson(result);
        Message msg = Message.obtain();
        if (isSuccess) {
            Gson gson = new Gson();
            BaseBO<List<LocationXY>> baseBO = gson.fromJson(result, new TypeToken<BaseBO<List<LocationXY>>>() {
            }.getType());
            msg.what = Constants.HANDLER_LOCUS_SUCCESS;
            msg.obj = baseBO.getResult();
        } else {
            msg.what = Constants.HANDLER_LOCUS_FAIL;
            msg.obj = message;
        }
        handler.sendMessage(msg);
    }


}
