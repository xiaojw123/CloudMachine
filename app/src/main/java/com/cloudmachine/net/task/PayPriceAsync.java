package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.PayPriceInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/2/24 下午3:36
 * 修改人：shixionglu
 * 修改时间：2017/2/24 下午3:36
 * 修改备注：
 */

public class PayPriceAsync extends ATask {

    private Context context;
    private Handler handler;
    private String orderNum;
    private String type;
    private String userCouponId;

    public PayPriceAsync(Context context, Handler handler, String orderNum, String type, String userCouponId) {
        this.context = context;
        this.handler = handler;
        this.orderNum = orderNum;
        this.type = type;
        this.userCouponId = userCouponId;

        try{
            memberId = String.valueOf(MemeberKeeper.getOauth(context).getId());
        }catch(Exception e){
            Constants.ToastAction(e.toString());
        }
    }

    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        String result = null;
        try {
            result = httpRequest.post(Constants.URL_GETPAYPRICE, initListData());
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
            try{

                Gson gson = new Gson();
                BaseBO<PayPriceInfo> bo = gson.fromJson(result, new TypeToken<BaseBO<PayPriceInfo>>(){}.getType());
                msg.what = Constants.HANDLER_GETPAYPRICE_SUCCESS;
                msg.obj = bo.getResult();
                handler.sendMessage(msg);
                return;
            }catch(Exception e){
            }
        }
        //缓存数据第4步
        if(!isHaveCache){
            msg.what = Constants.HANDLER_GETYUNBOXPAY_FAILD;
            msg.obj = message;
            handler.sendMessage(msg);
        }
    }



    private List<NameValuePair> initListData() {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(Utils.addBasicValue("memberId", memberId));
        if (null != orderNum) {
            list.add(Utils.addBasicValue("orderNum",orderNum));
        }
        list.add(Utils.addBasicValue("type",type));
        if (!userCouponId.equals("-1")) {
            list.add(Utils.addBasicValue("userCouponId",userCouponId));
        }
        return list;
    }
}
