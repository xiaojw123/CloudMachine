package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.MyApplication;
import com.cloudmachine.R;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.UIHelper;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class updateDeviceInfoAsync extends ATask {

	private Context context;
	private Handler handler;
	private int type;
	
	public updateDeviceInfoAsync(Context context,Handler handler){
		this.context = context;
		this.handler = handler;
	}
	
	@Override
	protected void onPreExecute() {
		if (!MyApplication.getInstance().isOpenNetwork(context)) {
			UIHelper.ToastMessage(context, context.getResources().getString(R.string.no_network));
			return ;
		}
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		String deviceId = params[0];//被授权的id
		String keys = params[1];//
		String values = params[2];//
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		String memberId = "";
		try{
			memberId = String.valueOf(MemeberKeeper
					.getOauth(context).getId());
		}catch(Exception ee){
			
		}
		String[] keyArray = keys.split(Constants.S_UPDATEDEVICEKEY_FG);
		String[] valuesArray = values.split(Constants.S_UPDATEDEVICEKEY_FG);
		list.add(new BasicNameValuePair("memberId", memberId));
		list.add(new BasicNameValuePair("deviceId", deviceId));
		int len = keyArray.length<valuesArray.length?keyArray.length:valuesArray.length;
		for(int i=0; i<len; i++){
			list.add(new BasicNameValuePair(keyArray[i], valuesArray[i]));
		}
//		list.add(new BasicNameValuePair("key", key));
//		list.add(new BasicNameValuePair("value", value));
		String result = null;
		try {
				result = httpRequest.post(Constants.URL_UPDATEDEVICEINFO, list);
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
				msg.what = Constants.HANDLER_UPDATEDEVICEBYKEY_SUCCESS;
				String message = baseBo.getMessage();
				msg.obj = message;
				handler.sendMessage(msg);
				return;
			}catch(Exception e){
			}
		} else {
		}
		msg.what = Constants.HANDLER_UPDATEDEVICEBYKEY_FAIL;
		msg.obj = message;
		handler.sendMessage(msg);
	}

}
