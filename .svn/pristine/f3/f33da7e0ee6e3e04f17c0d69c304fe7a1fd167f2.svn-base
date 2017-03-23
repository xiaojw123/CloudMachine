package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.struc.AliPayBean;
import com.cloudmachine.struc.BaseBO;
import com.cloudmachine.struc.WeiXinEntityBean;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称：CloudMachine
 * 类描述：
 * 创建人：shixionglu
 * 创建时间：2017/2/21 下午2:46
 * 修改人：shixionglu
 * 修改时间：2017/2/21 下午2:46
 * 修改备注：
 */

public class YunBoxPayAsync extends ATask {

    private Handler handler;
    private Context context;
    private long    typeId;
    private long    brandId;
    private long    modelId;
    private String  address;
    private String  custname;
    private String  custtel;
    private String  payType;
    private int     deliveryMethod;
    private int     setupTime;
    private String  province;
    private String  rackId;

    public YunBoxPayAsync(Handler handler, Context context, long typeId, long brandId,
                          long modelId, String address, String custname, String custtel,
                          String payType, int deliveryMethod, int setupTime, String province, String rackId) {
        this.handler = handler;
        this.context = context;
        this.typeId = typeId;
        this.brandId = brandId;
        this.modelId = modelId;
        this.address = address;
        this.custname = custname;
        this.custtel = custtel;
        this.payType = payType;
        this.deliveryMethod = deliveryMethod;
        this.setupTime = setupTime;
        this.province = province;
        this.rackId = rackId;
        try{
            memberId = String.valueOf(MemeberKeeper.getOauth(context).getId());
        }catch(Exception e){
            Constants.ToastAction(e.toString());
        }
    }

    @Override
    protected String doInBackground(String... params) {
        IHttp httpRequest = new HttpURLConnectionImp();
        String result = null;
        try {
            result = httpRequest.post(Constants.URL_YUNBOXPAY, initListData());
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
            try{
                if (payType.equals("102")) {
                    Gson gson = new Gson();
                    BaseBO<AliPayBean> bo = gson.fromJson(result, new TypeToken<BaseBO<AliPayBean>>(){}.getType());
                    msg.what = Constants.HANDLER_GETYUNBOXPAY_SUCCESS;
                    msg.obj = bo.getResult();
                    handler.sendMessage(msg);
                } else if (payType.equals("101")){
                    Gson gson = new Gson();
                    BaseBO<WeiXinEntityBean> bo = gson.fromJson(result, new TypeToken<BaseBO<WeiXinEntityBean>>(){}.getType());
                    msg.what = Constants.HANDLER_GETYUNBOXPAY_SUCCESS;
                    msg.obj = bo.getResult();
                    handler.sendMessage(msg);
                }

                return;
            }catch(Exception e){
            }
        }
        //缓存数据第4步
        if(!isHaveCache){
            msg.what = Constants.HANDLER_GETYUNBOXPAY_FAILD;
            msg.obj = message;
            handler.sendMessage(msg);
        }
    }

    private List<NameValuePair> initListData() {
        List<NameValuePair> list = new ArrayList<NameValuePair>();
        list.add(Utils.addBasicValue("memberId", memberId));
        list.add(Utils.addBasicValue("typeId", String.valueOf(typeId)));
        list.add(Utils.addBasicValue("brandId", String.valueOf(brandId)));
        list.add(Utils.addBasicValue("modelId", String.valueOf(modelId)));
        list.add(Utils.addBasicValue("address", address));
        list.add(Utils.addBasicValue("custname", custname));
        list.add(Utils.addBasicValue("custtel", custtel));
        list.add(Utils.addBasicValue("payType", payType));
        list.add(Utils.addBasicValue("deliveryMethod", String.valueOf(deliveryMethod)));
        list.add(Utils.addBasicValue("setupTime", String.valueOf(setupTime)));
        list.add(Utils.addBasicValue("province", province));
        list.add(Utils.addBasicValue("rackId", rackId));
        return list;
    }


}
