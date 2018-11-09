package com.cloudmachine.net.task;

import android.os.Handler;
import android.os.Message;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.LarkUrls;

import java.util.HashMap;
import java.util.Map;

public class GivePermissionNewAsync extends ATask {

    private Handler handler;
    private int messageType;//1 增加成员   2移交机主
    private long toMemberId;
    private long deviceIdS;

    public GivePermissionNewAsync(long toMemberId, long deviceIdS, int messageType, Handler handler) {
        this.handler = handler;
        this.toMemberId = toMemberId;
        this.deviceIdS = deviceIdS;
        this.messageType = messageType;
    }

    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        String result = null;
        try {
            Map<String,String> paramsMap=new HashMap<>();
            paramsMap.put("toMemberId",String.valueOf(toMemberId));
            paramsMap.put("deviceId",String.valueOf(deviceIdS));
            paramsMap.put("messageType",String.valueOf(messageType));
            result = httpRequest.post(LarkUrls.MEMBER_PERMISSION_URL, paramsMap);
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
            msg.what = Constants.HANDLER_ADDMEMBER_SUCCESS;
        } else {
            msg.what = Constants.HANDLER_ADDMEMBER_FAIL;
        }
        msg.obj = message;
        handler.sendMessage(msg);
    }


}
