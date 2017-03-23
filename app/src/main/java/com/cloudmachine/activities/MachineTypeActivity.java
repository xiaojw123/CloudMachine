package com.cloudmachine.activities;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.widget.ListView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.MachineTypeListAdapter;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.net.task.MachineTypeListAsync;
import com.cloudmachine.struc.MachineTypeInfo;
import com.cloudmachine.utils.Constants;

import java.util.ArrayList;

public class MachineTypeActivity extends BaseAutoLayoutActivity implements Callback{
	
	private Handler mHandler;
	private Context mContext;
	private ArrayList<MachineTypeInfo> mList = new ArrayList<MachineTypeInfo>();
	private MachineTypeListAdapter machineTypeListAdapter;
	private ListView listMachineType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_machine_type);
		mHandler = new Handler(this);
		mContext = this;
		new MachineTypeListAsync(mContext, mHandler).execute();
		initView();

	}

	@Override
	public void initPresenter() {

	}

	private void initView() {
		listMachineType = (ListView) findViewById(R.id.lv_machine_type);
		machineTypeListAdapter = new MachineTypeListAdapter(mContext,mList);
		listMachineType.setAdapter(machineTypeListAdapter);
	}

	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case Constants.HANDLER_GETMACHINETYPE_SUCCESS:
			if (msg.obj != null) {
				ArrayList<MachineTypeInfo> machineTypeInfos = (ArrayList<MachineTypeInfo>) msg.obj;
				mList.addAll(machineTypeInfos);
				machineTypeListAdapter.notifyDataSetChanged();
			}
			break;

		default:
			break;
		}
		
		return false;
	}
}
