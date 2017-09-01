package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.StatisticsInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.URLs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

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

    private Context mContext;
    private Handler handler;
    private long deviceId;
    private String date;

    public StatisticsAsync(Context context, Handler handler, long deviceId, String date) {
        mContext = context;
        this.handler = handler;
        this.deviceId = deviceId;
        this.date = date;
    }

    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("deviceId", String.valueOf(deviceId)));
        list.add(new BasicNameValuePair("date", date));
        String result = null;
        try {
            result = httpRequest
                    .post(URLs.GET_WORK_STATISTICS, list);
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
            try {
                Gson gson = new Gson();
                BaseBO<StatisticsInfo> bo = gson.fromJson(result,
                        new TypeToken<BaseBO<StatisticsInfo>>() {
                        }.getType());
                msg.what = Constants.HANDLER_GETDATASTATISTICS_SUCCESS;
                msg.obj = bo.getResult();
                handler.sendMessage(msg);
                return;
            } catch (Exception e) {
            }
        } else {
        }
        // 缓存数据第4步
        if (!isHaveCache) {
            msg.what = Constants.HANDLER_GETDATASTATISTICS_FAILD;
            msg.obj = message;
            handler.sendMessage(msg);
        }
    }
}
