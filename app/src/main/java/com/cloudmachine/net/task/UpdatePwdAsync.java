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

public class UpdatePwdAsync extends ATask {

    private Handler handler;
    private String oldPassword;
    private String newPassword;
    private String rePassword;


    public UpdatePwdAsync(String oldPassword, String newPassword, String rePassword, Handler handler) {
        this.handler = handler;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.rePassword = rePassword;
    }


    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        String result = null;
        try {
            Map<String,String> paramsMap=new HashMap<>();
            paramsMap.put("oldPassword", oldPassword);
            paramsMap.put("newPassword", newPassword);
            paramsMap.put("rePassword", rePassword);
            result = httpRequest.post(LarkUrls.PSW_MODIFY_URL, paramsMap);
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
        super.decodeJson(result);
        Message msg = Message.obtain();
        if (isSuccess) {
            msg.what = Constants.HANDLER_UPDATEPWD_SUCCESS;
        } else {
            msg.what = Constants.HANDLER_UPDATEPWD_FAIL;
        }
        msg.obj = message;
        handler.sendMessage(msg);
    }
}
