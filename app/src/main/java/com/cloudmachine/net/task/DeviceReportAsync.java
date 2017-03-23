package com.cloudmachine.net.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.app.MyApplication;
import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.struc.BaseBO;
import com.cloudmachine.struc.FaultWarnInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.UIHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * 
 * 
 * @author shixionglu 获取单个设备的检测报告
 */
public class DeviceReportAsync extends ATask {

	private Context context;
	private Handler handler;
	private long deviceId;

	public DeviceReportAsync(Context mContext, Handler mHandler, long deviceId) {
		// TODO Auto-generated constructor stub
		this.context = mContext;
		this.handler = mHandler;
		this.deviceId = deviceId;
	}

	@Override
	protected void onPreExecute() {
		if (!MyApplication.getInstance().isOpenNetwork(context)) {
			UIHelper.ToastMessage(context, "网络连接失败,请检查您的网络设置");
			return;
		}
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {

		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		String memid = "0";
		try {
			memid = String.valueOf(MemeberKeeper.getOauth(context).getId());
		} catch (Exception ee) {

		}
		list.add(new BasicNameValuePair("memberId", memid));
		list.add(new BasicNameValuePair("deviceId", String.valueOf(deviceId)));
		String result = null;
		try {
			result = httpRequest.post(Constants.URL_CHECKREPORT, list);
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
			Gson gson = new Gson();					 
			 BaseBO<FaultWarnInfo> baseBO = gson.fromJson(result, new TypeToken<BaseBO<FaultWarnInfo>>(){}.getType());
			 
			 FaultWarnInfo testReportInfo =baseBO.getResult();
			 if(null != testReportInfo){
				 msg.what = Constants.HANDLE_GETCHECKREPORT_SUCCESS;
			     msg.obj = testReportInfo;
				 handler.sendMessage(msg);
				return;
			 }
		} 
		if(!isHaveCache){
			msg.what = Constants.HANDLE_GETCHECKREPORT_FAILD;
			msg.obj = message;
			handler.sendMessage(msg);
		}
		
	}

}
