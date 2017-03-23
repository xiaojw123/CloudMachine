package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.struc.BaseBO;
import com.cloudmachine.utils.Constants;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/2/27 上午12:51
 * 修改人：shixionglu
 * 修改时间：2017/2/27 上午12:51
 * 修改备注：
 */

public class CheckPayAsync extends ATask {

    private Context context;
    private Handler handler;

    public CheckPayAsync(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        String result = null;
        try {
            result = httpRequest.post(Constants.URL_CHECK_PAY, list);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    protected void onPostExecute(String result) {
        //缓存数据第2步
        super.onPostExecute(result);
        decodeJson(result);
    }

    @Override
    protected void decodeJson(String result) {
        // TODO Auto-generated method stub
        //缓存数据第3步
        super.decodeJson(result);
        Message msg = Message.obtain();
        if (isSuccess) {
            try{
                Gson gson = new Gson();
                BaseBO updateResult = gson.fromJson(result, BaseBO.class);
                msg.what = Constants.HANDLER_CHECKPAY_SUCCESS;
                msg.obj = updateResult.getMessage();
                handler.sendMessage(msg);
                return;
            }catch(Exception e){
            }
        } else {
        }
        //缓存数据第4步
        if(!isHaveCache){
            msg.what = Constants.HANDLER_CHECKPAY_FAIDL;
            msg.obj = message;
            handler.sendMessage(msg);
        }

    }
}
