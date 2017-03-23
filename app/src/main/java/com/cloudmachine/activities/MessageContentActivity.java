package com.cloudmachine.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.cloudmachine.R;
import com.cloudmachine.base.BaseActivity;
import com.cloudmachine.utils.Constants;
import com.cloudmachine.utils.widgets.TitleView;

public class MessageContentActivity extends BaseActivity implements OnClickListener,Callback{
	
	private Context mContext;
	private Handler mHandler;
	private TextView content;
//	private ImageView backImg;
	private TitleView title_layout;
	private String title;
	private String contentStr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_message_content);
		getIntentData();
		mContext = this;
		mHandler = new Handler(this);
		content = (TextView)findViewById(R.id.content);
		content.setText(contentStr);
		initTitleLayout();
	}

	@Override
	protected void onResume() {
		//MobclickAgent.onPageStart(UMengKey.time_message_detail);
		super.onResume();
	}

	@Override
	protected void onPause() {
		//MobclickAgent.onPageEnd(UMengKey.time_message_detail);
		super.onPause();
	}

	private void initTitleLayout(){
		
		title_layout = (TitleView)findViewById(R.id.title_layout);
			title_layout.setTitle(title);
			title_layout.setLeftImage(-1,  new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					 finish(); 
				}
			});
	}
	@Override
	public void onClick(View v) {
		
	}
	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		
		return false;
	}

	private void getIntentData(){
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
        	try{
        		title = bundle.getString("title");
        		contentStr =bundle.getString("content");
        		
        	}catch(Exception e){
        		Constants.MyLog(e.getMessage());
        	}
        	
        }
    }
	
}
