package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.struc.BaseBO;
import com.cloudmachine.struc.RepairListInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.URLs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class GetRepairListByIdAsync extends ATask {

	private Handler handler;
	private long deviceId;

	public GetRepairListByIdAsync(Context context, Handler handler,long deviceId) {
		this.handler = handler;
		this.deviceId = deviceId;
		try {
			memberId = String.valueOf(MemeberKeeper.getOauth(context).getId());
		} catch (Exception ee) {

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
			result = httpRequest.post(URLs.REPAIR_HISTORY, list);
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
			try {
				Gson gson = new Gson();
				//Constants.MyLog("2222222222222");
				 //BaseBO<List<RepairListInfo>> baseBO = gson.fromJson(result, new TypeToken<BaseBO<List<RepairListInfo>>>(){}.getType());
				BaseBO<RepairListInfo> bo = gson.fromJson(result, new TypeToken<BaseBO<RepairListInfo>>(){}.getType());
				// Constants.MyLog("3333333333333");
					 msg.what = Constants.HANDLER_GET_REPAIRHISTORY_SUCCESS;
					 msg.obj = bo.getResult();
					 handler.sendMessage(msg);
					return;
			} catch (Exception e) {
			}
		} 
		//缓存数据第4步
		if(!isHaveCache){
			msg.what = Constants.HANDLER_GET_REPAIRHISTORY_FAILD;
			msg.obj = message;
			handler.sendMessage(msg);
		}
	}
}
