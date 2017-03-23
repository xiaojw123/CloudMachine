package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.struc.BaseBO;
import com.cloudmachine.struc.SetUpTimeInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/2/19 下午8:28
 * 修改人：shixionglu
 * 修改时间：2017/2/19 下午8:28
 * 修改备注：
 */

public class GetSetupTimeAsync extends ATask {

    private Handler handler;
    private Context context;

    public GetSetupTimeAsync(Handler handler, Context context) {
        this.handler = handler;
        this.context = context;

        try {
            memberId = String.valueOf(MemeberKeeper.getOauth(context).getId());
            //memberId = "714";
        } catch (Exception ee) {
            Constants.ToastAction("未获取到成员id");
        }
    }

    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(new BasicNameValuePair("memberId", memberId));
        String result = null;
        try {
            result = httpRequest.post(Constants.URL_GETSETUPTIME, list);
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
        //缓存数据第3步
        super.decodeJson(result);
        Message msg = Message.obtain();
        if (isSuccess) {
            Constants.MyLog("chenggong");
            try {
                Gson gson = new Gson();
                BaseBO<List<SetUpTimeInfo>> baseBO = gson.fromJson(result, new TypeToken<BaseBO<List<SetUpTimeInfo>>>(){}.getType());
                msg.what = Constants.HANDLER_GETSETUPTIME_SUCCESS;
                msg.obj = baseBO.getResult();
                handler.sendMessage(msg);
                Constants.MyLog("获取优惠券数据成功");
                return;
            } catch (Exception e) {
                Constants.MyLog(e.toString());
            }
        } else {
            Constants.MyLog("11111111");
        }
        //缓存数据第4步
        if(!isHaveCache){
            msg.what = Constants.HANDLER_GETSETUPTIME_FAILD;
            msg.obj = message;
            handler.sendMessage(msg);
        }
    }

}
