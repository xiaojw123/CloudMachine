package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.bean.BOInfo;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.CWInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.URLs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class GetWorkDetailAsync extends ATask {

    private Context mContext;
    private Handler handler;
    private String orderNum;
    private String flag;

    public GetWorkDetailAsync(Handler mHandler, Context mContext,
                              String orderNum, String flag) {
        this.mContext = mContext;
        this.handler = mHandler;
        this.orderNum = orderNum;
        this.flag = flag;
        try {
            memberId = String.valueOf(MemeberKeeper.getOauth(mContext).getId());
        } catch (Exception ee) {

        }
    }

    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("orderNum", String.valueOf(orderNum)));
        list.add(new BasicNameValuePair("memberId", String.valueOf(memberId)));
        list.add(new BasicNameValuePair("flag", String.valueOf(flag)));
        String result = null;
        try {
            result = httpRequest
                    .post(URLs.GET_WORK_DETAIL, list);
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
            if (flag.equals("0")) {
                BaseBO<BOInfo> bo = gson.fromJson(result,
                        new TypeToken<BaseBO<BOInfo>>() {
                        }.getType());
                msg.what = Constants.HANDLER_GET_BODETAIL_SUCCESS;
                msg.obj = bo.getResult();
            } else if (flag.equals("1")) {
                BaseBO<CWInfo> bo = gson.fromJson(result,
                        new TypeToken<BaseBO<CWInfo>>() {
                        }.getType());
                msg.what = Constants.HANDLER_GET_CWDETAIL_SUCCESS;
                msg.obj = bo.getResult();
            }

            handler.sendMessage(msg);
        } else {
            msg.what = Constants.HANDLER_GET_WORKDETAIL_FAILD;
            msg.obj = message;
            handler.sendMessage(msg);
        }
    }

}
