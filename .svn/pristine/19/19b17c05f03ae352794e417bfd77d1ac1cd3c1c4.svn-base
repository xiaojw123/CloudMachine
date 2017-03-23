package com.cloudmachine.net.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.app.MyApplication;
import com.cloudmachine.cache.MySharedPreferences;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.struc.BaseBO;
import com.cloudmachine.struc.MessageBO;
import com.cloudmachine.struc.RepairRecordInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.URLs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class RepairRecordListAsync extends ATask {

	private Handler handler;
	private long deviceId;
	
	public RepairRecordListAsync(long deviceId,Context context,Handler handler){
		this.deviceId = deviceId;
		this.handler = handler;
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
		list.add(new BasicNameValuePair("deviceId", String.valueOf(deviceId)));
		String result = null;
		try {
				result = httpRequest.post(URLs.REPAIRRECORD, list);
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
			Gson gson = new Gson();
			BaseBO<List<RepairRecordInfo>> results = gson.fromJson(result,
					new TypeToken<BaseBO<List<RepairRecordInfo>>>() {
					}.getType());
			if(null != results && null !=results.getResult()){
				if (results.getResult().size() == 0) {
				}else{
//					MySharedPreferences.setSharedPInt(Constants.KEY_NewMessageSize, results.getResult().size());
					msg.what = Constants.HANDLER_REPAIRRECORD_SUCCESS;
					msg.obj = results.getResult();
					handler.sendMessage(msg);
					return;
				}
			}
		}
		msg.what = Constants.HANDLER_REPAIRRECORD_FAIL;
		handler.sendMessage(msg);
	}
	
}
