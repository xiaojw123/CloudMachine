package com.cloudmachine.net.task;

import android.os.Handler;
import android.os.Message;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.ui.home.model.EvaluateInfoBean;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.URLs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojw on 2017/8/11.
 */

public class GetEvaluateInfoTask extends ATask {

    private Handler handler;
    private String orderNum;

    public GetEvaluateInfoTask(Handler mHandler, String orderNum) {
        this.handler = mHandler;
        this.orderNum = orderNum;

    }

    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("css_work_no", orderNum));
        try {
            return httpRequest.post(URLs.GET_EVALUATE_INFO, list);
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
            BaseBO<EvaluateInfoBean> bo = gson.fromJson(result,
                    new TypeToken<BaseBO<EvaluateInfoBean>>() {
                    }.getType());
            msg.what = Constants.HANDLER_GET_EVALUATE_INFO_SUCCESS;
            msg.obj = bo.getResult();
            handler.sendMessage(msg);
        } else {
            msg.what = Constants.HANDLER_GET_EVALUATE_INFO_FAILED;
            msg.obj = message;
            handler.sendMessage(msg);
        }

    }
}
