package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.bean.AlianceEvaluateInfo;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.LarkUrls;
import com.cloudmachine.utils.ToastUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiaojw on 2017/8/11.
 */

public class GetEvaluateInfoTask extends ATask {

    private Handler handler;
    private String orderNum;
    private Context mContext;

    public GetEvaluateInfoTask(Handler mHandler, Context context,String orderNum) {
        mContext=context;
        this.handler = mHandler;
        this.orderNum = orderNum;
    }

    @Override
    protected String doInBackground(String... params) {
        try {
            IHttp httpRequest = new HttpURLConnectionImp();
            Map<String, String> map = new HashMap<>();
            map.put("orderNo", orderNum);
            return httpRequest.post(LarkUrls.GET_EVALUATE_INFO, map);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String result) {
        // 缓存数据第2步
        super.onPostExecute(result);
        decodeJson(result);
    }

    @Override
    protected void decodeJson(String result) {
        // TODO Auto-generated method stub
        // 缓存数据第3步
        super.decodeJson(result);
        Message msg = Message.obtain();
        if (isSuccess) {
            Gson gson = new Gson();
            BaseBO<AlianceEvaluateInfo> alianceBo = gson.fromJson(result,
                    new TypeToken<BaseBO<AlianceEvaluateInfo>>() {
                    }.getType());
            AlianceEvaluateInfo info = alianceBo.getResult();
            msg.what = Constants.HANDLER_GET_EVALUATE_INFO_SUCCESS;
            msg.obj = info;
            handler.sendMessage(msg);
        } else {
            ToastUtils.showToast(mContext,message);
        }

    }
}
