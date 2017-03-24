package com.cloudmachine.net.task;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.cloudmachine.net.ATask;
import com.cloudmachine.net.HttpURLConnectionImp;
import com.cloudmachine.net.IHttp;
import com.cloudmachine.struc.BaseBO;
import com.cloudmachine.struc.NewRepairInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.MemeberKeeper;
import com.cloudmachine.utils.Utils;
import com.google.gson.Gson;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

public class SubmitRepairAsync extends ATask{

	private Handler handler;
	private NewRepairInfo newRepairInfo;
	
	public SubmitRepairAsync(Context context,Handler handler,NewRepairInfo newRepairInfo){
		this.handler = handler;
		this.newRepairInfo =newRepairInfo;
		try{
			memberId = String.valueOf(MemeberKeeper.getOauth(context).getId());
		}catch(Exception ee){
			
		}
	}
	@Override
	protected String doInBackground(String... params) {
		IHttp httpRequest = new HttpURLConnectionImp();
		String result = null;
		try {
				result = httpRequest.post(Constants.URL_SAVEVBUSINESS, initListData());
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
				msg.what = Constants.HANDLER_NEWREPAIR_SUCCESS;
				msg.obj = baseBo.getMessage();
				handler.sendMessage(msg);
				return;
			}catch(Exception e){
			}
		} 
		//缓存数据第4步
		if(!isHaveCache){
			msg.what = Constants.HANDLER_NEWREPAIR_FAILD;
			msg.obj = message;
			handler.sendMessage(msg);
		}
	}
	
	
	private List<NameValuePair> initListData(){
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		
		list.add(Utils.addBasicValue("memberId", memberId));
		if(null != newRepairInfo){
			list.add(Utils.addBasicValue("vmacopname",newRepairInfo.getVmacopname()));
			list.add(Utils.addBasicValue("vmacoptel",newRepairInfo.getVmacoptel()));
			list.add(Utils.addBasicValue("pk_prod_def",newRepairInfo.getPk_prod_def()));
			list.add(Utils.addBasicValue("pk_brand",newRepairInfo.getPk_brand()));
			list.add(Utils.addBasicValue("pk_vhcl_material",newRepairInfo.getPk_vhcl_material()));
			list.add(Utils.addBasicValue("vdiscription",newRepairInfo.getVdiscription()));
			list.add(Utils.addBasicValue("vservicetype",newRepairInfo.getVservicetype()));
			list.add(Utils.addBasicValue("vworkaddress",newRepairInfo.getVworkaddress()));
			list.add(Utils.addBasicValue("province",newRepairInfo.getProvince()));
			if (null !=newRepairInfo.getDeviceId()) {
				list.add(Utils.addBasicValue("deviceId",newRepairInfo.getDeviceId()));
			}
			if (null != newRepairInfo.getVmachinenum()) {
				list.add(Utils.addBasicValue("vmachinenum",newRepairInfo.getVmachinenum()));
			}

			
//			list.add(Utils.addBasicValue("idCardPhoto", SparseArrayToString(resultMap)));
		}
		return list;
	}
}
