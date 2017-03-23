package com.cloudmachine.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.DeviceListAdapter;
import com.cloudmachine.base.BaseActivity;
import com.cloudmachine.net.task.DevicesListAsync;
import com.cloudmachine.struc.McDeviceInfo;
import com.cloudmachine.ui.repair.activity.NewRepairActivity;
import com.cloudmachine.utils.Constants;

import java.util.ArrayList;

public class DeviceListActivity extends BaseActivity implements Callback, OnItemClickListener{
	
	private Context mContext;
	private Handler mHandler;
	private ListView deviceList;
	private int DevicesList_Type = Constants.MC_DevicesList_AllType;
	private ArrayList<McDeviceInfo> mList = new ArrayList<McDeviceInfo>();
	private DeviceListAdapter deviceListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_list);
		
		mContext = this;
		mHandler = new Handler(this);
		new DevicesListAsync(DevicesList_Type, null, mContext, mHandler)
		.execute();
		initView();
	}

	private void initView() {
		deviceList = (ListView) findViewById(R.id.lv_list);			//初始化listview
		deviceList.setOnItemClickListener(this);
		deviceListAdapter = new DeviceListAdapter(mContext,mList);
		deviceList.setAdapter(deviceListAdapter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch (msg.what) {
		case Constants.HANDLER_GETDEVICELIST_SUCCESS:
			ArrayList<McDeviceInfo> info = (ArrayList<McDeviceInfo>) msg.obj;
			Constants.MyLog("拿到数据");
			if(null != info && info.size()>0){									//拿到数据，设置listview布局
				mList.clear();
				mList.addAll(info);
				deviceListAdapter.notifyDataSetChanged();
			}
			break;

		default:
			break;
		}
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(this,NewRepairActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("McDeviceInfo", mList.get(position));
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
		finish();
	}

}
