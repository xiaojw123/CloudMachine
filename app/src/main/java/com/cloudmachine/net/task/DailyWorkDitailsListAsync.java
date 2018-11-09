package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.MyApplication;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.DailyWorkInfo;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.LarkUrls;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.UIHelper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyWorkDitailsListAsync extends ATask {

    private Context context;
    private Handler handler;
    private int type;
    private long deviceId;
    private String date;

    public DailyWorkDitailsListAsync(long deviceId, String date, Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
        this.deviceId = deviceId;
        this.date = date;
    }

    @Override
    protected void onPreExecute() {
        if (!MyApplication.getInstance().isOpenNetwork(context)) {
            UIHelper.ToastMessage(context, "网络连接失败,请检查您的网络设置");
            return;
        }
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        String result = null;
        try {
            Map<String, String> pm = new HashMap<>();
            pm.put("deviceId", String.valueOf(deviceId));
            pm.put("date", date);
            result = httpRequest.post(LarkUrls.DAILY_WORK_DETAIL, pm);
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
            try {
                JSONObject resposeJobj = new JSONObject(result);
                JSONObject resultJobj = resposeJobj.optJSONObject(Constants.RESULT);
                JSONArray detailArray = resultJobj.optJSONArray("workDetailList");
                List<DailyWorkInfo> infoList = gson.fromJson(detailArray.toString(), new TypeToken<List<DailyWorkInfo>>() {
                }.getType());
                msg.what = Constants.HANDLER_DAILYWORK_SUCCESS;
                msg.obj = infoList;
                handler.sendMessage(msg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            msg.what = Constants.HANDLER_DAILYWORK_FAIL;
            msg.obj = message;
            handler.sendMessage(msg);
        }
    }

}
