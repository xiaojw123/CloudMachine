package com.cloudmachine.net.task;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.cloudmachine.MyApplication;
import com.cloudmachine.R;
import com.cloudmachine.base.bean.BaseRespose;
import com.cloudmachine.bean.LarkMemberInfo;
import com.cloudmachine.bean.MemberInfo;
import com.cloudmachine.bean.SearchMemberItem;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.ui.login.acticity.LoginActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.LarkUrls;
import com.cloudmachine.utils.UIHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchMemberAsync extends ATask {

    private Context context;
    private Handler handler;
    private int type;

    public SearchMemberAsync(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    protected void onPreExecute() {
        if (!MyApplication.getInstance().isOpenNetwork(context)) {
            UIHelper.ToastMessage(context, context.getResources().getString(R.string.no_network));
            return;
        }
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("mobile", params[0]);
        String result = null;
        try {
            result = httpRequest.get(LarkUrls.MEMBER_SEARCH_URL, paramMap);
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
            JSONObject json = null;
            try {
                json = new JSONObject(result);
                String gsonStr = json.getString(Constants.RESULT);
                Gson gson = new Gson();
                SearchMemberItem mcMemberInfo = gson.fromJson(gsonStr,
                        new TypeToken<SearchMemberItem>() {
                        }.getType());
                msg.what = Constants.HANDLER_SEARCHMEMBER_SUCCESS;
                msg.obj = mcMemberInfo;
                handler.sendMessage(msg);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            msg.what = Constants.HANDLER_SEARCHMEMBER_FAIL;
            msg.obj = message;
            handler.sendMessage(msg);
        }
    }

}
