package com.cloudmachine.net.task;

import android.os.Handler;
import android.os.Message;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.Utils;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojw on 2017/8/9.
 */

public class UpdateDeviceInfoTask extends ATask {

    long memberId;
    String deviceId;
    String deviceName;
    Handler handler;

    public UpdateDeviceInfoTask(Handler handler, long memberId, String deviceId, String deviceName) {
        this.handler = handler;
        this.memberId = memberId;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
    }

    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        try {
            return httpRequest.post(Constants.URL_UPDATEDEVICEINFO, initListData());
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
            try {
                Gson gson = new Gson();
                BaseBO baseBo = gson.fromJson(result,
                        BaseBO.class);
                msg.what = Constants.HANDLER_UPDATE_INFO_SUCCESS;
                msg.obj = baseBo.getMessage();
                handler.sendMessage(msg);
                return;
            } catch (Exception e) {
            }
        }
        //缓存数据第4步
        if (!isHaveCache) {
            msg.what = Constants.HANDLER_UPDATE_INFO_FAILD;
            msg.obj = message;
            handler.sendMessage(msg);
        }
    }

    private List<NameValuePair> initListData() {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(Utils.addBasicValue("memberId", String.valueOf(memberId)));
        list.add(Utils.addBasicValue("deviceId", deviceId));
        list.add(Utils.addBasicValue("deviceName", deviceName));
        return list;
    }

}
