package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.cloudmachine.helper.DeviceHelper;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.NewRepairInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.LarkUrls;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.Utils;
import com.cloudmachine.utils.photo.zoom.SDK16;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.internal.util.unsafe.MpmcArrayQueue;

public class SubmitRepairAsync extends ATask {

    private Handler handler;
    private NewRepairInfo newRepairInfo;
    Context context;

    public SubmitRepairAsync(Context context, Handler handler, NewRepairInfo newRepairInfo) {
        this.context = context;
        this.handler = handler;
        this.newRepairInfo = newRepairInfo;
    }

    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        String result = null;
        try {
            result = httpRequest.post(LarkUrls.SAVE_REPAIR_ORDER, getParamsMap());
            DeviceHelper.saveRepairInfo(context, newRepairInfo);
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
                Gson gson = new Gson();
                BaseBO baseBo = gson.fromJson(result,
                        BaseBO.class);
                msg.what = Constants.HANDLER_NEWREPAIR_SUCCESS;
                msg.obj = baseBo.getMessage();
                handler.sendMessage(msg);
        }else{
            msg.what = Constants.HANDLER_NEWREPAIR_FAILD;
            msg.obj = message;
            handler.sendMessage(msg);
        }
    }



    private Map<String,String> getParamsMap(){
        Map<String,String> m=new HashMap<>();
        if (newRepairInfo!=null) {
            m.put("province",newRepairInfo.getProvince());
            String deviceId=newRepairInfo.getDeviceId();
            if (!TextUtils.isEmpty(deviceId)) {
                m.put("deviceId",deviceId);
            }
            m.put("reporterName",newRepairInfo.getVmacopname());
            m.put("reporterMobile",newRepairInfo.getVmacoptel());
            String machineNum=newRepairInfo.getVmachinenum();
            if (!TextUtils.isEmpty(machineNum)) {
                m.put("rackId",machineNum);
            }
            m.put("demandDescription",newRepairInfo.getVdiscription());
            m.put("workAddress",newRepairInfo.getVworkaddress());
            String logoAddress=newRepairInfo.getLogo_address();
            if (!TextUtils.isEmpty(logoAddress)){
                m.put("logoAddress",logoAddress);
            }
            m.put("lat",String.valueOf(newRepairInfo.getLat()));
            m.put("lng",String.valueOf(newRepairInfo.getLng()));
            m.put("serviceType",newRepairInfo.getVservicetype());
            m.put("typeId",newRepairInfo.getTypeId());
            m.put("brandId",newRepairInfo.getBrandId());
            m.put("modelId",newRepairInfo.getModelId());
        }
        return m;
    }

}
