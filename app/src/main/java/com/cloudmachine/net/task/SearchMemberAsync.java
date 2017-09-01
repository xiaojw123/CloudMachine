package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.cloudmachine.MyApplication;
import com.cloudmachine.R;
import com.cloudmachine.bean.MemberInfo;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.UIHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchMemberAsync extends ATask {

	private Context context;
	private Handler handler;
	private int type;
	
	public SearchMemberAsync(Context context,Handler handler){
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
		String mobi = params[0];
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("mobi", mobi));
		String result = null;
		try {
				result = httpRequest.post(Constants.URL_SEARCHMEMBER, list);
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
				JSONObject json = new JSONObject(result);
					String gsonStr = json.getString("result");
					if (!TextUtils.isEmpty(gsonStr)&&gsonStr.length()>2) { 
						Gson gson = new Gson();
						MemberInfo mcMemberInfo = gson.fromJson(gsonStr,
								new TypeToken<MemberInfo>() {
								}.getType());
//						deviceList.clear();
//						deviceList.addAll(mcDeviceInfo);
						msg.what = Constants.HANDLER_SEARCHMEMBER_SUCCESS;
						msg.obj = mcMemberInfo;
						handler.sendMessage(msg);
						return;
					}
				
			}catch(Exception e){
			}
		} else {
		}
		msg.what = Constants.HANDLER_SEARCHMEMBER_FAIL;
		msg.obj = message;
		handler.sendMessage(msg);
	}

}
