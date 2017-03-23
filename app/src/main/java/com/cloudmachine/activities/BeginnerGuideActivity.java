package com.cloudmachine.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.RadiusButtonView;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.UMengKey;

import static com.umeng.analytics.MobclickAgent.onEvent;

public class BeginnerGuideActivity extends BaseAutoLayoutActivity implements
		OnClickListener {
	private Context mContext;
	private TitleView title_layout;
	private RadiusButtonView btnTrial;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beginner_guide);
		mContext = this;
		initView();
	}

	@Override
	public void initPresenter() {

	}

	private void initView() {
		// TODO Auto-generated method stub
		initTitleLayout();
		btnTrial = (RadiusButtonView) findViewById(R.id.btn_trial);
		btnTrial.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onEvent(mContext,
						UMengKey.mc_main_list_cloud_device_click);
				Bundle b = new Bundle();
				b.putLong(Constants.P_DEVICEID, 0);
				b.putString(Constants.P_DEVICENAME, "");
				b.putString(Constants.P_DEVICEMAC, "");
				Constants.toActivity(BeginnerGuideActivity.this, DeviceMcActivity.class, b, false);
			}
		});
	}
	private void initTitleLayout() {

		title_layout = (TitleView) findViewById(R.id.title_layout);
		title_layout.setTitle(getResources().getString(R.string.mc_novice_guide));
		title_layout.setLeftOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_trial:
			
			break;

		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		//MobclickAgent.onPageStart(UMengKey.time_machine_experience);
		super.onResume();
		//MobclickAgent.onPageStart(this.getClass().getName());
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		//MobclickAgent.onPageEnd(UMengKey.time_machine_experience);
		super.onPause();
		//MobclickAgent.onPageEnd(this.getClass().getName());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
