package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.MachineDetailInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author shixionglu 获取设备的基本信息
 */
public class MachineDetailAsync extends ATask {

	private Context context;
	private Handler handler;
	private long deviceId;

	public MachineDetailAsync(long deviceId, Context mContext, Handler mHandler) {
		this.context = mContext;
		this.handler = mHandler;
		this.deviceId = deviceId;
		try {
			memberId = String.valueOf(MemeberKeeper.getOauth(context).getId());
		} catch (Exception ee) {

		}
		getCacheName(Constants.URL_MACHINE_DETAIL, String.valueOf(deviceId));
	}

	@Override
	protected String doInBackground(String... params) {
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("deviceId", String.valueOf(deviceId)));
		list.add(new BasicNameValuePair("memberId", memberId));
		String result = null;
		try {
			result = httpRequest.post(Constants.URL_MACHINE_DETAIL, list);
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
				 BaseBO<MachineDetailInfo> baseBO = gson.fromJson(result, new TypeToken<BaseBO<MachineDetailInfo>>(){}.getType());
					 	msg.what = Constants.HANDLER_GETMACHINEDETAIL_SUCCESS;
						msg.obj = baseBO.getResult();
						handler.sendMessage(msg);
						return;
			} 
			catch (Exception e) {

			}
		} 
		if(!isHaveCache){
			msg.what = Constants.HANDLER_GETMACHINEDETAIL_FAILD;
			msg.obj = message;
			handler.sendMessage(msg);
		}
	}
}
