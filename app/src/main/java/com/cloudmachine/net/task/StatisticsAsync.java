package com.cloudmachine.net.task;

import android.os.Handler;
import android.os.Message;

import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.StatisticsInfo;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.LarkUrls;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2016/12/6 下午4:44
 * 修改人：shixionglu
 * 修改时间：2016/12/6 下午4:44
 * 修改备注：
 */

public class StatisticsAsync extends ATask {

    private Handler handler;
    private long deviceId;
    private String date;

    public StatisticsAsync(Handler handler, long deviceId, String date) {
        this.handler = handler;
        this.deviceId = deviceId;
        this.date = date;
    }

    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        String result = null;
        try {
            Map<String,String> pm=new HashMap<>();
            pm.put("deviceId", String.valueOf(deviceId));
            pm.put("date", date);
            result = httpRequest
                    .get(LarkUrls.GET_MOTH_DATA, pm);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        // 缓存数据第2步
        super.onPostExecute(result);
        decodeJson(result);
    }

    @Override
    protected void decodeJson(String result) {
        // TODO Auto-generated method stub
        // 缓存数据第3步
        super.decodeJson(result);
        Message msg = Message.obtain();
        if (isSuccess) {
                Gson gson = new Gson();
                BaseBO<StatisticsInfo> bo = gson.fromJson(result,
                        new TypeToken<BaseBO<StatisticsInfo>>() {
                        }.getType());
                msg.what = Constants.HANDLER_GETDATASTATISTICS_SUCCESS;
                msg.obj = bo.getResult();
                handler.sendMessage(msg);
        }else{
            msg.what = Constants.HANDLER_GETDATASTATISTICS_FAILD;
            msg.obj = message;
            handler.sendMessage(msg);
        }
    }
}
