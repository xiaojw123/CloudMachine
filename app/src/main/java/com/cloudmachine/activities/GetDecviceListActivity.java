package com.cloudmachine.activities;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.cloudmachine.R;
import com.cloudmachine.autolayout.widgets.TitleView;
import com.cloudmachine.base.BaseAutoLayoutActivity;

/**
 * 
 * @author shixionglu
 *获取设备列表页面
 */
public class GetDecviceListActivity extends BaseAutoLayoutActivity {
	
	private TitleView title_layout_about;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_devicelist);

		initView();
	}

	@Override
	public void initPresenter() {

	}

	private void initView() {
		title_layout_about = (TitleView) findViewById(R.id.title_layout_about);
		title_layout_about.setTitle(getResources().getString(R.string.about_title_text));
		title_layout_about.setLeftOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		
	}
}
