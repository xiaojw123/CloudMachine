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
import com.cloudmachine.struc.RootNodesInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GetRootNodesAsync extends ATask {

	private Context context;
	private Handler handler;
	
	public GetRootNodesAsync(Context context,Handler handler){
		this.context = context;
		this.handler = handler;
//		getCacheName(Constants.URL_GETROOTNODES);
	}

	@Override
	protected String doInBackground(String... params) {
		String deviceId = params[0];
		String memberId = "";//params[1];
		try{
			memberId = String.valueOf(MemeberKeeper.getOauth(context).getId());
		}catch(Exception ee){
		}
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("deviceId", deviceId));
		list.add(new BasicNameValuePair("memberId", memberId));
		String result = null;
		try {
				result = httpRequest.post(Constants.URL_GETROOTNODES, list);
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
				 BaseBO<List<RootNodesInfo>> baseBO = gson.fromJson(result, new TypeToken<BaseBO<List<RootNodesInfo>>>(){}.getType());				
					 msg.what = Constants.HANDLER_GETROOTNODES_SUCCESS;
						msg.obj = baseBO.getResult();
						handler.sendMessage(msg);
						return;
			} catch (Exception e) {
			}
		} else {
		}
		if(!isHaveCache){
			msg.what = Constants.HANDLER_GETDEVICEMEMBER_FAIL;
			msg.obj = message;
			handler.sendMessage(msg);
			
		}
	}

	
}
