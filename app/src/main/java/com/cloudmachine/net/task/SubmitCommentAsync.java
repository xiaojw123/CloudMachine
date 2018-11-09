package com.cloudmachine.net.task;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.LarkUrls;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.ToastUtils;
import com.cloudmachine.utils.Utils;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubmitCommentAsync extends ATask {

    private String mOrderNo;// 工单id
    private String mSatisfaction;// 满意度评分值
    private String mEvaluateStr;// 评价标签
    private String mSuggestion;// 客户建议
    private Context mContext;

    public SubmitCommentAsync(Context context,
                              String orderNo, String satisfaction,
                              String evaluateStr, String suggestion) {
        mContext=context;
        mOrderNo = orderNo;
        mSatisfaction = satisfaction;
        mEvaluateStr = evaluateStr;
        mSuggestion = suggestion;
    }

    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        String result = null;
        try {
            result = httpRequest.post(LarkUrls.SAVE_EVALUATE_URL,
                    getParamsMap());
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
        if (isSuccess) {
            ToastUtils.showToast(mContext,"评价成功");
            ((Activity)mContext).finish();
        } else {
            ToastUtils.showToast(mContext,message);
        }
    }
//
//    private List<NameValuePair> initListData() {
//        List<NameValuePair> list = new ArrayList<NameValuePair>();
//
//        list.add(Utils.addBasicValue("memberId", memberId));
//
//        // if(mcDeviceInfo.getDeviceName()!=null){
//        // list.add(Utils.addBasicValue("memberId", memberId));
//        // }
//        // if(deviceId!=0){
//        // list.add(Utils.addBasicValue("id", String.valueOf(deviceId)));
//        // }
//        // list.add(Utils.addBasicValue("checkType", "1"));
//        // list.add(Utils.addBasicValue("memberId",
//        // String.valueOf(MemeberKeeper.getOauth(AddDeviceActivity.this).getId())));
//
//        list.add(Utils.addBasicValue("css_work_no", css_work_no));
//        list.add(Utils.addBasicValue("satisfaction", satisfaction));
////        if (!isAlience) {
////            list.add(Utils.addBasicValue("cust_tel", cust_tel));
////        }
//        list.add(Utils.addBasicValue("evaluate", evaluate));
//        list.add(Utils.addBasicValue("suggestion", suggestion));
//
//        // list.add(Utils.addBasicValue("idCardPhoto",
//        // SparseArrayToString(resultMap)));
//
//        return list;
//    }

    private Map<String, String> getParamsMap() {
        Map<String, String> map = new HashMap<>();
        map.put("orderNo", mOrderNo);
        map.put("satisfaction", mSatisfaction);
        map.put("suggestion", mSuggestion);
        map.put("evaluateTag",mEvaluateStr);
        return map;
    }


}
