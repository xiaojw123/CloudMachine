package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.amap.api.navi.view.PoiInputResItemWidget;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.MachineBrandInfo;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class MachineBrandListAsync extends ATask {

    private Handler handler;


    public MachineBrandListAsync(Handler handler) {
        this.handler = handler;
        getCacheName(Constants.URL_GETMACHINEBRAND);
    }


    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        if (params != null && params.length >= 2) {
            String memberId = params[0];
            String typeId = params[1];
            if (!TextUtils.isEmpty(memberId)) {
                list.add(new BasicNameValuePair("memberId", memberId));
            }
            if (!TextUtils.isEmpty(typeId)) {
                list.add(new BasicNameValuePair("typeId", typeId));
            }
        }
        String result = null;
        try {
            result = httpRequest.get(Constants.URL_GETMACHINEBRAND, list);
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
            BaseBO<List<MachineBrandInfo>> baseBO = gson.fromJson(result, new TypeToken<BaseBO<List<MachineBrandInfo>>>() {
            }.getType());
            msg.what = Constants.HANDLER_GETMACHINEBRAND_SUCCESS;
            msg.obj = baseBO.getResult();
        } else {
            msg.what = Constants.HANDLER_GETMACHINEBRAND_FAIL;
            msg.obj = message;
        }
        handler.sendMessage(msg);
    }


}
