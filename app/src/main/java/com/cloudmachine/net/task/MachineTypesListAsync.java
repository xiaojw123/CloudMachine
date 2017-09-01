package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.MachineTypeInfo;
import com.cloudmachine.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

public class MachineTypesListAsync extends ATask {

	private Context context;
	private Handler handler;
	
	public MachineTypesListAsync(Context context,Handler handler){
		this.context = context;
		this.handler = handler;
		getCacheName(Constants.URL_GETMACHINETYPES);
	}
	

	@Override
	protected String doInBackground(String... params) {
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		String result = null;
		try {
				result = httpRequest.post(Constants.URL_GETMACHINETYPES, list);
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
				 BaseBO<List<MachineTypeInfo>> baseBO = gson.fromJson(result, new TypeToken<BaseBO<List<MachineTypeInfo>>>(){}.getType());				
					msg.what = Constants.HANDLER_GETMACHINETYPES_SUCCESS;
					msg.obj = baseBO.getResult();
					handler.sendMessage(msg);
					return;

			} catch (Exception e) {
			}
		} else {
		}
		if(!isHaveCache){
			msg.what = Constants.HANDLER_GETMACHINETYPES_FAIL;
			msg.obj = message;
			handler.sendMessage(msg);
		}
	}

}
