package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.CommentsInfo;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.LarkUrls;
import com.cloudmachine.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

public class GetTagInfoAsync extends ATask {

    private Handler handler;
    private Context mContext;

    public GetTagInfoAsync(Handler mHandler, Context context) {
        this.handler = mHandler;
        mContext=context;

    }

    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        String result = null;
        try {
            result = httpRequest.get(LarkUrls.GET_EVALUATE_URL, null);
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
        }else{
            ToastUtils.showToast(mContext,message);
        }
    }

}
