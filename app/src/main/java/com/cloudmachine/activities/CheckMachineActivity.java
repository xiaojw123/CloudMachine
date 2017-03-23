package com.cloudmachine.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.cloudmachine.R;
import com.cloudmachine.adapter.OwnDeviceAdapter;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.struc.McDeviceInfo;
import com.cloudmachine.utils.Constants;

import java.util.ArrayList;

public class CheckMachineActivity extends BaseAutoLayoutActivity implements Callback, OnItemClickListener{

	private ListView lvOwnDevice;
	private Context mContext;
	private Handler mHandler;
	private ArrayList<McDeviceInfo> ownDeviceList;
	private OwnDeviceAdapter ownDeviceAdapter;
	private TitleView title_layout_about;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_machine);
		mContext = this;
		mHandler = new Handler(this);
		getIntentData();
		initView();
	}

	@Override
	public void initPresenter() {

	}

	@Override
	protected void onResume() {
		//MobclickAgent.onPageStart(UMengKey.time_repair_create_device);
		super.onResume();
	}

	@Override
	protected void onPause() {
		//MobclickAgent.onPageEnd(UMengKey.time_repair_create_device);
		super.onPause();
	}

	private void initView() {

		initTitleView();

		lvOwnDevice = (ListView) findViewById(R.id.listView);
		ownDeviceAdapter = new OwnDeviceAdapter(mContext, mHandler, ownDeviceList);
		lvOwnDevice.setAdapter(ownDeviceAdapter);
		lvOwnDevice.setOnItemClickListener(this);
	}

	private void initTitleView() {
		title_layout_about = (TitleView) findViewById(R.id.title_layout);
		title_layout_about.setTitle(getResources().getString(R.string.main_bar_text1));
		title_layout_about.setLeftOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	private void getIntentData() {
		Intent intent = this.getIntent();
		Bundle bundle = intent.getExtras();
		if (bundle != null) {
			try{
				ownDeviceList = (ArrayList<McDeviceInfo>) bundle.getSerializable(Constants.P_MAC_DEVICE);
			}catch(Exception e){
				Constants.MyLog(e.getMessage());
			}
		}
	}

	@Override
	public boolean handleMessage(Message arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent=new Intent();
		intent.putExtra("click_position", position);
		setResult(Constants.CLICK_POSITION,intent);
        finish();
	}

}
