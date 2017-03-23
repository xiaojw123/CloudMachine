package com.cloudmachine.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;

public class QrCodeActivity extends BaseAutoLayoutActivity {

     private TitleView title_layout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.about);	
		super.onCreate(savedInstanceState);
		initTitleLayout();
	}

	@Override
	public void initPresenter() {

	}

	@Override
	protected void onResume() {
	//	MobclickAgent.onPageStart(UMengKey.time_setting_rqcode);
		super.onResume();
	}

	@Override
	protected void onPause() {
		//MobclickAgent.onPageEnd(UMengKey.time_setting_rqcode);
		super.onPause();
	}

	private void initTitleLayout(){
		
		title_layout = (TitleView)findViewById(R.id.title_layout);
		title_layout.setTitle("二维码");
		
		title_layout.setLeftOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 finish(); 
			}
		});
		
	}

}
