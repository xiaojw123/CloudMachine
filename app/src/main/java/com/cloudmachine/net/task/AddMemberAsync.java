package com.cloudmachine.net.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.bean.BaseBO;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.google.gson.Gson;

public class AddMemberAsync extends ATask {

	private Handler handler;
	
	public AddMemberAsync(Context context,Handler handler){
		this.handler = handler;
		try{
			memberId = String.valueOf(MemeberKeeper.getOauth(context).getId());
		}catch(Exception ee){
			
		}
	}

	@Override
	protected String doInBackground(String... params) {
		String toMemberId = params[0];//被授权的id
		String permissionIdS = params[1];//
		String roleIdS = params[2];//
		String deviceIdS = params[3];//
		String roleRemark = params[4];//
		String messageType = params[5];//
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("toMemberId", toMemberId));
		list.add(new BasicNameValuePair("fromMemberId", memberId));
		list.add(new BasicNameValuePair("permissionIdS", permissionIdS));
		list.add(new BasicNameValuePair("roleIdS", roleIdS));
		list.add(new BasicNameValuePair("deviceIdS", deviceIdS));
		list.add(new BasicNameValuePair("messageType", messageType));
		if(!TextUtils.isEmpty(roleRemark))
			list.add(new BasicNameValuePair("roleRemark", roleRemark));
		String result = null;
		try {
				result = httpRequest.post(Constants.URL_GIVEPERMISSION, list);
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
				msg.what = Constants.HANDLER_ADDMEMBER_SUCCESS;
				msg.obj = baseBo.getMessage();
				handler.sendMessage(msg);
				return;
				/*JSONObject json = new JSONObject(result);
				if(json.has("code")){
					String code = json.getString("code");
					if(code.equals("1")||code.equals("2")||code.equals("5")){
						msg.what = Constants.HANDLER_ADDMEMBER_SUCCESS;
						msg.obj = json.has("message")?json.getString("message"):"";
						handler.sendMessage(msg);
						return;
					}else if(json.has("message")){
						msg.what = Constants.HANDLER_ADDMEMBER_FAIL;
						msg.obj = json.getString("message");
						handler.sendMessage(msg);
						return;
					}
				}*/
			}catch(Exception e){
			}
		}
		if(!isHaveCache){
			msg.what = Constants.HANDLER_ADDMEMBER_FAIL;
			msg.obj = message;
			handler.sendMessage(msg);
		}
	}

	
	
}
