package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.cloudmachine.MyApplication;
import com.cloudmachine.bean.MachineModelInfo;
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

public class MachineModelListAsync extends ATask {

	private Context context;
	private Handler handler;
	
	public MachineModelListAsync(Context context,Handler handler){
		this.context = context;
		this.handler = handler;
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
		try {
		String pk_prod_def = params[0];
		String pk_brand = params[1];

		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("pk_prod_def", pk_prod_def));
		list.add(new BasicNameValuePair("pk_brand", pk_brand));
		String result = null;
		result = httpRequest.post(Constants.URL_GETMACHINEMODEL, list);
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
						if (!TextUtils.isEmpty(gsonStr)&&gsonStr.length()>2) { 
							Gson gson = new Gson();
							List<MachineModelInfo> permissionList = gson.fromJson(gsonStr,
									new TypeToken<List<MachineModelInfo>>() {
									}.getType());
//							Constants.setObjcet2File(permissionList, Constants.P_PERMISSIONSKEY);
							msg.what = Constants.HANDLER_GETMACHINEMODEL_SUCCESS;
							msg.obj = permissionList;
							handler.sendMessage(msg);
							return;
						}
					}
				
			}catch(Exception e){
			}
		} else {
		}
		msg.what = Constants.HANDLER_GETMACHINEMODEL_FAIL;
		msg.obj = message;
		handler.sendMessage(msg);
	}

	
}
