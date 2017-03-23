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
import com.cloudmachine.struc.CommentsInfo;
import com.cloudmachine.struc.RepairRecordBasicInfo;
import com.cloudmachine.struc.ScoreInfo;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.URLs;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 
 * @author shixionglu 获取单个报修记录基本信息
 */
public class GetOneRepairRecordAsync extends ATask {

	private Context mContext;
	private Handler handler;
	private long repairId;
	private long memberId;

	public GetOneRepairRecordAsync(Handler mHandler, Context mContext,
			long repairId, long memberId) {
		this.mContext = mContext;
		this.handler = mHandler;
		this.repairId = repairId;
		this.memberId = memberId;
	}

	@Override
	protected String doInBackground(String... params) {
		IHttp httpRequest = new HttpURLConnectionImp();
		List<NameValuePair> list = new ArrayList<NameValuePair>();
		list.add(new BasicNameValuePair("repairId", String.valueOf(repairId)));
		list.add(new BasicNameValuePair("memberId", String.valueOf(memberId)));
		String result = null;
		try {
			result = httpRequest
					.post(URLs.REPAIR_RECORD_BASIC_INFOMATION, list);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	protected void onPostExecute(String result) {
		// 缓存数据第2步
		super.onPostExecute(result);
		decodeJson(result);
	}

	@Override
	protected void decodeJson(String result) {
		// TODO Auto-generated method stub
		// 缓存数据第3步
		super.decodeJson(result);
		Message msg = Message.obtain();
		if (isSuccess) {
			try {
				Gson gson = new Gson();
				BaseBO<RepairRecordBasicInfo> bo = gson.fromJson(result,
						new TypeToken<BaseBO<RepairRecordBasicInfo>>() {
						}.getType());
				msg.what = Constants.HANDLER_REPAIR_RECORD_BASIC_INFOMATION_SUCCESS;
				msg.obj = bo.getResult();
				handler.sendMessage(msg);
				return;
			} catch (Exception e) {
			}
		} else {
		}
		// 缓存数据第4步
		if (!isHaveCache) {
			msg.what = Constants.HANDLER_REPAIR_RECORD_BASIC_INFOMATION_FAILED;
			msg.obj = message;
			handler.sendMessage(msg);
		}
	}

}
