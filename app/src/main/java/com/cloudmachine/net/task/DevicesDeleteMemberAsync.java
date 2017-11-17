package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.MyApplication;
import com.cloudmachine.R;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.UIHelper;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class DevicesDeleteMemberAsync extends ATask {

    private Context context;
    private Handler handler;
    private int type;

    public DevicesDeleteMemberAsync(Context context, Handler handler) {
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
        String memid = params[0];
        String deviceId = params[1];
        IHttp httpRequest = new HttpURLConnectionImp();
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        String creator = "";
        try {
            creator = String.valueOf(MemeberKeeper
                    .getOauth(context).getId());
        } catch (Exception ee) {

        }
        list.add(new BasicNameValuePair("creator", creator));
        list.add(new BasicNameValuePair("memberId", memid));
        list.add(new BasicNameValuePair("deviceId", deviceId));
        String result = null;
        try {
            result = httpRequest.post(Constants.URL_DELETEMEMBER, list);
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
            Gson gson = new Gson();
            BaseBO baseBo = gson.fromJson(result,
                    BaseBO.class);
            msg.what = Constants.HANDLER_DELETEMEMBER_SUCCESS;
            msg.obj = baseBo.getMessage();
            handler.sendMessage(msg);
        } else {
            msg.what = Constants.HANDLER_DELETEMBER_FAIL;
            msg.obj = message;
            handler.sendMessage(msg);
        }
    }


}
