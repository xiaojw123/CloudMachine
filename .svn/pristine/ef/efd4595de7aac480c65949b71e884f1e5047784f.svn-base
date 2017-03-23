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
import com.cloudmachine.struc.BaseBO;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.google.gson.Gson;

public class DeleteDeviceAsync extends ATask {

	private Context context;
	private Handler handler;
	private int type;
	
	public DeleteDeviceAsync(Context context,Handler handler){
		this.context = context;
		this.handler = handler;
	}
	

	@Override
	protected String doInBackground(String... params) {
		String deviceId = params[0];//被授权的id
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		String memberId = "";
		try{
			memberId = String.valueOf(MemeberKeeper
					.getOauth(context).getId());
		}catch(Exception ee){
			
		}
		list.add(new BasicNameValuePair("memberId", memberId));
		list.add(new BasicNameValuePair("deviceId", deviceId));
		String result = null;
		try {
				result = httpRequest.post(Constants.URL_DELETEDEVICE, list);
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
				Gson gson = new Gson();
				BaseBO baseBo = gson.fromJson(result,
						BaseBO.class);
				msg.what = Constants.HANDLER_DELETEDEVICE_SUCCESS;
				msg.obj = baseBo.getMessage();
				handler.sendMessage(msg);
				return;
			}catch(Exception e){
			}
		} else {
		}
		msg.what = Constants.HANDLER_DELETEDEVICE_FAIL;
		msg.obj = message;
		handler.sendMessage(msg);
	}

	
	
}
