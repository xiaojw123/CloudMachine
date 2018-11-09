package com.cloudmachine.net.task;

import android.os.Handler;
import android.os.Message;

import com.cloudmachine.bean.AliPayBean;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.WeiXinEntityBean;
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
 * 创建时间：2017/2/26 下午11:33
 * 修改人：shixionglu
 * 修改时间：2017/2/26 下午11:33
 * 修改备注：
 */

public class CWPayAsync extends ATask {

    private Handler handler;
    private int payType;
    private String orderNum;

    public CWPayAsync(Handler handler, int payType, String orderNum) {
        this.handler = handler;
        this.payType = payType;
        this.orderNum = orderNum;
    }

    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        String result = null;
        try {
            result = httpRequest.post(LarkUrls.GET_PAY_SIGN, getParamsMap());
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
            if (payType==Constants.PAY_TYPE_ALIPAY) {
                Gson gson = new Gson();
                BaseBO<AliPayBean> bo = gson.fromJson(result, new TypeToken<BaseBO<AliPayBean>>() {
                }.getType());
                msg.what = Constants.HANDLER_GETCWPAY_SUCCESS;
                msg.obj = bo.getResult();
                handler.sendMessage(msg);
            } else if (payType==Constants.PAY_TYPE_WX) {
                Gson gson = new Gson();
                BaseBO<WeiXinEntityBean> bo = gson.fromJson(result, new TypeToken<BaseBO<WeiXinEntityBean>>() {
                }.getType());
                msg.what = Constants.HANDLER_GETCWPAY_SUCCESS;
                msg.obj = bo.getResult();
                handler.sendMessage(msg);
            }
        } else {
            msg.what = Constants.HANDLER_GETCWPAY_FAILD;
            msg.obj = message;
            handler.sendMessage(msg);
        }
    }



    private Map<String,String> getParamsMap(){
        Map<String,String> map=new HashMap<>();
        map.put("payType",String.valueOf(payType));
        map.put("orderNo",orderNum);
        return map;
    }
}
