package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.MyApplication;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.bean.DailyWorkInfo;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.UIHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class DailyWorkDitailsListAsync extends ATask {

	private Context context;
	private Handler handler;
	private int type;
	private long deviceId;
	private String date;
	
	public DailyWorkDitailsListAsync(long deviceId, String date, Context context,Handler handler){
		this.context = context;
		this.handler = handler;
		this.deviceId = deviceId;
		this.date = date;
		
		try{
			memberId = String.valueOf(MemeberKeeper.getOauth(context).getId());
		}catch(Exception ee){
			
		}
	}
	
	@Override
	protected void onPreExecute() {
		if (!MyApplication.getInstance().isOpenNetwork(context)) {
			UIHelper.ToastMessage(context, "网络连接失败,请检查您的网络设置");
			return ;
		}
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("deviceId", String.valueOf(deviceId)));
		list.add(new BasicNameValuePair("memberId", memberId));
		list.add(new BasicNameValuePair("date", date));
		String result = null;
		try {
				result = httpRequest.post(Constants.URL_DAILYWORKDITAILS, list);
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
							 BaseBO<List<DailyWorkInfo>> baseBO = gson.fromJson(result, new TypeToken<BaseBO<List<DailyWorkInfo>>>(){}.getType());	
							msg.what = Constants.HANDLER_DAILYWORK_SUCCESS;
							msg.obj = baseBO.getResult();
							handler.sendMessage(msg);
							return;
				
			}catch(Exception e){
			}
		} else {
		}
		msg.what = Constants.HANDLER_DAILYWORK_FAIL;
		msg.obj = message;
		handler.sendMessage(msg);
	}

}
