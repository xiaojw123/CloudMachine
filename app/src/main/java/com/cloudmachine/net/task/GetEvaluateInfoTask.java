package com.cloudmachine.net.task;

import android.os.Handler;
import android.os.Message;

import com.cloudmachine.bean.AlianceEvaluateInfo;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.DisadvantageBean;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.ui.home.model.EvaluateInfoBean;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.URLs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaojw on 2017/8/11.
 */

public class GetEvaluateInfoTask extends ATask {

    private Handler handler;
    private String orderNum;
    private boolean isAlliance;

    public GetEvaluateInfoTask(Handler mHandler, String orderNum, boolean isAlliance) {
        this.handler = mHandler;
        this.orderNum = orderNum;
        this.isAlliance = isAlliance;
    }

    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("css_work_no", orderNum));
        String url = isAlliance ? URLs.GET_EVALUATE_INFO_ALLIANCE : URLs.GET_EVALUATE_INFO;
        try {
            return httpRequest.post(url, list);
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
            EvaluateInfoBean evaluteBean;
            if (isAlliance) {

                BaseBO<AlianceEvaluateInfo> alianceBo = gson.fromJson(result,
                        new TypeToken<BaseBO<AlianceEvaluateInfo>>() {
                        }.getType());
                evaluteBean = new EvaluateInfoBean();
                AlianceEvaluateInfo info = alianceBo.getResult();
                AlianceEvaluateInfo.EvaluateDTOBean dtoBean = info.getEvaluateDTO();
//                dtoBean.getLevel()
                evaluteBean.setSatisfaction(dtoBean.getLevel());
                evaluteBean.setSuggestion(dtoBean.getRemark());
                List<AlianceEvaluateInfo.AdvantageBean> advantageBeens = info.getAdvantage();
                if (advantageBeens != null) {
                    List<DisadvantageBean> atList = new ArrayList<>();
                    for (AlianceEvaluateInfo.AdvantageBean ab : advantageBeens) {
                        DisadvantageBean bean1 = new DisadvantageBean();
                        bean1.setCode(String.valueOf(ab.getCodeKey()));
                        bean1.setCode_NAME(ab.getCodeValue());
                        atList.add(bean1);
                    }
                    evaluteBean.setAdvantage(atList);
                }
                List<AlianceEvaluateInfo.DisadvantageBean> disadvantageBeens = info.getDisadvantage();
                if (disadvantageBeens != null) {
                    List<DisadvantageBean>  disAtList=new ArrayList<>();
                    for (AlianceEvaluateInfo.DisadvantageBean dt:disadvantageBeens){
                        DisadvantageBean bean2=new DisadvantageBean();
                        bean2.setCode(String.valueOf(dt.getCodeKey()));
                        bean2.setCode_NAME(dt.getCodeValue());
                        disAtList.add(bean2);
                    }
                    evaluteBean.setDisadvantage(disAtList);
                }

            } else {
                BaseBO<EvaluateInfoBean>  bo = gson.fromJson(result,
                        new TypeToken<BaseBO<EvaluateInfoBean>>() {
                        }.getType());
               evaluteBean=bo.getResult();
            }
            msg.what = Constants.HANDLER_GET_EVALUATE_INFO_SUCCESS;
            msg.obj = evaluteBean;
            handler.sendMessage(msg);
        } else {
            msg.what = Constants.HANDLER_GET_EVALUATE_INFO_FAILED;
            msg.obj = message;
            handler.sendMessage(msg);
        }

    }
}
