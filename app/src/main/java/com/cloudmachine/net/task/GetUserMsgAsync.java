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

import org.apache.http.NameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/19 上午8:31
 * 修改人：shixionglu
 * 修改时间：2017/3/19 上午8:31
 * 修改备注：
 */

public class GetUserMsgAsync extends ATask {

    private Context context;
    private Handler handler;
    private String access_token;
    private String openid;

    public GetUserMsgAsync(Context context, Handler handler, String access_token, String openid) {
        this.context = context;
        this.handler = handler;
        this.access_token = access_token;
        this.openid = openid;
    }

    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        String result = null;
        try {
            result = httpRequest.post("https://api.weixin.qq.com/sns/userinfo", initListData());
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
        JSONObject jsonObject = null;
        Message msg = Message.obtain();
        try {
            jsonObject = new JSONObject(result);
            String nickname = jsonObject.getString("nickname");
            int sex = Integer.parseInt(jsonObject.get("sex").toString());
            String headimgurl = jsonObject.getString("headimgurl");
            msg.what = Constants.HANDLER_GETUSERMSG_SUCCESS;
            Bundle bundle = new Bundle();
            bundle.putString("nickname", nickname);
            bundle.putString("headimgurl", headimgurl);
            bundle.putInt("sex", sex);
            msg.setData(bundle);
            handler.sendMessage(msg);
        } catch (JSONException e) {
            e.printStackTrace();
            try {
                jsonObject = new JSONObject(result);
                String errcode = jsonObject.getString("errcode").toString().trim();
                String errmsg = jsonObject.getString("errmsg").toString().trim();
                ToastUtils.error("errcode" + errcode + "errmsg" + errmsg, true);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }

    private List<NameValuePair> initListData() {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(Utils.addBasicValue("access_token",access_token));
        list.add(Utils.addBasicValue("openid",openid));
        return list;
    }
}
