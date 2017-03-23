package com.cloudmachine.net.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.R;
import com.cloudmachine.app.MyApplication;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.struc.BaseBO;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.UIHelper;
import com.google.gson.Gson;

public class EnclosureUploadAsync extends ATask {

	private Context context;
	private Handler handler;
	private int type;
	
	public EnclosureUploadAsync(Context context,Handler handler){
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
		String deviceId = params[0];
		String fence = params[1];
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		String memid = "";
		try{
			memid = String.valueOf(MemeberKeeper
					.getOauth(context).getId());
		}catch(Exception ee){
			
		}
		list.add(new BasicNameValuePair("memberId", memid));
		list.add(new BasicNameValuePair("deviceId", deviceId));
		list.add(new BasicNameValuePair("fence", fence));
		String result = null;
		try {
				result = httpRequest.uploadPost(Constants.URL_ADDFENCE, list);
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
				msg.what = Constants.HANDLER_ADDFENCE_SUCCESS;
				msg.obj = baseBo.getMessage();
				handler.sendMessage(msg);
				return;
				
			}catch(Exception e){
			}
		} else {
		}
		msg.what = Constants.HANDLER_ADDFENCE_FAIL;
		msg.obj = message;
		handler.sendMessage(msg);
	}

	
}
