package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.struc.BaseBO;
import com.cloudmachine.struc.YunBoxStoreVolumeInfo;
import com.cloudmachine.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/3/2 下午3:49
 * 修改人：shixionglu
 * 修改时间：2017/3/2 下午3:49
 * 修改备注：
 */

public class YunBoxStoreAsync extends ATask {

    private Context context;
    private  Handler handler;

    public YunBoxStoreAsync(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        String result = null;
        try {
            result = httpRequest.post(Constants.URL_YUNBOXSTOREVOLUME, list);
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
        // TODO Auto-generated method stub
        super.decodeJson(result);

        Message msg = Message.obtain();
        if (isSuccess) {
            try{

                Gson gson = new Gson();
                BaseBO<YunBoxStoreVolumeInfo> bo = gson.fromJson(result, new TypeToken<BaseBO<YunBoxStoreVolumeInfo>>(){}.getType());
                msg.what = Constants.HANDLER_YUNBOXSTOREVOLUME_SUCCESS;
                msg.obj = bo.getResult();
                handler.sendMessage(msg);
                return;
            }catch(Exception e){
            }
        }
        //缓存数据第4步
        if(!isHaveCache){
            msg.what = Constants.HANDLER_YUNBOXSTOREVOLUME_FAILD;
            msg.obj = message;
            handler.sendMessage(msg);
        }
    }
}
