package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.Utils;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

public class SubmitCommentAsync extends ATask {

    private Handler handler;
    private String css_work_no;// 工单id
    private String satisfaction;// 满意度评分值
    private String cust_tel;// 评价人电话
    private String evaluate;// 评价标签
    private String suggestion;// 客户建议
    private boolean isAlience;

    public SubmitCommentAsync(Context context, Handler handler,
                              String css_work_no, String satisfaction, String cust_tel,
                              String evaluate, String suggestion, boolean isAlience) {
        super();
        this.handler = handler;
        this.css_work_no = css_work_no;
        this.satisfaction = satisfaction;
        this.cust_tel = cust_tel;
        this.evaluate = evaluate;
        this.suggestion = suggestion;
        this.isAlience = isAlience;
        try {
            memberId = String.valueOf(MemeberKeeper.getOauth(context).getId());
        } catch (Exception ee) {

        }
    }

    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        String result = null;
        try {
            String url = isAlience ? Constants.URL_SAVEEVALUATE_ALLIANCE : Constants.URL_SAVEEVALUATE;
            result = httpRequest.post(url,
                    initListData());
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
            try {
                Gson gson = new Gson();
                BaseBO baseBo = gson.fromJson(result, BaseBO.class);
                msg.what = Constants.HANDLER_SUBMITCOMMENT_SUCCESS;
                msg.obj = isAlience ? "评价成功" : baseBo.getMessage();
                handler.sendMessage(msg);
                return;
            } catch (Exception e) {
            }
        }
        // 缓存数据第4步
        if (!isHaveCache) {
            msg.what = Constants.HANDLER_SUBMITCOMMENT_FAILD;
            msg.obj = message;
            handler.sendMessage(msg);
        }
    }

    private List<NameValuePair> initListData() {
        List<NameValuePair> list = new ArrayList<NameValuePair>();

        list.add(Utils.addBasicValue("memberId", memberId));

        // if(mcDeviceInfo.getDeviceName()!=null){
        // list.add(Utils.addBasicValue("memberId", memberId));
        // }
        // if(deviceId!=0){
        // list.add(Utils.addBasicValue("id", String.valueOf(deviceId)));
        // }
        // list.add(Utils.addBasicValue("checkType", "1"));
        // list.add(Utils.addBasicValue("memberId",
        // String.valueOf(MemeberKeeper.getOauth(AddDeviceActivity.this).getId())));

        list.add(Utils.addBasicValue("css_work_no", css_work_no));
        list.add(Utils.addBasicValue("satisfaction", satisfaction));
        if (!isAlience) {
            list.add(Utils.addBasicValue("cust_tel", cust_tel));
        }
        list.add(Utils.addBasicValue("evaluate", evaluate));
        list.add(Utils.addBasicValue("suggestion", suggestion));

        // list.add(Utils.addBasicValue("idCardPhoto",
        // SparseArrayToString(resultMap)));

        return list;
    }

}
