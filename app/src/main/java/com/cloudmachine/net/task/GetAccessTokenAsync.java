package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.Utils;
import com.github.mikephil.charting.utils.AppLog;

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/19 上午6:03
 * 修改人：shixionglu
 * 修改时间：2017/3/19 上午6:03
 * 修改备注：
 */

public class GetAccessTokenAsync extends ATask {

    private Context context;
    private Handler handler;
    private String appid;
    private String secret;
    private String code;
    private String grant_type;

    public GetAccessTokenAsync(Context context, Handler handler, String appid, String secret, String code, String grant_type) {
        this.context = context;
        this.handler = handler;
        this.appid = appid;
        this.secret = secret;
        this.code = code;
        this.grant_type = grant_type;
    }

    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        String result = null;
        try {
            result = httpRequest.post("https://api.weixin.qq.com/sns/oauth2/access_token", initListData());

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
        AppLog.print("decodeJson result___"+result);
        super.decodeJson(result);
        JSONObject jsonObject = null;
        Message msg = Message.obtain();
        try {
            jsonObject = new JSONObject(result);
            String openid = jsonObject.getString("openid").toString().trim();
            String access_token = jsonObject.getString("access_token").toString().trim();
            msg.what = Constants.HANDLER_GETACCESSTOKEN_SUCCESS;
            Bundle bundle = new Bundle();
            bundle.putString("openid",openid);
            bundle.putString("access_token", access_token);
            msg.setData(bundle);
            handler.sendMessage(msg);
        } catch (JSONException e) {
            e.printStackTrace();
            try {
                AppLog.print("json error___");
                jsonObject = new JSONObject(result);
                String errcode = jsonObject.getString("errcode").toString().trim();
                String errmsg = jsonObject.getString("errmsg").toString().trim();
                ToastUtils.error("errcode"+errcode+"errmsg"+errmsg,true);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }



       /* Message msg = Message.obtain();
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
        }*/
    }

    private List<NameValuePair> initListData() {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(Utils.addBasicValue("appid",appid));
        list.add(Utils.addBasicValue("secret",secret));
        list.add(Utils.addBasicValue("code",code));
        list.add(Utils.addBasicValue("grant_type",grant_type));
        return list;
    }
}
