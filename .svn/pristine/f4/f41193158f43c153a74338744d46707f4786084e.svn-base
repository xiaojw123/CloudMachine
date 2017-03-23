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
import com.cloudmachine.struc.MachineBrandInfo;
import com.cloudmachine.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MachineBrandListAsync extends ATask {

	private Context context;
	private Handler handler;
	private String  pk_prod_def;
	
	public MachineBrandListAsync(Context context,Handler handler){
		this.context = context;
		this.handler = handler;
		getCacheName(Constants.URL_GETMACHINEBRAND);
	}
	

	@Override
	protected String doInBackground(String... params) {
		pk_prod_def = params[0];
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("pk_prod_def",pk_prod_def));
		String result = null;
		try {
				result = httpRequest.post(Constants.URL_GETMACHINEBRAND, list);
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
				 BaseBO<List<MachineBrandInfo>> baseBO = gson.fromJson(result, new TypeToken<BaseBO<List<MachineBrandInfo>>>(){}.getType());				
					msg.what = Constants.HANDLER_GETMACHINEBRAND_SUCCESS;
					msg.obj = baseBO.getResult();
					handler.sendMessage(msg);
					return;
			} catch (Exception e) {
			}
		} else {
		}
		if(!isHaveCache){
			msg.what = Constants.HANDLER_GETMACHINEBRAND_FAIL;
			msg.obj = message;
			handler.sendMessage(msg);
		}
	}

	
}
