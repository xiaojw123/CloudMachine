package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.cloudmachine.MyApplication;
import com.cloudmachine.R;
import com.cloudmachine.bean.VersionInfo;
import com.cloudmachine.cache.MySharedPreferences;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.UIHelper;
import com.cloudmachine.utils.VerisonCheckSP;
import com.cloudmachine.utils.VersionU;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetVersionAsync extends ATask {

	private Context context;
	private Handler handler;
	private int type;
	
	public GetVersionAsync(Context context,Handler handler){
		this.context = context;
		this.handler = handler;
		VerisonCheckSP.insertTime(context,
				System.currentTimeMillis());
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
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("system", "Android"));
		list.add(new BasicNameValuePair("version", VersionU.getVersionName()));
		String result = null;
		try {
				result = httpRequest.post(Constants.URL_GETVERSION, list);
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
					if(json.has("result"))
					{
						String gsonStr = json.getString("result");
						if (!TextUtils.isEmpty(gsonStr)&&gsonStr.length()>2&&!gsonStr.equals("null")) { 
							Gson gson = new Gson();
							VersionInfo versionInfo = gson.fromJson(gsonStr,
									new TypeToken<VersionInfo>() {
									}.getType());
							MySharedPreferences.setSharedPString(Constants.KEY_NewVersion,
									versionInfo.getVersion());
							msg.what = Constants.HANDLER_GETVERSION_SUCCESS;
							msg.obj = versionInfo;
							handler.sendMessage(msg);
							return;
						}else{
							VersionInfo v = new VersionInfo();
							v.setMustUpdate(-1);
							v.setMessage(json.getString("message"));
							msg.what = Constants.HANDLER_GETVERSION_SUCCESS;
							msg.obj = v;
							handler.sendMessage(msg);
							return;
						}
					}
				
			}catch(Exception e){
			}
		} else {
		}
		msg.what = Constants.HANDLER_GETVERSION_FAIL;
		msg.obj = message;
		handler.sendMessage(msg);
	}

	
	
}
