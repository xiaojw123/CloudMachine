package com.cloudmachine.net.task;

import android.os.Handler;
import android.os.Message;

import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.CommentsInfo;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.URLs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

public class GetTagInfoAsync extends ATask {

    private Handler handler;
    private boolean isAlience;

    public GetTagInfoAsync(Handler mHandler,boolean isAlience) {
        this.handler = mHandler;
        this.isAlience=isAlience;

    }

    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        String result = null;
        try {
            String url=isAlience?URLs.GET_TAG_INFO_ALLIANCE:URLs.GET_TAG_INFO;
            result = httpRequest.post(url, list);
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
            BaseBO<CommentsInfo> bo = gson.fromJson(result,
                    new TypeToken<BaseBO<CommentsInfo>>() {
                    }.getType());
            msg.what = Constants.HANDLER_GETTAGINFO_SUCCESS;
            msg.obj = bo.getResult();
            handler.sendMessage(msg);
        } else {
            msg.what = Constants.HANDLER_GETTAGINFO_FAILD;
            msg.obj = message;
            handler.sendMessage(msg);
        }
    }

}
