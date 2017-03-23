package com.cloudmachine.net.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.struc.AgentInfo;
import com.cloudmachine.struc.BaseBO;
import com.cloudmachine.struc.Member;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.URLs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class AgentInfoAsync extends ATask {

	private Handler handler;
	private long deviceId;
	private String memberId = "0";
	
	
	public AgentInfoAsync(long deviceId,Context context,Handler handler){
		this.handler = handler;
		this.deviceId = deviceId;
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
		list.add(new BasicNameValuePair("deviceId", String .valueOf(deviceId)));
		String result = null;
		try {
			result = httpRequest.post(URLs.GET_AGENTS, list);
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
				BaseBO<AgentInfo> bo = gson.fromJson(result, new TypeToken<BaseBO<AgentInfo>>(){}.getType());	
					msg.what = Constants.HANDLER_GETAGENTS_SUCCESS;
					msg.obj = bo.getResult();
					handler.sendMessage(msg);
				return;
			}catch(Exception e){
				Constants.MyLog(e.getMessage());
			}
		} else {
		}
		//缓存数据第4步
		if(!isHaveCache){
			msg.what = Constants.HANDLER_GETAGENTS_FAIL;
			msg.obj = message;
			handler.sendMessage(msg);
		}
	}
}
