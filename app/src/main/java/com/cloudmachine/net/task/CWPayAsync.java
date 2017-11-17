package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.activities.RepairPayDetailsActivity;
import com.cloudmachine.bean.AliPayBean;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.WeiXinEntityBean;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.Utils;
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
 * 创建时间：2017/2/26 下午11:33
 * 修改人：shixionglu
 * 修改时间：2017/2/26 下午11:33
 * 修改备注：
 */

public class CWPayAsync extends ATask {

    private Handler handler;
    private String payType;
    private String orderNum;
    private String userCouponId;

    public CWPayAsync(Handler handler, Context context, String payType, String orderNum, String userCouponId) {
        this.handler = handler;
        this.payType = payType;
        this.orderNum = orderNum;
        this.userCouponId = userCouponId;
        try {
            memberId = String.valueOf(MemeberKeeper.getOauth(context).getId());
        } catch (Exception e) {
            Constants.ToastAction(e.toString());
        }
    }

    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        String result = null;
        try {
            result = httpRequest.post(Constants.URL_CWPAY, initListData());
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
            if (payType.equals(RepairPayDetailsActivity.PAY_TYPE_ALIPAY)) {
                Gson gson = new Gson();
                BaseBO<AliPayBean> bo = gson.fromJson(result, new TypeToken<BaseBO<AliPayBean>>() {
                }.getType());
                msg.what = Constants.HANDLER_GETCWPAY_SUCCESS;
                msg.obj = bo.getResult();
                handler.sendMessage(msg);
            } else if (payType.equals(RepairPayDetailsActivity.PAY_TYPE_WECHAT)) {
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


    private List<NameValuePair> initListData() {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(Utils.addBasicValue("memberId", memberId));
        list.add(Utils.addBasicValue("payType", payType));
        list.add(Utils.addBasicValue("orderNum", orderNum));
        list.add(new BasicNameValuePair("coupons", userCouponId));
        return list;
    }
}
