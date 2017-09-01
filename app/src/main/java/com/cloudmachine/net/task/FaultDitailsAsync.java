package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.FaultWarnDitailsInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.URLs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class FaultDitailsAsync extends ATask {

	private Handler handler;
	private long dateTime;
	private String memberId = "0";
	private long deviceId;
	private int id;
	
	
	public FaultDitailsAsync(long dateTime,long deviceId,int id,Context context,Handler handler){
		this.handler = handler;
		this.dateTime = dateTime;
		this.deviceId = deviceId;
		this.id = id;
			try{
				memberId = String.valueOf(MemeberKeeper.getOauth(context).getId());
			}catch(Exception ee){
				
			}
		
	}
	

	@Override
	protected String doInBackground(String... params) {
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("memberId", memberId));
		list.add(new BasicNameValuePair("dateTime", String.valueOf(dateTime)));
		list.add(new BasicNameValuePair("deviceId", String.valueOf(deviceId)));
		list.add(new BasicNameValuePair("rowSize", "10"));
		list.add(new BasicNameValuePair("id", String.valueOf(id)));
		String result = null;
		try {
			result = httpRequest.post(URLs.FAULT_DITAILS, list);
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
		//缓存数据第3步
		super.decodeJson(result);
		Message msg = Message.obtain();
		if (isSuccess) {
			try{
				Gson gson = new Gson();
				 BaseBO<FaultWarnDitailsInfo> baseBO = gson.fromJson(result, new TypeToken<BaseBO<FaultWarnDitailsInfo>>(){}.getType());				
					msg.what = Constants.HANDLER_FAULTDITAILS_SUCCESS;
					msg.obj = baseBO.getResult();
					handler.sendMessage(msg);
				return;
			}catch(Exception e){
			}
		} else {
		}
		//缓存数据第4步
		if(!isHaveCache){
			msg.what = Constants.HANDLER_FAULTDITAILS_FAIL;
			msg.obj = message;
			handler.sendMessage(msg);
		}
	}
}
